package gui;

import gorcery_store.Product;
import gorcery_store.Sale;
import gorcery_store.Store;
import gorcery_store.StoreSimulation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by abelsang on 2017-08-04.
 */
public class CashierGui extends SmallGui implements ActionListener {
  private StoreSimulation simulation;
  private Store store;
  private JLabel upc;
  private JTextField productUpc;
  private Product product;
  private JTextArea soldProducts;
  private JLabel totalPrice;
  private JTextField sellQuantity;
  private JFrame frame;
  private HashMap<String, Integer> sellingItems = new HashMap<>();

  public CashierGui(StoreSimulation simulation){
    this.frame = new JFrame("Cashier");
    this.simulation = simulation;
    this.store = simulation.getStore();
    this.addObserver(simulation);
    upc = new JLabel("The product's UPC:");
    productUpc = new JTextField(12);
    JLabel quantity = new JLabel("Quantity: ");
    sellQuantity = new JTextField(5);
    soldProducts = new JTextArea(15, 30);
    soldProducts.setEditable(false);
    totalPrice = new JLabel("total: 0");
    JButton Sell = new JButton("Sell");
    JButton Bill = new JButton("Bill");
    JButton Check = new JButton("Check Discount");
    JButton Cancel = new JButton("Cancel");
    Panel panel = new Panel();
    Panel panel1 = new Panel();
    panel.setLayout(new FlowLayout(0));
    panel1.setLayout(new GridLayout(0,1));
    // button add listener
    Sell.addActionListener(this);
    Bill.addActionListener(this);
    Check.addActionListener(this);
    Cancel.addActionListener(this);
    panel.add(upc);
    panel.add(productUpc);
    panel.add(quantity);
    panel.add(sellQuantity);
    panel1.add(Sell);
    panel1.add(Bill);
    panel1.add(Check);
    panel1.add(Cancel);
    panel1.add(totalPrice);
    frame.add("North",panel);
    frame.add("East",panel1);
    frame.add(new JScrollPane(soldProducts, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    //setSize(480,400);
    frame.setResizable(false);
//    setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.pack();
  }

  public void actionPerformed(ActionEvent e) {
    if (checkUpc(productUpc.getText())) {
      if (store.getProducts().containsKey(productUpc.getText())) {
        product = store.getProducts().get(productUpc.getText());
        switch (e.getActionCommand()) {
          //note: The default quantity is 1.
          case "Sell":
            int quantity;
            if (!sellQuantity.getText().equals("")) {
              quantity = Integer.valueOf(sellQuantity.getText());
              if (product.getQuantity() < quantity) {
                quantity = product.getQuantity();
              }
            } else {
              quantity = 1;
            }
            if (!sellingItems.containsKey(productUpc.getText())) {
              sellingItems.put(productUpc.getText(), quantity);
              String infor = product.getName() + "(" + product.getUpc() + ") X " + quantity + "\n";
              soldProducts.append(infor);
            } else {
              sellingItems.put(product.getUpc(), quantity + sellingItems.get(productUpc.getText()));
              soldProducts.setText("");
              for (String upcNum : sellingItems.keySet()) {
                Product p = store.getProducts().get(upcNum);
                String information = p.getName() + "(" + productUpc.getText() + ") X "
                    + sellingItems.get(upcNum) + "\n";
                soldProducts.append(information);
              }

            }
            updatePrice(quantity);
            // ques: cancel one item
//          simulation.getLogger().log(Level.INFO, "Now the quantity of " + product.getName() + " is "
//              + product.getQuantity() + " reduced by " + quantity);
            setChanged();
            notifyObservers("Scanned " + product.getName() + " with quantity of " + quantity);
            break;
          case "Check Discount":
            checkDiscount();
            break;

        }
      } else if (e.getActionCommand().equals("Bill")) {
        for (String upcNum : sellingItems.keySet()) {
          product = store.getProducts().get(upcNum);
          product.addQuantity(-sellingItems.get(upcNum));
          //NOTE: DON'T REVISE product.getSale().price!!!!
          Sale newSale = new Sale(product.getSale().price, simulation.getDATE(),
              sellingItems.get(upcNum),
              product.getUpc(),
              product.getSale().getStartDate(), product.getSale().getEndDate(),
              product.getSale().getDiscount());
          newSale.setHistory(product.getSale().getHistory());
          //NOTE: DON'T REVISE newSale.price!!!!
          newSale.addHistory(newSale.price, newSale.getQuantity());
          product.setSale(newSale);
          store.getSaleInOneDay().add(product.getSale());
          checkThreshold();

          setChanged();
          notifyObservers("Now the quantity of " + product.getName() + " is "
              + product.getQuantity() + " reduced by " + sellingItems.get(upcNum));
        }
        sellingItems.clear();
        soldProducts.setText("");
        totalPrice.setText("Total: 0");
        simulation.getLogger().log(Level.INFO, "The customer has already paid the bill.");
      } else if (e.getActionCommand().equals("Cancel")) {
        if (sellingItems.size() != 0 && checkUpc(soldProducts.getSelectedText())) {
          product = store.getProducts().get(soldProducts.getSelectedText());
          String oldPrice = totalPrice.getText().
              substring(totalPrice.getText().lastIndexOf(": ") + 2);
          double newPrice = Integer.valueOf(oldPrice) - product.getSale().getPrice() *
              sellingItems.get(soldProducts.getSelectedText());
          totalPrice.setText(totalPrice.getText().replaceAll(oldPrice, String.valueOf(newPrice)));
          sellingItems.remove(soldProducts.getSelectedText());
          soldProducts.setText("");

          for (String upcNum : sellingItems.keySet()) {
            product = store.getProducts().get(upcNum);
            String information = product.getName() + "(" + product.getUpc() + ") X "
                + sellingItems.get(upcNum) + "\n";
            soldProducts.append(information);
          }
        } else {
          WarningGui wg = new WarningGui();
        }
      }
    } else {
      WarningGui wg = new WarningGui();
    }
  }

  private void updatePrice(int quantity) {
    String oldPrice = totalPrice.getText()
        .substring(2 + totalPrice.getText().lastIndexOf(": "));
    int total = Integer.valueOf(oldPrice);
    total += product.getSale().getPrice() * quantity;
    totalPrice.setText(totalPrice.getText().replaceAll(oldPrice, String.valueOf(total)));
    sellQuantity.setText("");
    productUpc.setText("");


    //

  }

  private void checkThreshold() {
    if (product.getQuantity() < product.getThreshold()){
      ArrayList order = new ArrayList<>();
      order.add(product.getName()+" ("+ product.getUpc() + ")");
      order.add(String.valueOf(product.getOrder().price));
      order.add(String.valueOf(product.getThreshold() * 3));
      store.getPendingOrder().add(order);
      simulation.getLogger().log(Level.INFO,
          "The inventory ( quantity: " + product.getQuantity() + ") of " + product.getName()
              + " is below the threshold " + product.getThreshold()
              + ". The information has automatically sent to the manager.");
    }
  }
  private void checkDiscount() {
    JFrame frame = new JFrame();
    frame.setSize(600, 100);
    JLabel label = null;    
    if(!checkUpc(productUpc.getText())){
      WarningGui warning = new WarningGui();
      productUpc.setText("");
      sellQuantity.setText("");
    }else if (product.getSale().getDiscount() == 1.0) {
      label = new JLabel("There is no discount for " + product.getName());
      setChanged();
      notifyObservers("There is no discount for " + product.getName());
    } else {
      String msg = "Product " + product.getName() + " starts to discount to " + product.getSale()
          .getPrice()
          + " from date " + product.getSale().getStartDate() + " to " + product.getSale()
          .getEndDate();
      label = new JLabel(msg);
      setChanged();
      notifyObservers(msg);
    }
    frame.add(label);
    frame.setVisible(true);
  }
  public Product getProduct() {
    return this.product;
  }

  public StoreSimulation getSimulation() {
    return this.simulation;
  }

  public Store getStore() {
    return store;
  }

}


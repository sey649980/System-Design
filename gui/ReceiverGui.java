package gui;

import gorcery_store.Product;
import gorcery_store.Store;
import gorcery_store.StoreSimulation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by abelsang on 2017-08-04.
 */
public class ReceiverGui extends SmallGui implements ActionListener {
  private JTextArea textArea = new JTextArea(15, 30);
  private StoreSimulation simulation;
  private Store store;
  private JLabel upc;
  private JTextField productUpc;
  private JFrame frame;
  private Product product;

  public ReceiverGui(StoreSimulation simulation){
    this.simulation = simulation;
    this.store = simulation.getStore();
    addObserver(simulation);
    textArea.setEditable(false);
    upc = new JLabel("The product's UPC:");
    productUpc = new JTextField(12);
    frame = new JFrame("Receiver");
    Panel panel = new Panel();
    Panel panel1 = new Panel();
    panel1.setLayout(new FlowLayout(0));
    panel.setLayout(new GridLayout(0, 1));
    JButton addQuantity = new JButton("Received");
    JButton priceHistory = new JButton("PriceHistory");
    JButton location = new JButton("Location");
    JButton price = new JButton("Price");
    JButton cost = new JButton("Cost");
//    JButton exit = new JButton("Exit");
//    exit.addActionListener(this);
    // button add listener
    addQuantity.addActionListener(this);
    priceHistory.addActionListener(this);
    location.addActionListener(this);
    price.addActionListener(this);
    cost.addActionListener(this);
//    addQuantity.setActionCommand("AddQuantity");
//    priceHistory.setActionCommand("PriceHistory");
//    location.setActionCommand("Location");
//    price.setActionCommand("Price");
//    cost.setActionCommand("Cost");
//    addNewItem.setActionCommand("AddNewItem");
    panel1.add(upc);
    panel1.add(productUpc);
    panel.add(addQuantity);
    panel.add(priceHistory);
    panel.add(location);
    panel.add(cost);
    panel.add(price);
    frame.add("North",panel1);
    frame.add("East",panel);
//    add(exit);
    frame.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    //setSize(480,400);
    frame.setResizable(false);
//    setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.pack();
  }

  public void actionPerformed(ActionEvent e){
    if(e.getActionCommand().equals("Received")){
      AddQuantityGui ag = new AddQuantityGui(this);
    }else if (store.getProducts().containsKey(productUpc.getText())) {
      product = store.getProducts().get(productUpc.getText());
      switchCommand(e);
    }else {
      WarningGui warning = new WarningGui();
      productUpc.setText("");
    }

//    if (e.getActionCommand().equals("Exit")) {
//      this.dispose();
//    }
  }

  private void switchCommand(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "PriceHistory":
        String result = "The price history of " + product.getName() + ": " + "\n";
        for (Integer key : product.getSale().getHistory().keySet()) {
          result += String.valueOf(key) + ": " + product.getSale().getHistory().get(key) + "\n";
        }
        textArea.setText(result);
        setChanged();
        notifyObservers(result);
        break;
      case "Location":
        textArea.setText(product.getName() + " UPC: " + product.getUpc()
            + " is at location " + product.getLocation());
        setChanged();
        notifyObservers(product.getName() + " UPC: " + product.getUpc()
            + " is at location " + product.getLocation());
        break;
      case "Price":
        textArea.setText(product.getName() + " UPC: " + product.getUpc()
            + " now the current price is " + product.getSale().price);
        setChanged();
        notifyObservers(product.getName() + " UPC: " + product.getUpc()
            + " now the current price is " + product.getSale().price);
        break;
      case "Cost":
        textArea.setText(product.getName() + " UPC: " + product.getUpc()
            + " cost " + product.getOrder().price + " now per unit.");
        setChanged();
        notifyObservers(product.getName() + " UPC: " + product.getUpc()
            + " cost " + product.getOrder().price + " now per unit.");
        break;
    }
  }

  public Product getProduct() {
    return this.product;
  }

  public StoreSimulation getSimulation() {
    return this.simulation;
  }

  public void setTextArea(String str) {
    this.textArea.setText(str);
  }

  public Store getStore() {
    return store;
  }

}

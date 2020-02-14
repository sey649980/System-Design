package gui;

import gorcery_store.Product;
import gorcery_store.Section;
import gorcery_store.Store;
import gorcery_store.SubSection;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by abelsang on 2017-08-04.
 */
public class AddNewItemGui extends SmallGui implements ActionListener{
  private HandleProductGui hg;
  private JLabel msg;
  private JLabel upc;
  private JTextField productUpc;
  private JLabel name;
  private JTextField productName;
  private JLabel price;
  private JTextField productPrice;
  private JLabel quantity;
  private JTextField productQuantity;
  private JLabel section;
  private JTextField productSection;
  private JLabel location;
  private JTextField productLocation;
  private JLabel threshold;
  private JTextField productThreshold;
  private JButton ok;
  private JPanel panel, panel1;
  private JFrame frame;
  private Store store;
  public AddNewItemGui(HandleProductGui hg){
    this.hg = hg;
    this.store = hg.getStore();
    addObserver(hg.getSimulation());
    frame = new JFrame("Add New Item");
    panel = new JPanel();
    panel1 = new JPanel();
    msg = new JLabel("Please fill the following info to add new product: ");
    upc = new JLabel("UPC:");
    productUpc = new JTextField(12);
    name = new JLabel("Name:");
    productName = new JTextField(10);
    price = new JLabel("Price:");
    productPrice = new JTextField(6);
    quantity = new JLabel("Quantity:");
    productQuantity = new JTextField(6);
    section = new JLabel("Section:");
    productSection = new JTextField(8);
    location = new JLabel("Location:");
    productLocation = new JTextField(6);
    threshold = new JLabel("Threshold");
    productThreshold = new JTextField(6);
    ok = new JButton("OK");
    ok.addActionListener(this);
//    ok.addActionListener(new CloseListener());
    frame.add("North",msg);
    panel.add(upc);
    panel.add(productUpc);
    panel.add(name);
    panel.add(productName);
    panel.add(price);
    panel.add(productPrice);
    panel1.add(quantity);
    panel1.add(productQuantity);
    panel1.add(section);
    panel1.add(productSection);
    panel1.add(location);
    panel1.add(productLocation);
    panel1.add(threshold);
    panel1.add(productThreshold);
    panel1.add(ok);
    panel.setLayout(new FlowLayout(0));
    panel1.setLayout(new FlowLayout(2));
    frame.add("Center",panel);
    frame.add("South",panel1);
    //setSize(480,400);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }

  //ques: for all input, what if the inputs are wrong type.
  public void actionPerformed(ActionEvent e){

    if (! checkUpc(productUpc.getText()) ||
        ! store.getProducts().containsKey(productUpc.getText()) ||
        ! price.getText().matches("[0-9]*\\.[0-9]*") ||
        ! quantity.getText().matches("[0-9]*") ||
        ! threshold.getText().matches("[0-9]*")){

      WarningGui wg = new WarningGui();
      productUpc.setText("");
      productName.setText("");
      productSection.setText("");
      productQuantity.setText("");
      productLocation.setText("");
      productThreshold.setText("");
      productPrice.setText("");
    } else {
      Double orderPrice = Double.valueOf(productPrice.getText());
      int amount = Integer.valueOf(productQuantity.getText());
      String[] secString = productSection.getText().split("-");
      Section section = store.getSection(secString[0]);
      SubSection subSection;
      if (!section.getContainer().containsKey(secString[1])) {
        subSection = new SubSection(secString[1]);
        store.addSubSection(subSection,section);
      } else {
        subSection = section.getContainer().get(secString[1]);
      }
      addNew(subSection, amount,orderPrice);
      productUpc.setText("");
      productName.setText("");
      productSection.setText("");
      productQuantity.setText("");
      productLocation.setText("");
      productThreshold.setText("");
      productPrice.setText("");
      frame.dispose();
    }
  }
  private void addNew(SubSection subSection, int amount, double orderPrice) {
    Product product = new Product(productUpc.getText(), productName.getText(), 0,
        Integer.valueOf(productThreshold.getText()), subSection, productLocation.getText(),
        orderPrice, 0, hg.getSimulation().getDATE());
    product.getOrder().setUpc(product.getName());
    product.getOrder().setPriceAndQuantity(orderPrice, amount);
    subSection.addProduct(product);
    store.getProducts().put(product.getUpc(), product);
    store.getOrderInOneDay().add(product.getOrder());
    ArrayList<String> order = new ArrayList<>();
    order.add(product.getName()+" ("+ product.getUpc() + ")");
    order.add(String.valueOf(product.getOrder().price));
    order.add(String.valueOf(amount));
    store.getPendingReceived().add(order);
    product.setStatus("Order");
    hg.setTextArea("Now we have a new product. The following are the details.." + "\n"
        + product.toString());
    setChanged();
    notifyObservers("Now we have a new product !! The following are the details !");
    setChanged();
    notifyObservers(product.toString());
  }
}

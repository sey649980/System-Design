package gui;

import gorcery_store.Order;
import gorcery_store.Product;
import gorcery_store.Sale;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by abelsang on 2017-08-06.
 */
public class SectionInfoGui extends SmallGui implements ActionListener {
  private RevenueAndCostGui revenueAndCostGui;
  private JLabel msg;
  private JTextField section;
  private JTextArea result;
  private JButton cost;
  private JButton revenue;
  private JPanel panel,panel1;
  private JFrame frame;
  public SectionInfoGui(RevenueAndCostGui revenueAndCostGui){
    this.revenueAndCostGui = revenueAndCostGui;
    addObserver(revenueAndCostGui.getSimulation());
    frame = new JFrame();
    panel = new JPanel();
    panel1 = new JPanel();
    msg = new JLabel("Please type the section :");
    section = new JTextField(10);
    cost = new JButton("Section Cost");
    cost.addActionListener(this);
    revenue = new JButton("Section Revenue");
    revenue.addActionListener(this);
    result = new JTextArea(15, 30);
    panel.add(msg);
    panel.add(section);
    panel1.add(revenue);
    panel1.add(cost);
    frame.add(new JScrollPane(result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    panel.setLayout(new FlowLayout(0));
    panel1.setLayout(new GridLayout(0,1));
    frame.add("East",panel1);
    frame.add("North",panel);
    
//    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    //setSize(500, 250);
    //ques: use?
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }
  //ques: the empty string would cause the program fails
  public void actionPerformed(ActionEvent e){
    if (! revenueAndCostGui.getStore().getContainer().containsKey(section.getText())){
      WarningGui wg = new WarningGui();
    } else {
    switch (e.getActionCommand()) {
      case "Section Cost":
        String str1 = section.getText() + "'s cost is : "
            + sectionCost(section.getText());
        result.setText(str1);
        setChanged();
        notifyObservers(str1);
        break;
      case "Section Revenue":
        String str2 = section.getText() + "'s revenue is : "
            + sectionRevenue(section.getText());
        result.setText(str2);
        setChanged();
        notifyObservers(str2);
        break;
    }
    }
  }

  public double sectionCost(String section) {
    ArrayList<Order> orderInOneDay = revenueAndCostGui.getStore().getOrderInOneDay();
    Map<String, Product> products = revenueAndCostGui.getStore().getProducts();
    double cost = 0;
    for (Order order : orderInOneDay) {
      Product product = products.get(order.getUpc());
      if (product.getSection().getParent().getName().equals(section)) {
        cost = cost + order.getPrice() * product.getQuantity();
      }
    }
    return cost;
  }
  public double sectionRevenue(String section) {
    ArrayList<Sale> saleInOneDay = revenueAndCostGui.getStore().getSaleInOneDay();
    Map<String, Product> products = revenueAndCostGui.getStore().getProducts();
    double revenue = 0;
    for (Sale sale : saleInOneDay) {
      Product product = products.get(sale.getUpc());
      if (product.getSection().getParent().getName().equals(section)) {
        revenue = revenue + sale.getPrice() * sale.getQuantity();
      }
    }
    return revenue;
  }
}

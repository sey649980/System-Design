package gui;

import gorcery_store.Order;
import gorcery_store.Product;
import gorcery_store.ProductEvent;
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
public class ProductInfoGui extends SmallGui implements ActionListener{
  private RevenueAndCostGui revenueAndCostGui;
  private JLabel upc;
  private JTextField productUpc;
  private JTextArea result;
  private JLabel start;
  private JTextField startDate;
  private JLabel end;
  private JTextField endDate;
  private JPanel panel,panel1;
  private JFrame frame;
  public ProductInfoGui(RevenueAndCostGui revenueAndCostGui){
    this.revenueAndCostGui = revenueAndCostGui;
    addObserver(revenueAndCostGui.getSimulation());
    frame = new JFrame("Product Info");
    panel = new JPanel();
    panel1 = new JPanel();
    // ques: not a good experience.  One way is that delete all infor and only remain upc and the other things should in field..
    upc = new JLabel("Please type the upc of the product:");
    productUpc = new JTextField(12);
    JButton cost = new JButton("Product Cost");
    cost.addActionListener(this);
    JButton revenue = new JButton("Product Revenue");
    revenue.addActionListener(this);
    start = new JLabel("start date:");
    startDate = new JTextField(12);
    end = new JLabel("end date:");
    endDate = new JTextField(12);
    result = new JTextArea(15, 30);
    panel.add(upc);
    panel.add(productUpc);
    panel.add(start);
    panel.add(startDate);
    panel.add(end);
    panel.add(endDate);
    panel1.add(revenue);
    panel1.add(cost);
    panel.setLayout(new FlowLayout(0));
    panel1.setLayout(new GridLayout(0,1));
    frame.add(new JScrollPane(result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    frame.add("North",panel);
    frame.add("East", panel1);
//    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    setSize(960, 250);
    //ques: use?
    frame.setResizable(true);
    frame.setVisible(true);
    frame.pack();
  }
  //ques: the empty string would cause the program fails
  public void actionPerformed(ActionEvent e){
    switch (e.getActionCommand()) {
      case "Product Revenue":
        String str1 = productUpc.getText() + "'s revenue is : "
            + productRevenue(productUpc.getText());
        result.setText(str1);
        setChanged();
        notifyObservers(str1);
        break;
      case "Product Cost":
        String str2 = productUpc.getText() + "'s cost is : "
            + productCost(productUpc.getText());
        result.setText(str2);
        setChanged();
        notifyObservers(str2);
        break;
    }
  }

  public double productCost(String upc) {
    ArrayList<Order> orderInOneDay = revenueAndCostGui.getStore().getOrderInOneDay();
    Map<String, Product> products = revenueAndCostGui.getStore().getProducts();
    double cost = 0;
    for (Order p : orderInOneDay) {
      if (p.getUpc().equals(upc)) {
        cost = calculateResult(p, cost);
      }
    }

    return cost;
  }

  public double productRevenue(String upc) {
    ArrayList<Sale> saleInOneDay = revenueAndCostGui.getStore().getSaleInOneDay();
    Map<String, Product> products = revenueAndCostGui.getStore().getProducts();
    double revenue = 0;
    for (Sale p : saleInOneDay) {
      if (p.getUpc().equals(upc)) {
        revenue = calculateResult(p, revenue);
      }
    }
    return revenue;
  }
  private double calculateResult(ProductEvent p, double result){
    if((startDate.getText().isEmpty())||(endDate.getText().isEmpty())){
      result = result + p.getPrice() * p.getQuantity();
    } else {
      for (int date : p.getHistory().keySet()) {
        int startD = Integer.valueOf(startDate.getText());
        int endD = Integer.valueOf(endDate.getText());
        if ((startD <= date)&&(date <= endD)) {
//          result = result + p.getHistory().get(date).get(0) * p.getHistory().get(date).get(1);
        }
      }
    }
    return result;
  }
}

package gui;

import gorcery_store.Store;
import gorcery_store.StoreSimulation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Created by abelsang on 2017-08-08.
 */
public class RevenueAndCostGui extends SmallGui implements ActionListener {
  private JFrame frame;
  private JPanel panel;
  private JTextArea textArea;
  private Store store;
  private ManagerGui mg;
  public RevenueAndCostGui(ManagerGui mg) {
    this.mg = mg;
    store = mg.getStore();
    addObserver(mg.getSimulation());
    frame = new JFrame("Revenue And Cost");
    panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));
    textArea = new JTextArea(15, 30);
    JButton Revenue = new JButton("Revenue");
    JButton Profit = new JButton("Profit");
    JButton range = new JButton("Range Info");
    JButton productInfo = new JButton("Product Info");
    JButton sectionInfo = new JButton("Section Info");
    Revenue.addActionListener(this);
    Profit.addActionListener(this);
    range.addActionListener(this);
    productInfo.addActionListener(this);
    sectionInfo.addActionListener(this);
    panel.add(Revenue);
    panel.add(Profit);
    panel.add(range);
    panel.add(productInfo);
    panel.add(sectionInfo);
    frame.add("West",new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    frame.add("East",panel);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }
  public void actionPerformed(ActionEvent e){
    switch (e.getActionCommand()) {
      case "Revenue":
        textArea.setText("Today's Revenue is:" + String.valueOf(store.revenue()));
        setChanged();
        notifyObservers("Today's Revenue is:" + String.valueOf(store.revenue()));
        break;
      case "Profit":
        textArea.setText("Today's Profit is: " + String.valueOf(store.profit()));
        setChanged();
        notifyObservers("Today's Profit is: " + String.valueOf(store.profit()));
        break;
      case "Range Info":
        RangeInfoGui rig = new RangeInfoGui(this);
        break;
      case "Product Info":
        ProductInfoGui pig = new ProductInfoGui(this);
        break;
      case "Section Info":
        SectionInfoGui sig = new SectionInfoGui(this);
        break;
    }
  }

  public StoreSimulation getSimulation() {
    return mg.getSimulation();
  }

  public Store getStore() {
    return store;
  }
}

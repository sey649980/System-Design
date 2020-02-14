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
public class HandleProductGui extends SmallGui implements ActionListener {
  private ManagerGui mg;
  private JPanel panel;
  private JFrame frame;
  private JTextArea textArea;
  public HandleProductGui(ManagerGui mg){
    this.mg = mg;
    frame = new JFrame("Handle Product");
    panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));
    addObserver(mg.getSimulation());
    textArea = new JTextArea(15, 30);
    JButton addNewItem = new JButton("Add New Item");
    JButton setSellPrice = new JButton("Set Sell Price");
    JButton setDiscount = new JButton("Set Discount");
    addNewItem.addActionListener(this);
    setSellPrice.addActionListener(this);
    setDiscount.addActionListener(this);
    panel.add(addNewItem);
    panel.add(setSellPrice);
    panel.add(setDiscount);
    frame.add("West",new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    frame.add("East",panel);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Set Sell Price":
        SetSellPriceGui ssp = new SetSellPriceGui(this);
        break;
      case "Set Discount":
        SetDiscountGui sdc = new SetDiscountGui(this);
        break;

      case "Add New Item":
        AddNewItemGui ang = new AddNewItemGui(this);
        break;

    }
  }

  public StoreSimulation getSimulation() {
    return mg.getSimulation();
  }

  public Store getStore() {
    return mg.getStore();
  }

  public void setTextArea(String str) {
    mg.setTextArea(str);
  }
}

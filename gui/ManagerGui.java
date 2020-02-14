package gui;

import gorcery_store.Product;
import gorcery_store.Store;
import gorcery_store.StoreSimulation;
import gorcery_store.WorkerAccount;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ManagerGui extends SmallGui implements ActionListener{
  private JTextArea textArea;
  private Store store;
  private StoreSimulation simulation;
  private WorkerAccount workers;
  private JButton pendingOrder;
  private JFrame frame;
  public ManagerGui(WorkerAccount acc, StoreSimulation simulation ){
    this.simulation = simulation;
    this.store = simulation.getStore();
    this.workers = acc;
    addObserver(simulation);
    frame = new JFrame("Manager");
    textArea = new JTextArea(15, 30);
    pendingOrder = new JButton("Pending Order");
    JButton workerManagement = new JButton("Worker Management");
    JButton addSection = new JButton("Add New Section");
    JButton revAndCost = new JButton("Revenue and Cost");
    JButton handle = new JButton("Handle Product");
    Panel panel = new Panel();
    panel.setLayout(new GridLayout(0, 1));
    // button add listener
    pendingOrder.addActionListener(this);
    workerManagement.addActionListener(this);
    addSection.addActionListener(this);
    revAndCost.addActionListener(this);
    handle.addActionListener(this);
    frame.add("West",new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
    panel.add(pendingOrder);
    panel.add(workerManagement);
    panel.add(addSection);
    panel.add(revAndCost);
    panel.add(handle);
    frame.add("East",panel);
    //setSize(480,400);
    frame.setResizable(false);
    //note: EXIT_ON_CLOSE would close all windows. but the default value only close the one.
    //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.pack();
  }
  
  public void actionPerformed(ActionEvent e){
    switch (e.getActionCommand()) {
      case "Worker Management":
        WorkerManagement wmg = new WorkerManagement(this);
        break;
      case "Pending Order":
        textArea.setText(store.getOrderListString());
        setChanged();
        notifyObservers(store.getOrderListString());
        pendingOrder.setText("Purchase");
        break;
      case "Purchase":
        if (! textArea.getText().equals("This is no pending order. \n"
            + "All pending orders have been purchased.")) {
          purchase();
        }
        pendingOrder.setText("Pending Order");
        break;
      case "Add New Section":
        AddSectionGui ad = new AddSectionGui(this);
        break;
      case "Revenue and Cost" :
        RevenueAndCostGui revenueAndCostGui = new RevenueAndCostGui(this);
        break;
      case "Handle Product":
        HandleProductGui handleProductGui = new HandleProductGui(this);
    }
  }
  private void purchase() {
    String[] orders= textArea.getText().split("\n");
    ArrayList<ArrayList<String>> newOrders = new ArrayList<>();
    for (String order : orders){
      //note: regex
      String[] array_ = order.split("[:;]");
      ArrayList<String> newOrder = new ArrayList<>();
      newOrder.add(array_[1].substring(1));
      newOrder.add(array_[3].substring(1));
      newOrder.add(array_[5].substring(1));
      newOrders.add(newOrder);
    }
    for (ArrayList<String> order: newOrders){
      int upcLength = order.get(0).length();
      String upc = order.get(0).substring(upcLength-13, upcLength-1);
      Product p = store.getProducts().get(upc);
      p.getOrder().setUpc(upc);
      p.getOrder().setDate(simulation.getDATE());

      p.getOrder().setPriceAndQuantity(Double.valueOf(order.get(1)), Integer.valueOf(order.get(2)));
      store.getPendingReceived().add(order);

      //in order to remove order from the pending order, we need change order's quantity
      // into the default value.
      //note: copy method
      ArrayList<String> oldOrder = new ArrayList<>(order);
      oldOrder.remove(2);
      oldOrder.add(String.valueOf(p.getThreshold() * 3));
      store.getPendingOrder().remove(oldOrder);

      //ques: no add it into Order.history
      p.setStatus("Reorder");
      store.getOrderInOneDay().add(p.getOrder());
      textArea.setText(store.getOrderListString());
      setChanged();
      notifyObservers(p.toString() + " by " +
          p.getOrder().getQuantity() + " from "+p.getOrder().companyName);
    }
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
  public WorkerAccount getWorkerAccount(){
    return workers;
  }
}

package gui;

import gorcery_store.Product;

import gorcery_store.Store;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Created by abelsang on 2017-08-04.
 */
public class AddQuantityGui extends SmallGui implements ActionListener {
  private ReceiverGui rg;
  private JLabel msg;
  private JTextArea pendingOrder;
  private JButton ok;
  private JPanel panel;
  private JFrame frame;
  private Store store;
  public AddQuantityGui(ReceiverGui rg){
    this.rg = rg;
    store = rg.getStore();
    addObserver(rg.getSimulation());
    frame = new JFrame("Add Quantity");
    panel = new JPanel();
    // ques: not a good experience.  One way is that delete all info and only remain upc and the other things should in field..
    msg = new JLabel("Please select the product UPC received.");
    pendingOrder = new JTextArea(15, 30);
    pendingOrder.setEditable(false);
    pendingOrder.setText(rg.getStore().getPendingReceivedItems());
    ok = new JButton("OK");
    ok.addActionListener(this);
//    ok.addActionListener(new CloseListener());
    panel.add(msg);
    panel.add(pendingOrder);
    panel.add(ok);
    panel.setLayout(new FlowLayout(0));
    frame.add(new JScrollPane(pendingOrder, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    frame.add("North",panel);
//    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    //setSize(500, 250);
    //note: cannot resize if false
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }
  public void actionPerformed(ActionEvent e){
    if (! pendingOrder.getText().equals("This are no pending received items. \n"
        + "All pending items have been received.")) {

      if (!checkUpc(pendingOrder.getSelectedText())) {
        WarningGui warning = new WarningGui();
      } else {
        Product product = store.getProducts().get(pendingOrder.getSelectedText());
        product.addQuantity(product.getOrder().getQuantity());
        ArrayList<String> list = new ArrayList<>();
        list.add(product.getName() + " (" + product.getUpc() + ")");
        list.add(String.valueOf(product.getOrder().price));
        list.add(String.valueOf(product.getOrder().getQuantity()));
        store.getPendingReceived().remove(list);
        pendingOrder.setText(rg.getStore().getPendingReceivedItems());
        rg.setTextArea("Now the quantity of " + product.getName() + " is " + product.getQuantity());
        setChanged();
        notifyObservers("Now the quantity of " + product.getName() + " is "
            + product.getQuantity());
      }
    } else {
      frame.dispose();
    }
  }
}

package gui;

import gorcery_store.Product;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SetSellPriceGui extends SmallGui implements ActionListener {
  private HandleProductGui hg;
  private JLabel input1;
  private JLabel input2;
  private JTextArea textArea1 = new JTextArea(2, 30);
  private JTextArea textArea2 = new JTextArea(2, 30);
  private JTextArea outputArea = new JTextArea(15, 30);
  private JButton go;
  private JFrame frame;
  private Product product;
  
  public SetSellPriceGui(HandleProductGui hg) {
    this.hg = hg;

    addObserver(hg.getSimulation());
    frame = new JFrame("Set Sell Price");
    frame.setLayout(new FlowLayout());
    input1 = new JLabel("Please type UPC of your product here:");
    input2 = new JLabel("Please set your sell price here:");
    go = new JButton("Go");

    go.addActionListener(this);

    frame.add(input1);
    frame.add(textArea1);
    frame.add(input2);
    frame.add(textArea2);
    frame.add(go);
    frame.add(new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    frame.setSize(400, 480);
    frame.setResizable(true);
    frame.setVisible(true);

  }

  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Go":
        product = hg.getStore().getProducts().get(textArea1.getText());
        if (product == null || ! textArea2.getText().matches("[0-9]*")) {
          WarningGui wg = new WarningGui();
          outputArea.setText("Invalid UPC number. Please enter it again.");
          textArea1.setText("");
          textArea2.setText("");
        } else{
          product.getSale().price = Double.parseDouble(textArea2.getText());
          outputArea.append("Setting price successfully. Now the price for "
              + String.valueOf(product.getName()) + " is " + 
              String.valueOf(textArea2.getText()));
          setChanged();
          notifyObservers("Setting price successfully. Now the price for "
              + String.valueOf(product.getName()) + " is " +
              String.valueOf(textArea2.getText())+ ". ");
          textArea1.setText("");
          textArea2.setText("");
        }
    }
  }
}


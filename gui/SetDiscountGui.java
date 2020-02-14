package gui;

import gorcery_store.Product;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SetDiscountGui extends SmallGui implements ActionListener{
  private HandleProductGui hg;
  private JLabel input1;
  private JLabel input2;
  private JLabel input3;
  private JLabel input4;
  private JTextArea textArea1;
  private JTextArea textArea2;
  private JTextArea textArea3;
  private JTextArea textArea4;
  private JTextArea outputArea;
  private JButton go;
  private Product product;
  private JPanel panel, panel1, panel2;
  private JFrame frame;
  public SetDiscountGui(HandleProductGui hg){
    this.hg = hg;
    addObserver(hg.getSimulation());
    frame = new JFrame("Discount");
    panel = new JPanel();
    panel1 = new JPanel();
    panel2 = new JPanel();
    textArea1 = new JTextArea(2, 30);
    textArea2 = new JTextArea(2, 5);
    textArea3 = new JTextArea(2, 5);
    textArea4 = new JTextArea(2, 5);
    outputArea = new JTextArea(15, 30);
    input1 = new JLabel("Please type UPC of your product here:");
    input2 = new JLabel("Discount:");
    input3 = new JLabel("Start Date:");
    input4 = new JLabel("End Date:");
    go = new JButton("Go");
    
    go.addActionListener(this);
    
    panel.add(input1);
    panel.add(textArea1);
    panel1.add(input2);
    panel1.add(textArea2);
    panel1.add(input3);
    panel1.add(textArea3);
    panel1.add(input4);
    panel1.add(textArea4);
    panel1.add(go);
    panel.setLayout(new FlowLayout(0));
    panel1.setLayout(new FlowLayout(0));
    panel2.add("North", panel);
    panel2.add("South", panel1);
    panel2.setLayout(new GridLayout(0,1));
    frame.add("North",panel2);
    frame.add(new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
    
    
    
    //setSize(400,480);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
    
  }
  
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Go":
        product = hg.getStore().getProducts().get(textArea1.getText());
        if (product == null) {
          WarningGui wg = new WarningGui();
          outputArea.setText("Invalid UPC number. Please enter it again.");
          cleatTextArea();
        } else if (! textArea2.getText().matches("0\\.[0-9]*") ||
            ! checkDates(textArea3.getText(),textArea4.getText())){
          WarningGui wg = new WarningGui();
        }else{
          product.getSale().setStartDate(textArea3.getText());
          product.getSale().setEndDate(textArea4.getText());
          product.getSale().setDiscount(Double.parseDouble(textArea2.getText()));
          outputArea.append("Setting discount successfully. Now the discount for "
              + String.valueOf(product.getName()) + " is " + 
              String.valueOf(textArea2.getText()) + " from " + 
              String.valueOf(textArea3.getText()) + " to " + 
              String.valueOf(textArea4.getText()) + ". ");
          setChanged();
          notifyObservers("Setting discount successfully. Now the discount for "
                  + String.valueOf(product.getName()) + " is " + 
                  String.valueOf(textArea2.getText()) + " from " + 
                  String.valueOf(textArea3.getText()) + " to " + 
                  String.valueOf(textArea4.getText()) + ". ");
          cleatTextArea();
        }
    }
  }

  private void cleatTextArea() {
    textArea1.setText("");
    textArea2.setText("");
    textArea3.setText("");
    textArea4.setText("");
  }
}


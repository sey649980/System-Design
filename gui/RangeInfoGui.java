package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class RangeInfoGui extends SmallGui implements ActionListener {
  private RevenueAndCostGui revenueAndCostGui;
  private JLabel startDate;
  private JTextField startInput;
  private JLabel endDate;
  private JTextField endInput;
  private JTextArea result;
  private JButton cost;
  private JButton revenue;
  private JPanel panel,panel1,panel2,panel3;
  private JFrame frame;
  public RangeInfoGui(RevenueAndCostGui revenueAndCostGui){
    this.revenueAndCostGui = revenueAndCostGui;
    addObserver(revenueAndCostGui.getSimulation());
    frame = new JFrame("Range Info");
    panel = new JPanel();
    panel1 = new JPanel();
    panel2 = new JPanel();
    panel3 = new JPanel();
    // ques: not a good experience.  One way is that delete all infor and only remain upc and the other things should in field..
    startDate = new JLabel("Please type the start date as format yyyyMMdd.");
    endDate = new JLabel("Please type the end date as format yyyyMMdd");
    startInput = new JTextField(10);
    endInput = new JTextField(10);
    cost = new JButton("Range Cost");
    cost.addActionListener(this);
    revenue = new JButton("Range Revenue");
    revenue.addActionListener(this);
    result = new JTextArea(15, 30);
//    ok.addActionListener(new CloseListener());
    panel.add(startDate);
    panel.add(startInput);
    panel1.add(endDate);
    panel1.add(endInput);
    panel2.add(cost);
    panel2.add(revenue);
    panel.setLayout(new FlowLayout(0));
    panel1.setLayout(new FlowLayout(0));
    panel2.setLayout(new GridLayout(0,1));
    frame.add("Center",new JScrollPane(result, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    panel3.add("North",panel);
    panel3.add("South",panel1);
    panel3.setLayout(new GridLayout(0,1));
    frame.add("North",panel3);
    frame.add("East",panel2);
//    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    //setSize(500, 250);
    //ques: use?
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }
  //ques: the empty string would cause the program fails
  public void actionPerformed(ActionEvent e){
    int start = Integer.valueOf(startInput.getText());
    int end = Integer.valueOf(endInput.getText());
    if ((start > 99999999) || (end< start)) {
      WarningGui wg = new WarningGui();
    } else {
      switch (e.getActionCommand()) {
        case "Range Cost":
          String str1 = rangeCosts(start, end);
          result.setText(str1);
          setChanged();
          notifyObservers(str1);
          break;
        case "Range Revenue":
          String str2 = rangeRevenues(start, end);
          result.setText(str2);
          setChanged();
          notifyObservers(str2);
          break;
      }
    }
  }
  private String rangeRevenues(int startingDate, int endingDate){
    int date = revenueAndCostGui.getStore().getDate();
    Map<Integer, Double> oneDayRevenue = revenueAndCostGui.getStore().getOneDayRevenue();
    String result = "";
    if(endingDate > date) {
      endingDate = date;
    }
    if (oneDayRevenue.keySet().isEmpty()) {
      result =  "The revenue is 0";
    } else {
      for (int day : oneDayRevenue.keySet()) {
        if ((startingDate <= day) && (day <= endingDate)) {
          result += String.valueOf(day) + oneDayRevenue.get(day) + "\n";
        }
      }
    }
    return result;
  }

  public String rangeCosts(int startingDate, int endingDate){
    int date = revenueAndCostGui.getStore().getDate();
    Map<Integer, Double> oneDayCost = revenueAndCostGui.getStore().getOneDayCost();
    String result = "";
    if(endingDate > date) {
      endingDate = date;
    }
    if (oneDayCost.keySet().isEmpty()) {
      result =  "The cost is 0";
    } else {
      double total = 0;
      for (int day: oneDayCost.keySet()) {
        if ((startingDate <= day) && (day <= endingDate)) {
          result += String.valueOf(day) + "  " + oneDayCost.get(day) + "\n";
          total += oneDayCost.get(day);
        }
      }
      result += "Total cost is "+total;
    }
    return result;

  }

}

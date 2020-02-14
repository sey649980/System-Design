package gui;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import gorcery_store.Store;
import gorcery_store.StoreSimulation;
import gorcery_store.WorkerAccount;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * Created by sey64 on 8/3/2017.
 */
public class InitialFrame extends SmallGui implements ActionListener{
  private JPanel panel,panel1,panel2,panel3;
  private JLabel account;
  private JLabel password;
  private JTextField l1;
  private JPasswordField l2;
  private WorkerAccount workers;
  private StoreSimulation simulation;
  private Store store;
  private JFrame frame;
  // ques: change position
  //ques: wrong account.
  public InitialFrame(WorkerAccount acc, StoreSimulation simulation){
    frame = new JFrame("Log in");
    this.simulation = simulation;
    this.store = simulation.getStore();
    panel = new JPanel();
    panel1 = new JPanel();
    panel2 = new JPanel();
    panel3 = new JPanel();
    account = new JLabel("Account:    ");
    password = new JLabel("Password: ");
    JButton button = new JButton("Log in");
    JButton change = new JButton("Change the password");
    l1 = new JTextField(18);
    l2 = new JPasswordField(18);

    button.addActionListener(this);
    change.addActionListener(this);
    workers = acc;

    panel.add(account);
    panel.add(l1);
    panel1.add(password);
    panel1.add(l2);
    panel2.add(button);
    panel2.add(change);
    panel3.setLayout(new GridLayout(0,1));
    panel3.add(panel);
    panel3.add(panel1);
    panel3.add(panel2);
    panel.setLayout(new FlowLayout(1));
    panel1.setLayout(new FlowLayout(1));
    panel2.setLayout(new FlowLayout(1));
    frame.add(panel3);

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    //setSize(500, 250);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }

  @Override
  public void actionPerformed(ActionEvent e){

    String info = workers.logIn(l1.getText(), l2.getText());
    if (!(info.equals("We didn't recognize this worker account.") || info.equals("Wrong password."))) {
      switch (e.getActionCommand()) {
        case "Log in":
          switch (info) {
            case "Manager":
              ManagerGui managerGui = new ManagerGui(workers, simulation);
              break;
            case "Receiver":
              ReceiverGui receiverGui = new ReceiverGui(simulation);
              break;
            case "Reshelver":
              ReshelverGui reshelverGui = new ReshelverGui(simulation);
              break;
            case "Cashier":
              CashierGui cashierGui = new CashierGui(simulation);
              break;
          }
          simulation.getLogger().log(Level.INFO, "The " + info +
              " (" + l1.getText() + ") logged in.");
          l2.setText("");
          l1.setText("");

          break;
        case "Change the password":
          ChangePasswordGui cha = new ChangePasswordGui(this);
          simulation.getLogger().log(Level.INFO, "The " + info +
              " (" + l1.getText() + ") has changed the password.");
          l1.setText("");
          l2.setText("");
      }
    }else{
       WarningGui warning = new WarningGui();
       l1.setText("");
       l2.setText("");
    }
   }

   public WorkerAccount getWorkers() {
    return workers;
   }
   public String getL1() {
    return l1.getText();
   }
}


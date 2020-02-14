package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by sey64 on 8/5/2017.
 */
public class ChangePasswordGui extends SmallGui implements ActionListener {
  private JLabel ins;
  private JTextField password;
  private InitialFrame ini;
  private JFrame frame;
  public ChangePasswordGui(InitialFrame i){
    ini = i;
    frame = new JFrame("Change Password");
    JPanel p = new JPanel();
    ins = new JLabel("Please enter your new password: ");
    password = new JTextField(6);
    JButton ok = new JButton("Confirm");
    ok.addActionListener(this);
    p.add(ins);
    p.add(password);
    p.add(ok);
    p.setLayout(new FlowLayout(0));
    frame.add(p);
    //setSize(500, 100);
    frame.setVisible(true);
    frame.pack();
  }

  @Override
  public void actionPerformed(ActionEvent e){
    frame.dispose();
    ini.getWorkers().workers.get(ini.getL1()).password = password.getText();
  }


}

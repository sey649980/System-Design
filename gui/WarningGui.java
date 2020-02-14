package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by sey64 on 8/7/2017.
 */
public class WarningGui extends JFrame implements ActionListener{
  public WarningGui(){
    super("Warning!");
    JLabel label = new JLabel("Invalid input! Please re-enter it.");
    label.setFont(new Font("Rome", Font.PLAIN, 31));
    JButton ok = new JButton("OK");
    ok.setPreferredSize(new Dimension(100,20));
    ok.setFont(new Font("Rome", Font.PLAIN, 20));
    ok.addActionListener(this);
    JPanel buttonPanel = new JPanel();
    JPanel labelPanel = new JPanel();
    labelPanel.add(label);
    labelPanel.setLayout(new FlowLayout(1));
    buttonPanel.add(ok);
    buttonPanel.setLayout(new FlowLayout(1));
    add("North", labelPanel);
    add("South", buttonPanel);
    setVisible(true);
    //setSize(500, 100);
    pack();
  }

  @Override
  public void actionPerformed(ActionEvent e){
    dispose();
  }

}

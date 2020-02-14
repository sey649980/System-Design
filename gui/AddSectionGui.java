package gui;

import gorcery_store.Section;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Created by abelsang on 2017-08-05.
 */
public class AddSectionGui extends SmallGui implements ActionListener {
  private ManagerGui mg;
  private JLabel msg;
  private JTextField section;
  private JButton ok;
  private JPanel panel;
  private JFrame frame;
  public AddSectionGui(ManagerGui mg){
    this.mg = mg;
    addObserver(mg.getSimulation());
    frame = new JFrame("Add New Section");
    panel = new JPanel();
    msg = new JLabel("Please type the section you want to add");
    section = new JTextField(14);
    section.setEditable(true);
    ok = new JButton("OK");
    ok.addActionListener(this);
//    ok.addActionListener(new CloseListener());
    panel.add(msg);

    panel.add(new JScrollPane(section, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));   
    panel.add(ok);
    panel.setLayout(new FlowLayout());
    frame.add(panel);
//    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    //setSize(500, 250);
    //ques: use?
    frame.setResizable(false);
    frame.setVisible(true);
    frame.pack();
  }

  public void actionPerformed(ActionEvent e){
    String sectionName = section.getText();
    Section s = new Section(sectionName);
    mg.getStore().getContainer().put(sectionName,s);
    setChanged();
    notifyObservers("New section " + sectionName + " is added to Store.");
    frame.dispose();
  }
}

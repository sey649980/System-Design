package gorcery_store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import javax.xml.crypto.Data;

/**
 * DataManager contains methods to get data from file and store data to the file.
 */
public class DataManager {

  StoreSimulation simulation;

  public DataManager(StoreSimulation simul) throws IOException, ClassNotFoundException {
    simulation = simul;
    simulation.addDate(getLocalDate());
    File file = new File("./ProjectPhase1/Store.ser");
    if (file.exists()) {
      simulation.store = readData();
    } else {
      EventInstruction e = new EventInstruction(simulation);
      e.readEvent("./ProjectPhase1/Setup.txt");
//      File readable_file = new File("./ProjectPhase1/Store.csv");
//      readable_file.createNewFile();
    }
  }

  /**
   * Store the given object in the file which has the given file path.
   */
  //note: why PrintWriter?  better than outputStream
  //note: how do you convert it into csv
  void storeReadableData() throws FileNotFoundException {
    PrintWriter writer = new PrintWriter("./ProjectPhase1/Store.csv");
    for (String key : simulation.store.getProducts().keySet()) {
      Product product = simulation.store.getProducts().get(key);
      String section;
      section = product.getSection().getParent().getName() + "-" + product.getSection().getName();
      String line = product.getUpc() + "," + product.getName() + "," + product.getSale().getPrice()
          + "," + product.getQuantity() + "," + section + "," + product.getLocation() + "," + product.getThreshold();
      writer.println(line);
    }
    writer.close();
  }
  public void storeData(){
    try {
      FileOutputStream fileOut = new FileOutputStream("./ProjectPhase1/Store.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(simulation.store);
      out.close();
      fileOut.close();
    } catch (IOException i) {
      i.printStackTrace();
    }
  }

  /**
   * Read the Data in the file.
   *
   * @return the object read, if cannot return "NotFound".
   */
  Store readData() throws IOException, ClassNotFoundException{
      FileInputStream fileIn = new FileInputStream("./ProjectPhase1/Store.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      Object e = in.readObject();
      in.close();
      fileIn.close();
      return (Store) e;

  }

  /**
   * Return an integer which represents the local date in the format of "yyyymmdd".
   * 
   * @return the local date.
   */
  int getLocalDate() {
    int year = LocalDate.now().getYear();
    int month = LocalDate.now().getMonthValue();
    int day = LocalDate.now().getDayOfMonth();
    String monthStr = Integer.toString(month);
    String dayStr = Integer.toString(day);
    if (month < 10) {
      monthStr = "0" + month;
    }
    if (day < 10) {
      dayStr = "0" + day;
    }
    String dateStr = Integer.toString(year) + monthStr + dayStr;
    return Integer.valueOf(dateStr);
  }
}

package gorcery_store;


import gui.InitialFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Simulate and start the whole store program.
 */

public class StoreSimulation implements Observer{
  /** The store in simulation. */
  Store store;
  /** The date. */
  private int DATE;
  /** A logger for creating log.txt. */
  private static final Logger logger = Logger.getLogger(StoreSimulation.class.getName());
  /** A list of event. */
  List<Event> events;
  /**
   * The constructor for StoreSimulation.
   * Use an existing store to create log.txt.
   * @param store The store
   * @throws IOException IO EXCEPTION
   */
  public StoreSimulation(Store store) throws IOException {
    this.store = store;
    logger.setLevel(Level.ALL);
    FileHandler fileHandler = new FileHandler("./ProjectPhase1/log.txt");
    logger.addHandler(fileHandler);
    SimpleFormatter formatter = new SimpleFormatter();
    fileHandler.setFormatter(formatter);
    events = new ArrayList<>();
  }
  public void update(Observable o, Object obj){
    logger.log(Level.INFO, (String) obj);
  }
  /**
   * Get my logger.
   * @return my logger
   */
   public Logger getLogger(){
    return logger;
  }
  /**
   * Return the date
   * @return the date.
   */
  public int getDATE(){
     return DATE;
  }

  void addDate(int date) {
    DATE = date;
  }
  public Store getStore() {
    return this.store;
  }

  /**
   * The main method for StoreSimulation.
   * Start the program.
   * @param args arguments
   * @throws IOException may throws IOException.
   * @throws ClassNotFoundException may throws ClassNotFoundException.
   */
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Store s = new Store();
    StoreSimulation simulation = new StoreSimulation(s);
    DataManager dataMa = new DataManager(simulation);

    //
    InitialFrame x = new InitialFrame(simulation.getStore().acc, simulation);

    // Comment out to activate storing.
    //dataMa.storeData();
//    d.storeReadableData();

  }
  
}




package gorcery_store;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by sey64 on 7/31/2017.
 */
public class EventInstruction {
  private StoreSimulation simulation;
//  /** Command order. */
//  private static final String ORDER = "Order";
//  /** Command reorder. */
//  private static final String REORDER = "Reorder";
//  /** Command sale. */
//  private static final String SALE = "Sale";
//  /** Command discount. */
//  private static final String DISCOUNT = "Discount";
//  /** Command received. */
//  private static final String RECEIVED = "Received";
  /*** Command SetUp. */
  private static final String SETUP = "SetUp";
  /*** Command SetUp. */
  private static final String SETUPWORKER = "SetUpWorker";
//  /** Command login. */
//  private static final String LOGIN = "LogIn";
//  /** Command logout. */
//  private static final String LOGOUT = "LogOut";

  public EventInstruction(StoreSimulation simul){
    simulation = simul;
  }


  public void setUpOrOrder(String[] record) {
    Double o3 = Double.valueOf(record[3]);
    int o4 = Integer.valueOf(record[4]);
    String[] secString = record[5].split("-");
    Section section = simulation.store.getSection(secString[0]);
    SubSection subSection;
    Product product;
    if (!section.getContainer().containsKey(secString[1])) {
      subSection = new SubSection(secString[1]);
      simulation.store.addSubSection(subSection,section);
    } else {
      subSection = section.getContainer().get(secString[1]);
    }
    product = new Product(record[1], record[2], o4, Integer.valueOf(record[7]), subSection,
        record[6], o3, Double.valueOf(record[8]), simulation.getDATE());
    product.getOrder().setUpc(product.getName());
    product.getOrder().setQuantity(product.getQuantity());
    product.getOrder().addHistory(o3, o4);
    subSection.addProduct(product);
    simulation.store.getProducts().put(product.getUpc(), product);
    if (record[0].equals("Order")) {
      simulation.store.getOrderInOneDay().add(product.getOrder());
      product.setStatus("Order");
    } else {
      product.setStatus("SetUp");
      product.getOrder().companyName = record[9];
    }
    simulation.getLogger().log(Level.INFO, product.toString());

  }

  public void setUpWorker(String[] record){
    Worker a = new Worker(record[1], record[2], record[3]);
    simulation.store.acc.recruit(a);
    simulation.getLogger().log(Level.INFO, "Set up worker " + a.workingNum +
        " with the occupation of " + a.occupation);
  }

//  public void reOrder(String[] record) {
//    Double rr2 = Double.valueOf(record[2]);
//    Product product4 = simulation.store.getProducts().get(record[1]);
//    if (record.length == 4) {
//      product4.reOrder(rr2, simulation.getDATE());
//    } else {
//      product4.reOrder(rr2, Integer.valueOf(record[4]), simulation.getDATE());
//    }
//    product4.getOrder().setDate(simulation.getDATE());
//    product4.setStatus("Reorder");
//    ArrayList list4 = new ArrayList();
//    list4.add(product4.getName()+" ("+ product4.getUpc() + ")");
//    list4.add(rr2);
//    list4.add(product4.getOrder().getQuantity());
//    simulation.store.getPendingOrder().add(list4);
//    simulation.store.getOrderInOneDay().add(product4.getOrder());
//    simulation.getLogger().log(Level.INFO, product4.toString() + " from " + record[3]
//        + " by " + product4.getOrder().getQuantity());
//  }
//
//  public void received(String[] record) {
//    Product product2 = simulation.store.getProducts().get(record[1]);
//    product2.addQuantity(product2.getOrder().getQuantity());
//    product2.setStatus("Received");
//    ArrayList list2 = new ArrayList();
//    list2.add(product2.getName()+" ("+ product2.getUpc() + ")");
//    list2.add(product2.getOrder().getPrice());
//    list2.add(product2.getOrder().getQuantity());
//    simulation.store.getPendingOrder().remove(list2);
//    simulation.getLogger().log(Level.INFO, product2.toString());
//  }
//
//  public void sell(String[] record) {
//    Double s3 = Double.valueOf(record[2]);
//    int s4 = Integer.valueOf(record[3]);
//    Product product3 = simulation.store.getProducts().get(record[1]);
//    if (product3.getQuantity() < s4) {
//      s4 = product3.getQuantity();
//    }
//    product3.setQuantity(product3.getQuantity() - s4);
//    Sale newSale = new Sale(s3, simulation.getDATE(), s4, product3.getName(),
//        product3.getSale().getStartDate(), product3.getSale().getEndDate(),
//        product3.getSale().getDiscount());
//    newSale.setHistory(product3.getSale().getHistory());
//    if (s3 != product3.getSale().getPrice()) {
//      newSale.addPrice(s3);
//    }
//    product3.setStatus("Sale");
//    product3.setSale(newSale);
//    simulation.store.getSaleInOneDay().add(product3.getSale());
//    simulation.getLogger().log(Level.INFO, product3.toString());
//    if (product3.getQuantity() < product3.getThreshold()) {
//      simulation.getLogger().log(Level.INFO,
//          "The inventory ( quantity: " + product3.getQuantity() + ") of " + product3.getName()
//              + " is below the threshold " + product3.getThreshold() + ". Need reorder");
//    }
//  }
//
//  public void discount(String[] record) {
//    Product p = simulation.store.getProducts().get(record[1]);
//    p.getSale().setStartDate(record[3]);
//    p.getSale().setEndDate(record[4]);
//    p.getSale().setDiscount(Double.valueOf(record[2]));
//    String result = "Product " + p.getName() + " starts to discount to "
//        + p.getSale().getPrice() + " from date " + p.getSale().getStartDate()
//        + " to " + p.getSale().getEndDate();
//    simulation.getLogger().log(Level.INFO, result);
//  }

  /**
   * Read events from event.txt and run the program.
   * @param filePath the path of the file event.txt.
   * @throws IOException may throws IOException
   */
  void readEvent(String filePath) throws IOException {
    Scanner scanner = new Scanner(new FileInputStream(filePath));
    while (scanner.hasNext()) {
      String line = scanner.nextLine();
      String[] record = line.split(", ");
      Event event = new Event(record[0], record);
      simulation.events.add(event);
    }
    scanner.close();
    simulate(simulation.events);
  }

  /**
   * Handle the event in events list.
   */
  public void handleEvent(Event event) throws IOException {
    switch(event.getType()) {
//      case LOGIN:
//        simulation.getLogger().log(Level.INFO, "Cashier " + event.getContent()[1] + " logged in.");
//        break;
      case SETUP: //case ORDER:
        setUpOrOrder(event.getContent());
        break;
      case SETUPWORKER:
        setUpWorker(event.getContent());
        break;
//      case REORDER:
//        reOrder(event.getContent());
//        break;
//      case RECEIVED:
//        received(event.getContent());
//        break;
//      case SALE:
//        sell(event.getContent());
//        break;
//      case DISCOUNT:
//        discount(event.getContent());
//        break;
//      case LOGOUT:
//        simulation.getLogger().log(Level.INFO, "The Cashier logged out.");
//        break;
    }
  }

  public void simulate(List<Event> events) throws IOException {
    for(Event e : events) {
      handleEvent(e);
    }
  }

}

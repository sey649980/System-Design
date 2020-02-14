package gorcery_store;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A store has 9 sections. Each section has lots of different foods.
 */
public class Store implements Serializable {

  /**
   * The collection of all kinds of the product.
   */
  private HashMap<String, Product> products = new HashMap<>();
  ;
  /**
   * All sales in a day.
   */
  private static ArrayList<Sale> saleInOneDay = new ArrayList<Sale>();
  /**
   * All orders in a day.
   */
  private static ArrayList<Order> orderInOneDay = new ArrayList<Order>();
  /**
   * Pending orders
   */
  private ArrayList<ArrayList<String>> pendingOrder = new ArrayList<>();
  /**
   * Purchased pending orders.
   */
  private ArrayList<ArrayList<String>> pendingReceived = new ArrayList<>();
  //ques: unable to add new section
  /**
   * The map whose key is the section's upc, and the value is the section.
   */
  private Map<String, Section> container;
  private Map<Integer, Double> oneDayRevenue;
  private Map<Integer, Double> oneDayCost;
  private int date;
  public WorkerAccount acc = new WorkerAccount();

  public Store() {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    date = Integer.valueOf(dateFormat.format(new Date()));
    container = new HashMap<>();
    container.put("Produce", new Section("Produce"));
    container.put("Dairy", new Section("Dairy"));
    container.put("Meat", new Section("Meat"));
    container.put("PackagedProduct", new Section("PackagedProduct"));
    container.put("FrozenFood", new Section("FrozenFood"));
    container.put("CleaningSupplies", new Section("CleaningSupplies"));
    container.put("PetFood", new Section("PetFood"));
    container.put("KitchenWares", new Section("KitchenWares"));
    container.put("SeasonalProduct", new Section("SeasonalProduct"));
    oneDayCost = new HashMap<>();
    oneDayRevenue = new HashMap<>();
  }

  /**
   * Get my section.
   *
   * @param name the upc of a given section
   * @return section map
   */
  public Section getSection(String name) {
    return container.get(name);

  }

  /**
   * Add the subsection in different sections
   *
   * @param sub a given subsection
   * @param section a given upc of a section
   */
  public void addSubSection(SubSection sub, Section section) {
    sub.setParent(section);
    section.getContainer().put(sub.getName(), sub);
  }

  /**
   * Get my products.
   *
   * @return my products
   */
  public HashMap<String, Product> getProducts() {
    return products;
  }

  /**
   * Create a orderList based on orderInOneDay.
   *
   * @return a nice looking version of orderInOneDay
   */
  String orderListString() {
    if (getPendingOrder().size() == 0) {
      return "This is no pending order. \nAll pending orders have been purchased.";
    }
    String result = "";
    for (ArrayList<String> pendingOrder : getPendingOrder()) {
      int upcLength = pendingOrder.get(0).length();
      String upc = pendingOrder.get(0).substring(upcLength - 13, upcLength - 1);
      Product product = getProducts().get(upc);
      result = result + "Product: " + pendingOrder.get(0) + "; Price: " + pendingOrder.get(1) + "; Quantity: " + pendingOrder.get(2)
          + "; from " + product.getOrder().companyName
          + "\n";
    }
    return result;
  }

  public String getPendingReceivedItems() {
    if (pendingReceived.size() == 0) {
      return "This are no pending received items. \nAll pending items have been received.";
    }
    String result = "";
    for (ArrayList<String> pendingReci : pendingReceived) {
      result = result + "Product: " + pendingReci.get(0) + "; Price: " + pendingReci.get(1) + "; Quantity: " + pendingReci.get(2)
          + "\n";
    }
    return result;
  }


  /**
   * Get my saleInOneDay.
   *
   * @return my saleInOneDay
   */
  public ArrayList<Sale> getSaleInOneDay() {
    return saleInOneDay;
  }

  /**
   * Get my orderInOneDay.
   *
   * @return my orderInOneDay
   */
  public ArrayList<Order> getOrderInOneDay() {
    return orderInOneDay;
  }

  public String getOrderListString() {
    return orderListString();
  }
  public ArrayList<ArrayList<String>> getPendingReceived() {
    return pendingReceived;
  }
  /**
   * Get my PendingOrder.
   *
   * @return my PendingOrder
   */
  public ArrayList<ArrayList<String>> getPendingOrder() {
    return pendingOrder;
  }

  public Map<String, Section> getContainer() {
    return this.container;
  }

  /**
   * Counting revenue for my store.
   *
   * @return the revenue of my store
   */
  public double revenue() {
    double revenue = 0;
    for (Sale getSIOD : getSaleInOneDay()) {
      revenue = revenue + getSIOD.getPrice() * getSIOD.getQuantity();
    }
    oneDayRevenue.put(date, revenue);
    return revenue;
  }

  /**
   * Counting cost of my store.
   *
   * @return the cost of my store
   */
  public double cost() {
    double cost = 0;
    for (Order getOIOD : getOrderInOneDay()) {
      cost = cost + getOIOD.getPrice() * getOIOD.getQuantity();
    }
    oneDayCost.put(date, cost);
    return cost;
  }

  /**
   * Counting profit of my store.
   *
   * @return the profit of my store
   */
  public double profit() {
    return revenue() - cost();
  }

  //for one product.
//  public double productCost(String upc) {
//    double cost = 0;
//    for (Order p : getOrderInOneDay()) {
//      if (p.upc.equals(getProducts().get(upc).getUpc())) {
//        cost = cost + p.getPrice() * p.getQuantity();
//      }
//    }
//    return cost;
//  }
//
//  public double productRevenue(String upc) {
//    double revenue = 0;
//    for (Sale p : saleInOneDay) {
//      if (p.upc.equals(products.get(upc).getUpc())) {
//        revenue = revenue + p.getPrice() * p.getQuantity();
//      }
//    }
//    return revenue;
//  }

//  public double sectionCost(String section) {
//    double cost = 0;
//    for (Order p : orderInOneDay) {
//      Product product = products.get(p.getUpc());
//      if (product.getSection().getParent().getUpc().equals(section)) {
//        cost = cost + p.getPrice() * product.getQuantity();
//      }
//    }
//    return cost;
//  }
//  public double sectionRevenue(String section) {
//    double revenue = 0;
//    for (Sale p : saleInOneDay) {
//      Product product = products.get(p.getUpc());
//      if (product.getSection().getParent().getUpc().equals(section)) {
//        revenue = revenue + p.getPrice() * product.getQuantity();
//      }
//    }
//    return revenue;
//  }

//
//  public double productRevenue(String section){
//
//  }

  //date format: yyyymmdd
  // Idea is that we store every day's cost and revenue into a csv (Notice that if someday is not in the file, its cost and revenue should be 0
  // instead of causing an error.)
//  public String rangeCosts(int startingDate, int endingDate){
//    String result = "";
//    if(endingDate > date) {
//      endingDate = date;
//    }
//    if (oneDayCost.keySet().isEmpty()) {
//      result =  "The cost is 0";
//    } else {
//      for (int date: oneDayCost.keySet()) {
//        if ((startingDate <= date) && (date <= endingDate)) {
//          result += String.valueOf(date) + oneDayCost.get(date) + "\n";
//        }
//      }
//    }
//    return result;
//
//  }
//
//  public String rangeRevenues(int startingDate, int endingDate){
//    String result = "";
//    if(endingDate > date) {
//      endingDate = date;
//    }
//    if (oneDayRevenue.keySet().isEmpty()) {
//      result =  "The revenue is 0";
//    } else {
//      for (int date : oneDayRevenue.keySet()) {
//        if ((startingDate <= date) && (date <= endingDate)) {
//          result += String.valueOf(date) + oneDayRevenue.get(date) + "\n";
//        }
//      }
//    }
//    return result;
//  }

  public Map<Integer, Double> getOneDayCost() {
    return oneDayCost;
  }
  public Map<Integer, Double> getOneDayRevenue() {
    return oneDayRevenue;
  }
  public int getDate() {
    return date;
  }

}

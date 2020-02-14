package gui;

import java.util.Observable;

/**
 * Created by sey64 on 8/7/2017.
 */
public class SmallGui extends Observable {
  
  public boolean checkUpc(String upc){
    return (! upc.equals( null)) && upc.length() == 12 && upc.matches("[1234567890]*");
  }
  public boolean checkEmpty(String m){
    //Check if it is space
    return ! m.equals( "");
  }
  public boolean checkDates(String s, String e){
    return s.length() == 8 && s.matches("[0-9]*") && e.length() == 8 &&
        e.matches("[0-9]*") && Double.valueOf(s) <= Double.valueOf(e);
  }


}

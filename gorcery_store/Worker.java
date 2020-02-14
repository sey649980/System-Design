package gorcery_store;

import java.io.Serializable;

/**
 * Created by sey64 on 8/3/2017.
 */
public class Worker implements Serializable{
  public String workingNum;
  public String password;
  public String occupation;

  public Worker(String num, String code, String job) {
    workingNum = num;
    password = code;
    occupation = job;
  }
}

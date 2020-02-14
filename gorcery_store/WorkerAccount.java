package gorcery_store;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sey64 on 8/3/2017.
 */
public class WorkerAccount implements Serializable{
  public HashMap<String, Worker> workers = new HashMap<>();
  public WorkerAccount() {
  }
  public WorkerAccount(ArrayList<Worker> lists) {
    for (Worker worker : lists) {
      workers.put(worker.workingNum, worker);
    }
  }

  public String logIn(String acoountNum, String code){
    if (!workers.containsKey(acoountNum)) {
      System.out.println("a");
      return "We didn't recognize this worker account.";
    } else {
        Worker worker = workers.get(acoountNum);
        if (!code.equals(worker.password)) {
          System.out.println("b");
          return "Wrong password.";
        } else {
          return worker.occupation;
        }
    }

  }

  public void recruit(Worker newMan){
    if (!workers.containsKey(newMan.workingNum) ){
      workers.put(newMan.workingNum, newMan);
    } else {
      // do something
    }
  }

  public void dismiss(String workerNum){
    if (workers.containsKey(workerNum) ){
      workers.remove(workerNum);
    } else {
      // do something
    }
  }


}

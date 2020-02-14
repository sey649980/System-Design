package gorcery_store;

import java.io.ObjectInputStream;

/**
 * Created by abelsang on 2017-07-29.
 */
public class Event {
  private String type;
  private String[] content;

  public Event(String type, String[] content) {
    this.type = type;
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public String[] getContent() {
    return this.content;
  }

}

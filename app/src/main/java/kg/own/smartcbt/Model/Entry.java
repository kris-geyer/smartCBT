package kg.own.smartcbt.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Entry implements Serializable {

     public String time;
     public ArrayList<Thought> thoughts;
     public ArrayList<Emotion> emotion;
     public ArrayList<Behaviour> behaviour;

     public Entry(){
         time = DateFormat.getDateTimeInstance().format(new Date());
         thoughts = new ArrayList<>();
         emotion = new ArrayList<>();
         behaviour = new ArrayList<>();
     }

}

package kg.own.smartcbt.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Thought implements Serializable {

    public ArrayList<String> thoughts;

    public void addThought(String thought){
        thoughts.add(thought);
    }

    public Thought(){
        thoughts = new ArrayList<>();
    }
}

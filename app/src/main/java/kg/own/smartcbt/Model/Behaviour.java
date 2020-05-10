package kg.own.smartcbt.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Behaviour implements Serializable {

    final public String app;
    public ArrayList<String> behaviours;

    public Behaviour() {
        this.app = "";
        this.behaviours = new ArrayList<>();
    }

    public  void addBehaviour (String behaviour){
        this.behaviours.add(behaviour);
    }
}

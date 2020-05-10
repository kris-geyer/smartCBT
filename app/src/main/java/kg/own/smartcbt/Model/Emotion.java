package kg.own.smartcbt.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Emotion implements Serializable {

    public ArrayList<String> emotions;

    public Emotion(){
        emotions = new ArrayList<>();
    }

    public void addEmotion(String emotion){
        emotions.add(emotion);
    }
}

package kg.own.smartcbt.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Day implements Serializable {

    public String date;
    private ArrayList<Entry> entries;
    public LinkedHashMap<String, Integer> emotions;

    public Day(Entry entry) {
        entries = new ArrayList<>();
        entries.add(entry);
        emotions = new LinkedHashMap<>();
        date = String.valueOf(LocalDate.now());
        establishEmotions();
    }

    public void addToEntry(Entry entry){
        entries.add(entry);
        establishEmotions();
    }

    public void establishEmotions (){
        LinkedHashMap<String, Integer> newEmotions = new LinkedHashMap<>();

        for (int i = 0; i <entries.size(); i++){
            for (int j = 0; j < entries.get(i).emotion.size(); j++){
                for (int k = 0; k < entries.get(i).emotion.get(j).emotions.size(); k++){
                    String emotion = entries.get(i).emotion.get(j).emotions.get(k);
                    if (newEmotions.containsKey(emotion)){
                        newEmotions.put(emotion, newEmotions.get(emotion) + 1);
                    }else{
                        newEmotions.put(emotion, 1);
                    }
                }
            }
        }
        this.emotions = newEmotions;
    }

    public ArrayList<Entry> getEntries(){
        return entries;
    }
}

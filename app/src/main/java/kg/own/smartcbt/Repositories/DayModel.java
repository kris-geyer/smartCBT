package kg.own.smartcbt.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import kg.own.smartcbt.Model.Day;
import timber.log.Timber;


class DayModel {

    private final static Gson gson = new Gson();
    private final Context context;
    private final SharedPreferences sharedPreferences;


    DayModel(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("model", Context.MODE_PRIVATE);
    }

    int getNumEntries(){
        return sharedPreferences.getInt("numEntries", 0);
    }

    void addDay(Day day){
        Timber.i("addDay called");
        sharedPreferences.edit().putString("Day" + sharedPreferences.getInt("numEntries", 0), gson.toJson(day))
                .putInt("numEntries",sharedPreferences.getInt("numEntries", 0) + 1).apply();
    }

    ArrayList<Day> retrieveEntries(){
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < sharedPreferences.getInt("numEntries", 0); i++){
            if(!sharedPreferences.getString("Day" + i, "null").equals("null")){
                days.add(gson.fromJson(sharedPreferences.getString("Day" + i, "null"), Day.class));
            }
        }
        return days;
    }

    void overwriteRecords(ArrayList<Day> days){
        Timber.i("overwriteRecords called");
        sharedPreferences.edit().clear().apply();
        for(Day day: days){
            addDay(day);
        }
    }

}

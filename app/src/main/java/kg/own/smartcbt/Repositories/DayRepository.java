package kg.own.smartcbt.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kg.own.smartcbt.Model.Behaviour;
import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.Model.Entry;
import timber.log.Timber;

public class DayRepository {

    private static DayRepository instance;
    private DayModel dayModel;

    public static DayRepository getInstance() {
        if(instance == null){
            instance = new DayRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Day>> getDays() {
        /*
        if(dayModel.getNumEntries() == 0){
            initializeDays();
        }
         */
        MutableLiveData<List<Day>> days = new MutableLiveData<>();
        days.setValue(dayModel.retrieveEntries());
        return days;
    }

    public void setUpSharedPreferences(Context context){
        dayModel = new DayModel(context);
    }


    public void addEntry(Entry entry) {
        ArrayList<Day> days = dayModel.retrieveEntries();

        if (PastDayToAddRecordTo(entry, days)){
            Timber.i("Entry added - past day");
        }else{
            Day newDay = new Day(entry);
            dayModel.addDay(newDay);
            Timber.i("Entry added - new day");
        }
    }

    private boolean PastDayToAddRecordTo(Entry entry, ArrayList<Day> days){
        final LocalDate now = LocalDate.now();
        for(Day day: days){
            if(day.date.equals(String.valueOf(now))){
                day.addToEntry(entry);
                dayModel.overwriteRecords(days);
                return true;
            }
        }
        return false;
    }


}

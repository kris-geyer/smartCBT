package kg.own.smartcbt.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kg.own.smartcbt.Model.AppData;
import kg.own.smartcbt.Model.Behaviour;
import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.Model.Emotion;
import kg.own.smartcbt.Model.Entry;
import kg.own.smartcbt.Model.Thought;
import kg.own.smartcbt.Model.UsageRecord;
import kg.own.smartcbt.Repositories.DayRepository;
import timber.log.Timber;

public class MakeEntryViewModel extends ViewModel implements RetrieveUsageInterface {

    private MutableLiveData<List<Day>> days;
    private DayRepository dayRepository;
    private MutableLiveData<List<AppData>> appData;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public void init(Context context) {
        if (days != null){
            return;
        }
        dayRepository = DayRepository.getInstance();
        dayRepository.setUpSharedPreferences(context);
        days = dayRepository.getDays();
        appData = new MutableLiveData<>();
    }

    public void setDays(ArrayList<Emotion> emotions, ArrayList<Thought> thoughts, ArrayList<Behaviour> behaviours){
        isUpdating.setValue(true);
        Entry entry = new Entry();
        entry.emotion = emotions;
        entry.thoughts = thoughts;
        entry.behaviour = behaviours;
        dayRepository.addEntry(entry);
    }

    public LiveData <List<AppData>> getAppData(){
        return appData;
    }

    public void retrieveAppUsage(int hour, int minute, Context context) {
        RetrieveUsageRecords retrieveUsageRecords = new RetrieveUsageRecords(hour, minute, context, this);
        retrieveUsageRecords.execute();
    }

    @Override
    public void recordsGathered(LinkedList<UsageRecord> toReturn) {
        Timber.i("Size of toReturn: %s", toReturn.size());

        LinkedList<UsageRecord> cleanData = cleanData(toReturn);
        Timber.i("Size of toReturn: %s", cleanData.size());
        Timber.i("Starting \n to return cleaned data \n");
        for (UsageRecord usageRecord: cleanData){
            Timber.i("app %s; event %s, time %s" , usageRecord.app, usageRecord.event, usageRecord.UNIXTime);
        }

        List<AppData> AllAppData = establishAppData(cleanData);


        List<AppData> forUser = new ArrayList<>();
        Timber.i("App data size: %s", AllAppData.size());
        for (AppData data: AllAppData){
            if(data.duration > 60){
                forUser.add(data);
            }
            Timber.i("App - %s; Duration - %s", data.AppName, data.duration);
        }

        appData.postValue(forUser);
    }

    private LinkedList<UsageRecord> cleanData(LinkedList<UsageRecord> toReturn) {
        LinkedList<UsageRecord> cleanData = new LinkedList<>();

        for (UsageRecord usageRecord: toReturn){
            if (usageRecord.event == 2 || usageRecord.event == 1 || usageRecord.event == 7){
                cleanData.add(usageRecord);
            }
        }
        return cleanData;
    }

    private List<AppData> establishAppData(LinkedList<UsageRecord> cleanData) {
        List<AppData> appData = new ArrayList<>();
        long startEvent = 0;
        for (UsageRecord record: cleanData){
            if (record.event!= 2){
                startEvent = record.UNIXTime;
            }else if (startEvent!= 0){
                final int duration = (int) (record.UNIXTime - startEvent)/1000;
                appendToAppData(record, appData, duration);
            }
        }

        return appData;
    }

    private void appendToAppData(UsageRecord record, List<AppData> appData, int duration) {
        for(AppData data: appData){
            if(record.app.equals(data.AppName)){
                data.duration+= duration;
                return;
            }
        }
        appData.add(new AppData(record.app, duration));
    }

}

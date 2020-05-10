package kg.own.smartcbt.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.Repositories.DayRepository;

public class SeeEntriesViewModel extends ViewModel {

    private MutableLiveData<List<Day>> days;
    private DayRepository dayRepository;

    public void init(Context context) {
        if (days != null){
            return;
        }
        dayRepository = DayRepository.getInstance();
        dayRepository.setUpSharedPreferences(context);
        days = dayRepository.getDays();
    }

    public LiveData<List<Day>> getDays(){
        return days;}
}

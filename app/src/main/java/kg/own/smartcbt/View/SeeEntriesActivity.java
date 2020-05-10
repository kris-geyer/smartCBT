package kg.own.smartcbt.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;



import kg.own.smartcbt.Model.Behaviour;
import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.Model.Entry;
import kg.own.smartcbt.R;
import kg.own.smartcbt.ViewModel.SeeEntriesViewModel;
import timber.log.Timber;

public class SeeEntriesActivity extends AppCompatActivity {

    private static SeeEntriesViewModel seeEntriesViewModel;
    private EntryExpandableList entryExpandableList;

    private List<Day> dataForList;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_entries);
        dataForList = new ArrayList<>();
        initializeUI();
        Timber.i("Initializing the view model");
        initializeViewModel();
    }

    private void initializeUI() {
        expandableListView = findViewById(R.id.expandableListView);
        entryExpandableList = new EntryExpandableList(this, dataForList);
        expandableListView.setAdapter(entryExpandableList);
        expandableListView.setOnChildClickListener(onChildClickListener);
    }

    private void initializeViewModel() {
        seeEntriesViewModel = ViewModelProviders.of(this).get(SeeEntriesViewModel.class);
        seeEntriesViewModel.init(this);
        seeEntriesViewModel.getDays().observe(this, new Observer<List<Day>>() {
            @Override
            public void onChanged(List<Day> days) {
                dataForList.addAll(days);

                entryExpandableList.notifyDataSetChanged();
                expandableListView.setOnChildClickListener(onChildClickListener);
                Timber.i("Size of data for list: " + dataForList.size());

                Timber.i("Days changed. Days size: %s", days.size());
                for (Day day: days){
                    Timber.i("Day:  %s", String.valueOf(day.date));
                    for (Entry entry: day.getEntries()){
                        Timber.i("Time: " + entry.time);
                        for(Behaviour behaviour: entry.behaviour){
                            Timber.i("Entry: behaviour" + behaviour.app);
                        }
                    }
                }
            }
        });
    }

    OnChildClickListener onChildClickListener = new OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Timber.i("Onchildclicked called");
            reviewRecords(groupPosition);
            return false;
        }
    };

    private void reviewRecords(int groupPosition) {
        Intent intent = new Intent(this, ReviewRecords.class);
        intent.putExtra("dayObject",dataForList.get(groupPosition) );
        startActivity(intent);

    }


    void makeToast(String message){
        Toast.makeText(this, ""+ message, Toast.LENGTH_SHORT).show();
    }

}

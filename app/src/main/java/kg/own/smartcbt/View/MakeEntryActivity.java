package kg.own.smartcbt.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.R;
import kg.own.smartcbt.Model.AppData;
import kg.own.smartcbt.Model.Behaviour;
import kg.own.smartcbt.Model.Emotion;
import kg.own.smartcbt.ViewModel.MakeEntryViewModel;
import kg.own.smartcbt.Model.Thought;
import timber.log.Timber;

public class MakeEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private static MakeEntryViewModel makeEntryViewModel;
    private static final String[] setEmotions = {"happy", "sad", "angry", "disgusted", "fearful"};
    private Emotion emotion;
    private Thought thought;
    private Behaviour behaviour;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_entry);
        Log.i("new activity","Make Entry Activity");
        initializeComponents();
        initializeUI();
        initializeViewModel();
    }

    private void initializeComponents() {
        emotion = new Emotion();
        thought = new Thought();
        behaviour = new Behaviour();
    }

    private void initializeUI() {
        findViewById(R.id.btnCommitEntry).setOnClickListener(this);
    }

    private void initializeViewModel() {
        makeEntryViewModel = ViewModelProviders.of(this).get(MakeEntryViewModel.class);
        makeEntryViewModel.init(this);

        makeEntryViewModel.getAppData().observe(this, new Observer<List<AppData>>() {
            @Override
            public void onChanged(List<AppData> appData) {
                Timber.i("App Data changed");
                reportEmotion(appData);
            }
        });
    }

    private void reportEmotion(final List<AppData> appData){

        final boolean[] emotionsChecker = {false, false, false, false, false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What emotions are you feeling?")
                .setMultiChoiceItems(setEmotions, emotionsChecker, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        emotionsChecker[which] = isChecked;
                    }
                }).setPositiveButton("next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < emotionsChecker.length; i++){
                    if(emotionsChecker[i]){
                        emotion.emotions.add(setEmotions[i]);
                    }
                }
                if(emotion.emotions.size() == 0){
                    emotion.emotions.add("none");
                }

                reportThoughts(appData);
            }
        }).create().show();

    }

    private void reportThoughts(final List<AppData> appData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Report any thoughts that you were having");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        builder.setView(input);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                thought.thoughts.add(input.getText()+"");
                reportApps(appData);
            }
        }).create().show();
    }

    private void reportApps(List<AppData> appData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] options = new String[appData.size()];
        final boolean[] results = new boolean[appData.size()];
        for (int i = 0; i < appData.size(); i++){
            options[i] = appData.get(i).AppName;
            results[i] = false;
        }

        builder.setMultiChoiceItems(options, results, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                results[which] = isChecked;
            }
        }).setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < options.length; i++){
                    if(results[i]){
                        behaviour.addBehaviour(options[i]);
                        Timber.i("App: %s was chosen", options[i]);
                    }
                }
                commitEntry();
                initializeComponents();
            }
        })
        .create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCommitEntry:

                generateAlertDialog(3);
                //commitEntry();
                break;
        }
    }

    private void generateAlertDialog(int messageNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Entry");
        builder.setMessage("Please document when you felt these significant emotions and thoughts");
        switch (messageNumber){
            case 0:
                builder.setMessage("Entry add");
                break;
            case 3:
                final LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View view = layoutInflater.inflate(R.layout.alert_behaviour,null);
                builder.setMessage("Add smartphone record?")
                        .setView(view)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){

                                TimePicker timePicker = view.findViewById(R.id.timePicker);
                                final int hour =  timePicker.getHour();
                                final int minute = timePicker.getMinute();

                                Timber.i("Time: %s:%s",  hour , minute);
                                startReviewingUsage(hour, minute);
                            }
                        });

                break;
        }


        builder.create().show();
    }

    private void startReviewingUsage(int hour, int minute) {
        makeEntryViewModel.retrieveAppUsage(hour, minute, this);
    }

    private void commitEntry() {
        ArrayList <Emotion> emotions = new ArrayList<>();
        ArrayList <Thought> thoughts = new ArrayList<>();
        ArrayList <Behaviour> behaviours = new ArrayList<>();
        emotions.add(emotion);
        thoughts.add(thought);
        behaviours.add(behaviour);

        makeEntryViewModel.setDays(emotions, thoughts, behaviours);
    }
}

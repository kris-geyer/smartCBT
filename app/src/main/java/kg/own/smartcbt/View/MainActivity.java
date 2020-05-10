package kg.own.smartcbt.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;

import kg.own.smartcbt.CrashHandler;
import kg.own.smartcbt.R;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeErrorHandling();
        initializeUI();
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            Timber.i("Past crash: ");
            reportCrash(extras.getString("crashReport"), extras.getString("cause"));
        }
    }

    private void initializeErrorHandling() {
        Timber.plant(new Timber.DebugTree(){
            @NotNull
            @Override
            protected String createStackElementTag(@NotNull StackTraceElement element) {
                return String.format("C:%s:%s",super.createStackElementTag(element), element.getLineNumber());
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    private void reportCrash(String crashReport, String cause) {
        String time = DateFormat.getDateTimeInstance().format(new Date());
        Timber.i("Crash results at - %s; cause - %s; Crash report - %s", time, cause, crashReport);
    }

    private void initializeUI() {
        findViewById(R.id.btnSeeEntries).setOnClickListener(this);
        findViewById(R.id.btnSeeBehaviour).setOnClickListener(this);
        findViewById(R.id.btnMakeEntry).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSeeEntries:
                openNewActivity("seeEntries");
                break;
            case R.id.btnSeeBehaviour:
                requestUsagePermission();
                //openNewActivity("seeBehaviour");
                break;
            case R.id.btnMakeEntry:
                openNewActivity("makeEntries");
                break;
        }
    }

    private void openNewActivity(String activity) {
        Intent startNewActivity;
        try {
            switch (activity) {
                case "seeEntries":
                    startNewActivity = new Intent(this, SeeEntriesActivity.class);
                    break;
                case "seeBehaviour":
                    startNewActivity = new Intent(this, SeeBehaviourActivity.class);
                    break;
                case "makeEntries":
                    startNewActivity = new Intent(this, MakeEntryActivity.class);
                    break;
                default:
                    throw new Exception("Activity could not be identified");
            }
            startNewActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Timber.i("launching activity");
            this.startActivity(startNewActivity);

        }catch (Exception e){
            Timber.e("Error opening activity: %s", e.getLocalizedMessage());
        }
    }

    private void requestUsagePermission() {
        if(establishStateOfUsageStatisticsPermission()){

            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101:
                Timber.i("");
                break;
        }
    }

    private Boolean establishStateOfUsageStatisticsPermission() {

        int mode = 2;
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mode = appOpsManager.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), String.valueOf(getPackageName()));
            } else {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), String.valueOf(getPackageName()));
            }
        }
        return (mode != AppOpsManager.MODE_ALLOWED);
    }
}

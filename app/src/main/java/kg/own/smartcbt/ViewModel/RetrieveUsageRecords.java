package kg.own.smartcbt.ViewModel;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import kg.own.smartcbt.Model.UsageRecord;
import timber.log.Timber;

public class RetrieveUsageRecords extends AsyncTask<Void, Void, Void> {

    private final int hour, minute;
    private final Context context;
    private final RetrieveUsageInterface callBack;
    private LinkedList<UsageRecord> toReturn;

    RetrieveUsageRecords(final int hour,final int minute, final Context context, RetrieveUsageInterface callBack) {
        this.hour = hour;
        this.minute = minute;
        this.context = context;
        this.callBack = callBack;
        toReturn = new LinkedList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");

        if (usageStatsManager == null) {
            Timber.e("usm is null");
            return null;
        }

        UsageEvents usageEvents = establishUsageEvents(usageStatsManager);

        if (usageEvents == null) {
            Timber.e("UsageEvents is null");
        }

        if (usageEvents != null) {
             toReturn = queryUsage(usageEvents);
        }

        return null;

    }

    private LinkedList<UsageRecord> queryUsage(UsageEvents usageEvents) {
        PackageManager packageManager = context.getPackageManager();
        LinkedList<UsageRecord> allData = new LinkedList<>();

        while(usageEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            usageEvents.getNextEvent(e);

            String appName;
            try{
                appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(e.getPackageName(), PackageManager.GET_META_DATA));
            }catch (PackageManager.NameNotFoundException nameNotFound){
                appName = e.getPackageName();
            }

            allData.add(new UsageRecord(
                    e.getTimeStamp(),
                    appName,
                    e.getEventType()
            )) ;

        }

        return allData;
    }

    private UsageEvents establishUsageEvents(UsageStatsManager usageStatsManager) {
        Calendar start = Calendar.getInstance();
        Calendar stop = Calendar.getInstance();

        Date date = stop.getTime();
        stop.setTime(date);
        stop.set(Calendar.MILLISECOND, 0);
        stop.set(Calendar.SECOND, 0);
        stop.set(Calendar.MINUTE, minute);
        stop.set(Calendar.HOUR_OF_DAY, hour);

        start.setTime(date);
        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MINUTE, minute);
        start.set(Calendar.HOUR_OF_DAY, hour - 1);

        return usageStatsManager.queryEvents(start.getTimeInMillis(), stop.getTimeInMillis());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callBack.recordsGathered(toReturn);
    }
}

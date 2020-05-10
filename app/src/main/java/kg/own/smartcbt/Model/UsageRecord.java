package kg.own.smartcbt.Model;

public class UsageRecord {

    public final Long UNIXTime;
    public final String app;
    public final int event;

    public UsageRecord(Long UNIXTime, String app, int event){
        this.UNIXTime = UNIXTime;
        this.app = app;
        this.event = event;
    }

}
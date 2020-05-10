package kg.own.smartcbt.ViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

import kg.own.smartcbt.Model.UsageRecord;

public interface RetrieveUsageInterface {

    void recordsGathered(LinkedList<UsageRecord> toReturn);
}

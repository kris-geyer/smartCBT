package kg.own.smartcbt.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.Model.Entry;
import kg.own.smartcbt.R;
import timber.log.Timber;

public class EntryExpandableList extends BaseExpandableListAdapter {

    private final Context context;
    private List<Day> days;


    EntryExpandableList(Context context, List<Day> dataForList){
        this.context = context;
        this.days = dataForList;
    }

    @Override
    public int getGroupCount() {
        return days.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        Timber.i("Get children count: " + days.get(groupPosition).emotions.keySet().size());
        return days.get(groupPosition).emotions.keySet().size();
    }

    @Override
    public Day getGroup(int groupPosition) {
        return days.get(groupPosition);
    }

    @Override
    public StringBuilder getChild(int groupPosition, int childPosition) {
        Timber.i("Child postion: %s", childPosition);
        days.get(groupPosition).establishEmotions();
        Iterator it = days.get(groupPosition).emotions.entrySet().iterator();
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();

        while (it.hasNext()) {
            if(!String.valueOf(stringBuilder).equals("")){
                stringBuilder.append("\n");
            }

            Map.Entry pair = (Map.Entry)it.next();
            if(count == childPosition){
                stringBuilder.append(pair.getKey()).append(" x").append(pair.getValue());
            }
        }
        return stringBuilder;
    }

    @Override
    public long getGroupId(int groupPosition) {
        Timber.i("Group ID %s", groupPosition);
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Timber.i("Get Group View called");
        Day day = getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView date = convertView.findViewById(R.id.date);
        date.setText(day.date);

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Timber.i("Get child view called");
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        //WORK ON THIS BIT

        TextView emotion, count;
        emotion = convertView.findViewById(R.id.tvEmotion);

        StringBuilder message = getChild(groupPosition, childPosition);

        if(message!= null){
            Timber.i("message %s", message);
        }
        emotion.setText(message);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

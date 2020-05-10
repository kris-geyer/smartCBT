package kg.own.smartcbt.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kg.own.smartcbt.Model.Behaviour;
import kg.own.smartcbt.Model.Emotion;
import kg.own.smartcbt.Model.Entry;
import kg.own.smartcbt.Model.Thought;
import kg.own.smartcbt.R;
import timber.log.Timber;

public class myRec extends RecyclerView.Adapter<myRec.MyViewHolder> {
    private final ArrayList<Entry> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvTime, tvEmotion, tvThoughts, tvBehaviours;
        public MyViewHolder(View v) {
            super(v);
            tvTime = v.findViewById(R.id.tvTime);
            tvEmotion = v.findViewById(R.id.tvEmotions);
            tvThoughts = v.findViewById(R.id.tvThoughts);
            tvBehaviours = v.findViewById(R.id.tvBehaviours);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public myRec(ArrayList<Entry> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public myRec.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Timber.i("Time: %s", mDataset.get(position).time);
        holder.tvTime.setText(mDataset.get(position).time);
        StringBuilder emotions = new StringBuilder();
        for (Emotion emotion: mDataset.get(position).emotion){
            for( String emotionString: emotion.emotions){
                emotions.append(emotionString).append("\n");
            }
        }

        holder.tvEmotion.setText(emotions);

        StringBuilder thoughts = new StringBuilder();

        for (Thought thought: mDataset.get(position).thoughts){
            for( String thoughtStrings: thought.thoughts){
                thoughts.append(thoughtStrings).append("\n");
            }
        }

        holder.tvThoughts.setText(thoughts);

        StringBuilder behaviours = new StringBuilder();

        for (Behaviour behaviour: mDataset.get(position).behaviour){
            for( String behaviourString: behaviour.behaviours){
                behaviours.append(behaviourString).append("\n");
            }
        }

        holder.tvBehaviours.setText(behaviours);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Timber.i("mDataset size: %s", mDataset.size());
        return mDataset.size();
    }
}
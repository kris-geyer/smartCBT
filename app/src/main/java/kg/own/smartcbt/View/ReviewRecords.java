package kg.own.smartcbt.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kg.own.smartcbt.Model.Day;
import kg.own.smartcbt.Model.Entry;
import kg.own.smartcbt.R;

public class ReviewRecords extends AppCompatActivity {

    ArrayList<Entry> entries;
    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_records);
        Intent i = getIntent();
        Day day = (Day) i.getSerializableExtra("dayObject");
        if(day!=null){
            date = day.date;
            entries = day.getEntries();
            generateUI();
        }else{
            Toast.makeText(this, "Something went wrong ... sorry", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateUI() {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new myRec(entries);
        recyclerView.setAdapter(adapter);

    }
}

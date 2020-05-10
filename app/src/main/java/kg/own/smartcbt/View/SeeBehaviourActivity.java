package kg.own.smartcbt.View;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kg.own.smartcbt.R;

public class SeeBehaviourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_behaviour);
        Log.i("new activity","See behaviour Activity");
    }
}

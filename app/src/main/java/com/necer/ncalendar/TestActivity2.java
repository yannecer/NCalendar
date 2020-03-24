package com.necer.ncalendar;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by necer on 2020/3/24.
 */
public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


        float dimension = getResources().getDimension(R.dimen.N_calendarHeight);
        float N_pppp = getResources().getInteger(R.integer.N_animationDuration);

        Log.e("float", "float::" + dimension);
        Log.e("float", "float::" + N_pppp);
    }
}

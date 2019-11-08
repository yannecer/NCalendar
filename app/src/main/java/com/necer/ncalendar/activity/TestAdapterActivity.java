package com.necer.ncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.necer.ncalendar.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_adapter);
    }


    public void general(View view) {

        startActivity(new Intent(this, GeneralAdapterActivity.class));

    }

    public void ding(View view) {

        startActivity(new Intent(this, DingAdapterActivity.class));
    }


}

package com.necer.ncalendar.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.necer.enumeration.SelectedModel;

public abstract class BaseActivity extends AppCompatActivity {

    protected final static String TAG = "NECER";
    protected String title;
    protected SelectedModel selectedModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedModel = (SelectedModel) getIntent().getSerializableExtra("selectedModel");
        title = getIntent().getStringExtra("title");

        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }


}

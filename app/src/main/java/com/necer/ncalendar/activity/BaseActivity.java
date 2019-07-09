package com.necer.ncalendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

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

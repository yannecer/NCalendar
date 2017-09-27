package necer.ncalendardemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import necer.ncalendardemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toMiui(View v) {
        startActivity(new Intent(this, NCalendarActivity.class));
    }

    public void defaultSelect(View v) {
        startActivity(new Intent(this, MonthSelectActivity.class));
    }

    public void notDefaultSelect(View v) {
        startActivity(new Intent(this, MonthNotSelectActivity.class));
    }
}
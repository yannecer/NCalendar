package necer.ncalendardemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import necer.ncalendardemo.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本：" + Utils.getCurrentVersion(this));
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

    public void week(View v) {
        startActivity(new Intent(this, WeekActivity.class));
    }

    public void fragment(View v) {
        startActivity(new Intent(this, TestFragmentActivity.class));
    }


}
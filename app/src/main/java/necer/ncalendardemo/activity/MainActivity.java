package necer.ncalendardemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import necer.ncalendardemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)  ;
        setContentView(R.layout.activity_main);
    }


    public void month(View view) {
        startActivity(new Intent(this, MonthCalendarActivity.class));
    }

    public void week(View view) {
        startActivity(new Intent(this, WeekCalendarActivity.class));
    }

    public void monthAndWeek(View view) {
        startActivity(new Intent(this, MonthAndWeekCalendarActivity.class));
    }


    public void ncanlendar(View view) {
        startActivity(new Intent(this, NCalendarActivity.class));
    }

}

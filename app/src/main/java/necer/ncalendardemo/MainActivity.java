package necer.ncalendardemo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

   // private MonthCalendar monthCalendar;
    //private WeekCalendar weekCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  monthCalendar = (MonthCalendar) findViewById(monthCalendar);
       // weekCalendar = (WeekCalendar) findViewById(weekCalendar);
        monthCalendar.setOnClickMonthCalendarListener(new OnClickMonthCalendarListener() {
            @Override
            public void onClickMonthCalendar(DateTime dateTime) {
                MyLog.d("onClickMonthCalendar:::" + dateTime.toLocalDate());
            }
        });

        monthCalendar.setOnMonthCalendarPageChangeListener(new OnMonthCalendarPageChangeListener() {
            @Override
            public void onMonthCalendarPageSelected(DateTime dateTime) {
                MyLog.d("onMonthCalendarPageSelected::" + dateTime.toLocalDate());
            }
        });
*/

/*
        weekCalendar.setOnClickWeekCalendarListener(new OnClickWeekCalendarListener() {
            @Override
            public void onClickWeekCalendar(DateTime dateTime) {
                MyLog.d("onClickWeekCalendar:::" + dateTime.toLocalDate());
            }
        });
        weekCalendar.setOnWeekCalendarPageChangeListener(new OnWeekCalendarPageChangeListener() {
            @Override
            public void onWeekCalendarPageSelected(DateTime dateTime) {
                MyLog.d("onWeekCalendarPageSelected::" + dateTime.toLocalDate());
            }
        });*/
    }


    public void aaa(View view) {
        //DateTime currectDateTime = monthCalendar.getSelectDateTime();
       // MyLog.d("currectDateTime:::" + currectDateTime);
    }

    public void bbb(View view) {
       // weekCalendar.setDate(2088, 6, 10);
    }

}

package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.calendar.MonthCalendar;
import com.necer.ncalendar.listener.OnClickMonthCalendarListener;
import com.necer.ncalendar.listener.OnMonthCalendarPageChangeListener;
import com.necer.ncalendar.utils.MyLog;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2017/6/15.
 */

public class MonthCalendarActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_today;
    private ImageView iv_finish;
    private TextView tv_title;
    private MonthCalendar monthCalendar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        getSupportActionBar().hide();


        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        monthCalendar = (MonthCalendar) findViewById(R.id.monthCalendar);


        tv_today.setOnClickListener(this);
        iv_finish.setOnClickListener(this);


        monthCalendar.setOnClickMonthCalendarListener(new OnClickMonthCalendarListener() {
            @Override
            public void onClickMonthCalendar(DateTime dateTime) {
                Toast.makeText(MonthCalendarActivity.this, "选择了：：" + dateTime.toLocalDate(), Toast.LENGTH_SHORT).show();
            }
        });

        monthCalendar.setOnMonthCalendarPageChangeListener(new OnMonthCalendarPageChangeListener() {
            @Override
            public void onMonthCalendarPageSelected(DateTime dateTime) {
                tv_title.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月");
                MyLog.d("dateTime::" + dateTime);
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_today:
                DateTime dateTime = new DateTime();
                monthCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),true);
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }
}

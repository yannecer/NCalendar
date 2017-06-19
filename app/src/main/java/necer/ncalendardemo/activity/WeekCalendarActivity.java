package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.calendar.WeekCalendar;
import com.necer.ncalendar.listener.OnClickWeekCalendarListener;
import com.necer.ncalendar.listener.OnWeekCalendarPageChangeListener;

import org.joda.time.DateTime;

import necer.ncalendardemo.R;

/**
 * Created by necer on 2017/6/15.
 */

public class WeekCalendarActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_today;
    private ImageView iv_finish;
    private TextView tv_title;
    private WeekCalendar weekCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        getSupportActionBar().hide();

        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        weekCalendar = (WeekCalendar) findViewById(R.id.weekCalendar);

        tv_today.setOnClickListener(this);
        iv_finish.setOnClickListener(this);

        weekCalendar.setOnClickWeekCalendarListener(new OnClickWeekCalendarListener() {
            @Override
            public void onClickWeekCalendar(DateTime dateTime) {
                Toast.makeText(WeekCalendarActivity.this, "选择了：：" + dateTime.toLocalDate(), Toast.LENGTH_SHORT).show();
            }
        });
        weekCalendar.setOnWeekCalendarPageChangeListener(new OnWeekCalendarPageChangeListener() {
            @Override
            public void onWeekCalendarPageSelected(DateTime dateTime) {
                tv_title.setText(dateTime.getYear() + "年" + dateTime.getMonthOfYear() + "月");

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_today:
                DateTime dateTime = new DateTime();
                weekCalendar.setDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),true);

                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }
}

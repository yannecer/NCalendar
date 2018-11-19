package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.necer.calendar.EmuiCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2018/11/12.
 */
public class TestEmuiActivity extends AppCompatActivity {



    TextView tv_lunar;
    TextView tv_date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emui);


        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        tv_lunar = findViewById(R.id.tv_lunar);
        tv_date = findViewById(R.id.tv_date);






        EmuiCalendar emuiCalendar = findViewById(R.id.emuiCalendar);
        emuiCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarDateChanged(NDate date) {
                tv_date.setText(date.localDate.getYear() + "年" + date.localDate.getMonthOfYear() + "月");
                tv_lunar.setText((Days.daysBetween(new LocalDate(), date.localDate).getDays() + "天后  ") + "农历" + date.lunar.lunarYearStr + "年 " + date.lunar.lunarMonthStr + date.lunar.lunarDayStr);
               // tv_date.setText(date.lunar.lunarMonthStr + date.lunar.lunarDayStr + (TextUtils.isEmpty(date.lunarHoliday) ? "" : (" | " + date.lunarHoliday)) + (TextUtils.isEmpty(date.solarHoliday) ? "" : (" | " + date.solarHoliday)));
            }

            @Override
            public void onCalendarStateChanged(boolean isMonthSate) {

            }
        });

    }
}

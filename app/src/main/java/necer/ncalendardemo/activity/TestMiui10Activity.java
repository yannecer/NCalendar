package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.necer.calendar.Miui10Calendar;
import com.necer.entity.NDate;
import com.necer.listener.OnCalendarChangedListener;

import necer.ncalendardemo.R;
import necer.ncalendardemo.adapter.AAAdapter;

/**
 * Created by necer on 2018/11/12.
 */
public class TestMiui10Activity extends AppCompatActivity {

    TextView tv_month;
    TextView tv_week;
    TextView tv_year;
    TextView tv_lunar;
    TextView tv_lunar_tg;


    private final String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日",};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miui10);


        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        tv_month = findViewById(R.id.tv_month);
        tv_week = findViewById(R.id.tv_week);
        tv_year = findViewById(R.id.tv_year);
        tv_lunar = findViewById(R.id.tv_lunar);
        tv_lunar_tg = findViewById(R.id.tv_lunar_tg);


        Miui10Calendar miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarDateChanged(NDate date) {
                tv_year.setText(date.localDate.getYear() + "年");
                tv_month.setText(date.localDate.getMonthOfYear() + "月");
                tv_week.setText(weeks[date.localDate.getDayOfWeek() - 1]);
                tv_lunar.setText("农历" + date.lunar.lunarYearStr + "年 ");
                tv_lunar_tg.setText(date.lunar.lunarMonthStr + date.lunar.lunarDayStr + (TextUtils.isEmpty(date.lunarHoliday) ? "" : (" | " + date.lunarHoliday)) + (TextUtils.isEmpty(date.solarHoliday) ? "" : (" | " + date.solarHoliday)));

            }

            @Override
            public void onCalendarStateChanged(boolean isMonthSate) {

            }
        });




    }
}

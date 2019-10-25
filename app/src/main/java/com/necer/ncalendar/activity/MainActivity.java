package com.necer.ncalendar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.necer.enumeration.SelectedModel;
import com.necer.ncalendar.R;
import com.necer.ncalendar.TestActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本：" + Utils.getCurrentVersion(this));


    }


    public void month_selected(View view) {
        startActivity(getNewIntent(TestMonthActivity.class, SelectedModel.SINGLE_SELECTED, "月日历默认选中"));
    }

    public void month_unSelected(View view) {
        startActivity(getNewIntent(TestMonthActivity.class, SelectedModel.SINGLE_UNSELECTED, "月日历默认不选中"));
    }

    public void month_multiple(View view) {
        startActivity(getNewIntent(TestMonthActivity.class, SelectedModel.MULTIPLE, "月日历多选"));
    }

    public void week_selected(View view) {
        startActivity(getNewIntent(TestWeekActivity.class, SelectedModel.SINGLE_SELECTED, "周日历默认选中"));
    }

    public void week_unSelected(View view) {
        startActivity(getNewIntent(TestWeekActivity.class, SelectedModel.SINGLE_UNSELECTED, "周日历默认不选中"));
    }

    public void week_multiple(View view) {
        startActivity(getNewIntent(TestWeekActivity.class, SelectedModel.MULTIPLE, "周日历多选"));
    }

    public void miui9_selected(View view) {
        startActivity(getNewIntent(TestMiui9Activity.class, SelectedModel.SINGLE_SELECTED, "miui9默认选中"));
    }

    public void miui9_unSelected(View view) {
        startActivity(getNewIntent(TestMiui9Activity.class, SelectedModel.SINGLE_UNSELECTED, "miui9默认不选中"));
    }

    public void miui9_multiple(View view) {
        startActivity(getNewIntent(TestMiui9Activity.class, SelectedModel.MULTIPLE, "miui9多选"));
    }

    public void miui10_selected(View view) {
        startActivity(getNewIntent(TestMiui10Activity.class, SelectedModel.SINGLE_SELECTED, "miui10默认选中"));
    }

    public void miui10_unSelected(View view) {
        startActivity(getNewIntent(TestMiui10Activity.class, SelectedModel.SINGLE_UNSELECTED, "miui10默认不选中"));
    }

    public void miui10_multiple(View view) {
        startActivity(getNewIntent(TestMiui10Activity.class, SelectedModel.MULTIPLE, "miui10多选"));
    }

    public void emiui_selected(View view) {
        startActivity(getNewIntent(TestEmuiActivity.class, SelectedModel.SINGLE_SELECTED, "emiui默认选中"));
    }

    public void emiui_unSelected(View view) {
        startActivity(getNewIntent(TestEmuiActivity.class, SelectedModel.SINGLE_UNSELECTED, "emiui默认不选中"));
    }

    public void emiui_multiple(View view) {
        startActivity(getNewIntent(TestEmuiActivity.class, SelectedModel.MULTIPLE, "emiui多选"));
    }

    public void toHoldWeek(View view) {
        startActivity(new Intent(this, TestWeekHoldActivity.class));
    }

    public void addView(View view) {
        startActivity(new Intent(this, TestAddViewActivity.class));
    }

    public void custom_painter(View view) {
        startActivity(new Intent(this, CustomCalendarActivity.class));
    }

    public void viewpager(View view) {
        startActivity(new Intent(this, TestViewPagerActivity.class));
    }

    public void general(View view) {
        startActivity(new Intent(this, TestGeneralActivity.class));
    }

    public void stretch(View view) {
        startActivity(new Intent(this, TestStretchActivity.class));
    }

    public void test(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }


    private Intent getNewIntent(Class<? extends BaseActivity> clazz, SelectedModel selectedModel, String title) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("selectedModel", selectedModel);
        intent.putExtra("title", title);
        return intent;
    }

}
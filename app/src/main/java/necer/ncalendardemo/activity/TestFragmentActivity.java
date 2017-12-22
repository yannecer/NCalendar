package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import necer.ncalendardemo.R;
import necer.ncalendardemo.fragment.TestFragment;

/**
 * Created by necer on 2017/12/22.
 */

public class TestFragmentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        getSupportActionBar().hide();


        getSupportFragmentManager().beginTransaction().replace(R.id.fl_, TestFragment.newInstance("","")).commit();


    }
}

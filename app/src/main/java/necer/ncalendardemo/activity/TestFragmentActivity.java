package necer.ncalendardemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import necer.ncalendardemo.R;
import necer.ncalendardemo.fragment.TestFragment;
import necer.ncalendardemo.fragment.TestFragment1;
import necer.ncalendardemo.fragment.TestFragment2;

/**
 * Created by necer on 2017/12/22.
 */

public class TestFragmentActivity extends AppCompatActivity {

    private Fragment currentFragment ;
    private Fragment fragment1;
    private Fragment fragment2;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        getSupportActionBar().hide();

        fragment1 = new TestFragment1();
        fragment2 = new TestFragment2();

        currentFragment = fragment1;

        getSupportFragmentManager().beginTransaction().add(R.id.fl_, fragment1).show(fragment1).commit();

    }

    public void bt1(View view) {
        showFragment(fragment1);
    }

    public void bt2(View view) {
        showFragment(fragment2);
    }

    private void showFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != fragment) {
            fragmentTransaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                fragmentTransaction.add(R.id.fl_, fragment).show(fragment).commit();
            } else {
                fragmentTransaction.show(fragment).commit();
            }
        }
    }
}

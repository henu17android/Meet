package com.example.meet.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.meet.R;
import com.example.meet.fragment.SettingFragment;
import com.example.meet.fragment.ShareFragment;
import com.example.meet.helper.NavHelper;
import com.example.meet.fragment.TaskFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

public class MainActivity extends AppCompatActivity implements
        CalendarView.OnYearChangeListener,//宿主Activity实现接口，在fragment中实例化
        CalendarView.OnCalendarSelectListener,
        NavHelper.OnTabChangeListener {

    private BottomNavigationView navigationView;
    private FragmentManager mFragmentManager;
    private NavHelper<Integer> mNavHelper;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_checkItem:
                    mNavHelper.performClickMenu(R.id.navigation_checkItem);
                    return true;
                case R.id.navigation_share:
                    mNavHelper.performClickMenu(R.id.navigation_share);
                    return true;
                case R.id.navigation_set:
                    mNavHelper.performClickMenu(R.id.navigation_set);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initView();

    }

    private void initView() {
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);

        mFragmentManager = getSupportFragmentManager();

        mNavHelper = new NavHelper<>(this, R.id.lay_container, mFragmentManager, this);
        mNavHelper.add(R.id.navigation_checkItem, new NavHelper.Tab<Integer>(TaskFragment.class, R.id.navigation_checkItem))
                .add(R.id.navigation_share, new NavHelper.Tab<Integer>(ShareFragment.class, R.id.navigation_share))
                .add(R.id.navigation_set, new NavHelper.Tab<Integer>(SettingFragment.class, R.id.navigation_set));

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Menu menu = navigationView.getMenu();
        //navigation默认选中任务界面
        navigationView.setSelectedItemId(menu.getItem(1).getItemId());
        //触发首次选中TaskFragment
        menu.performIdentifierAction(R.id.navigation_checkItem, 0);

    }


    @Override
    public void onYearChange(int year) {

    }


    //fragment切换时设置
    @Override
    public void onTabChange(NavHelper.Tab newTab, NavHelper.Tab oldTab) {

    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
    }
}


package com.nantonelli.smu_now;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nantonelli.smu_now.Adapters.PagerAdapter;
import com.nantonelli.smu_now.Events.BackEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ndantonelli on 9/2/15.
 */
public class Activity_Home extends AppCompatActivity {
    @Inject Bus eventBus;
    private ImageView userAccountButton;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    private TabLayout tabLayout;
    private boolean attendanceClicked=true;
    private boolean minClicked = true;
    private EditText timeInput;
    private SeekBar timeSeek;
    private PagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OasysApplication)getApplication()).getObjectGraph().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupToolbar();
        setupTabs();
        setupNavButtonClicks();
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        userAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
//                Toast.makeText(getBaseContext(), "We should open the nav bar now", Toast.LENGTH_LONG).show();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new com.nantonelli.smu_now.Adapters.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        eventBus.register(this);
    }
    @Override
    public void onPause(){
        super.onPause();
        eventBus.unregister(this);
    }
    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.addView(getLayoutInflater().inflate(R.layout.custom_toolbar, null));
        userAccountButton = (ImageView) findViewById(R.id.settings);
        setSupportActionBar(toolbar);
    }

    private void setupTabs(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.time_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.popularity_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.my_events_selector));
    }

    private void setupNavButtonClicks(){
        timeInput = (EditText)findViewById(R.id.time_input);
        timeSeek = (SeekBar)findViewById(R.id.time_seekbar);
        final Button attendanceButton=(Button)findViewById(R.id.attendance_button);
        final Button commentsButton=(Button)findViewById(R.id.comments_button);
        final TextView maxTime = (TextView)findViewById(R.id.max_time);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!attendanceClicked){
                    attendanceButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark,null));
                    attendanceButton.setAlpha(1.0f);
                    commentsButton.setBackgroundColor(getResources().getColor(R.color.my_primary,null));
                    commentsButton.setAlpha(.7f);
                    attendanceClicked = true;
                }
            }
        });
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendanceClicked){
                    commentsButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark,null));
                    commentsButton.setAlpha(1.0f);
                    attendanceButton.setBackgroundColor(getResources().getColor(R.color.my_primary, null));
                    attendanceButton.setAlpha(.7f);
                    attendanceClicked = false;
                }
            }
        });

        final Button minButton=(Button)findViewById(R.id.minutes_button);
        final Button hourButton=(Button)findViewById(R.id.hours_button);
        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!minClicked) {
                    minButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark, null));
                    minButton.setAlpha(1.0f);
                    hourButton.setBackgroundColor(getResources().getColor(R.color.my_primary, null));
                    hourButton.setAlpha(.7f);
                    timeSeek.setMax(180);
                    timeSeek.setProgress(90);
                    timeInput.setText(Integer.toString(90));
                    maxTime.setText("3 h");
                    minClicked = true;
                }
            }
        });
        hourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minClicked) {
                    hourButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark, null));
                    hourButton.setAlpha(1.0f);
                    minButton.setBackgroundColor(getResources().getColor(R.color.my_primary, null));
                    minButton.setAlpha(.7f);
                    timeSeek.setMax(72);
                    timeSeek.setProgress(36);
                    timeInput.setText(Integer.toString(36));
                    maxTime.setText("72 h");
                    minClicked = false;
                }
            }
        });
        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeInput.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        if(OasysApplication.getInstance().onFirst == false && viewPager.getCurrentItem() == 0) {
            eventBus.post(new BackEvent());
        }
        else{
            super.onBackPressed();
        }
    }
}


package com.nantonelli.smu_now;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Adapters.PagerAdapter;
import com.nantonelli.smu_now.Adapters.WeatherAPI;
import com.nantonelli.smu_now.Adapters.upcomingListAdapter;
import com.nantonelli.smu_now.Events.BackEvent;
import com.nantonelli.smu_now.Events.EventsRepoRefreshed;
import com.nantonelli.smu_now.Fragments.UsernameDialog;
import com.nantonelli.smu_now.Model.DailyWeather;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.Model.EventsRepo;
import com.nantonelli.smu_now.Model.Forecast;
import com.nantonelli.smu_now.Model.IProjectDialFrag;
import com.nantonelli.smu_now.Model.WeatherResponse;
import com.nantonelli.smu_now.Response.EventsResponse;
import com.nantonelli.smu_now.Response.UserResponse;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ndantonelli on 9/2/15.
 * Home Activity which holds the tab layout and the drawer layout for the app
 * has to hold all of the code for the drawer layout and the tab layouts
 * Ideally should have split nav drawer into a fragment to reduce code in this activity
 */
public class Activity_Home extends AppCompatActivity implements IProjectDialFrag{
    @Inject Bus eventBus;
    @Inject OasysRestfulAPI service;
    @Inject EventsRepo repo;
    @Inject Picasso picasso;
    @Inject WeatherAPI weatherAPI;

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.splash) RelativeLayout splash;
    @Bind(R.id.username) TextView username;
    @Bind(R.id.uber_selection_list) ListView selectionList;
    @Bind(R.id.pager) ViewPager viewPager;

    private SeekBar timeSeek;

    private ImageView userAccountButton;
    private ImageView conditions1;
    private ImageView conditions2;
    private ImageView conditions3;

    private TextView date1;
    private TextView date2;
    private TextView date3;
    private TextView high1;
    private TextView high2;
    private TextView high3;
    private TextView low1;
    private TextView low2;
    private TextView low3;
    private TextView timeInput;

    private TabLayout tabLayout;

    private boolean attendanceClicked=true;
    private boolean minClicked = true;

    private PagerAdapter adapter;
    private upcomingListAdapter radioAdapter;

    private UsernameDialog projectDialFrag = new UsernameDialog();

    private static final String TAG = "HOME_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OasysApplication)getApplication()).getObjectGraph().inject(this);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupToolbar();

        //get the users username to set the nav drawer header
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username.setText(prefs.getString("username", "Click Text!"));

        //remove the main splash screen from view.  Splash screen used to load data cleanly
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_out));
                splash.setVisibility(View.GONE);
            }
        }, 3000);

        userAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        setupTabs();
        setupNavButtonClicks();

        //selection list in the navdrawer for selecting an event to uber to quickly
        radioAdapter = new upcomingListAdapter(this, new ArrayList<Event>());
        selectionList.setAdapter(radioAdapter);

        getEvents();

        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(2);//prevents tabs from recycling and screwing up the inner fragments
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

    // Gets all events for the whole app (Upcoming, Popular, and My Events)
    public void getEvents(){

        //Get Upcoming events
        service.getEvents(OasysApplication.getInstance().timeFormat, new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                if (events != null)
                    repo.setTimeEvents(events);
            }

            @Override
            public void failure(RetrofitError error) {
                    Log.e(TAG, "Failed to get Upcoming Events", error);
            }
        });
        service.getPopularEvents(OasysApplication.getInstance().timeFormat,
                OasysApplication.getInstance().popularity, new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                if(events != null)
                    repo.setPopularEvents(events);
            }

            @Override
            public void failure(RetrofitError error) {
                    Log.e(TAG, "Failed to get Popular Events", error);
            }
        });

        service.getMyEvents(OasysApplication.getInstance().timeFormat,
                OasysApplication.getInstance().uid, new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                if(events != null)
                    repo.setMyEvents(events);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to get My Events", error);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        eventBus.register(this); //must register activities on resume to obtain messages
    }

    @Override
    public void onPause(){
        super.onPause();
        eventBus.unregister(this); // must unregister to reduce number of things getting messages
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("minutes", minClicked).putBoolean("attendance", attendanceClicked)
                .putInt("timeBar", timeSeek.getProgress()).apply();
    }

    //setup the toolbar and its views
    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.addView(getLayoutInflater().inflate(R.layout.custom_toolbar, null));
        userAccountButton = (ImageView) findViewById(R.id.settings);

        //assigns all of the views for the weather conditions
        conditions1 = (ImageView) findViewById(R.id.weather_image1);
        conditions2 = (ImageView) findViewById(R.id.weather_image2);
        conditions3 = (ImageView) findViewById(R.id.weather_image3);

        date1 = (TextView) findViewById(R.id.weather_date1);
        date2 = (TextView) findViewById(R.id.weather_date2);
        date3 = (TextView) findViewById(R.id.weather_date3);

        high1 = (TextView) findViewById(R.id.weather_high1);
        high2 = (TextView) findViewById(R.id.weather_high2);
        high3 = (TextView) findViewById(R.id.weather_high3);

        low1 = (TextView) findViewById(R.id.weather_low1);
        low2 = (TextView) findViewById(R.id.weather_low2);
        low3 = (TextView) findViewById(R.id.weather_low3);

        setSupportActionBar(toolbar);
        fillWeather();
    }

    private void setupTabs(){
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.time_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.popularity_selector));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.my_events_selector));
    }

    private void setupNavButtonClicks(){
        timeInput = (TextView)findViewById(R.id.time_input);
        timeSeek = (SeekBar)findViewById(R.id.time_seekbar);

        final Button attendanceButton=(Button)findViewById(R.id.attendance_button);
        final Button commentsButton=(Button)findViewById(R.id.comments_button);
        final TextView maxTime = (TextView)findViewById(R.id.max_time);

        //activate when the attendance button is clicked to change sorting popular by attendance
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!attendanceClicked){
                    attendanceButton.setBackgroundColor(getResources()
                            .getColor(R.color.my_primary_dark));
                    attendanceButton.setAlpha(1.0f);
                    commentsButton.setBackgroundColor(getResources()
                            .getColor(R.color.my_primary));
                    commentsButton.setAlpha(.7f);
                    attendanceClicked = true;

                    //sets popularity tag for the server to determine sorting method
                    OasysApplication.getInstance().popularity = "attendance";
                }
            }
        });

        //now sorting popular events by total comments
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendanceClicked){
                    commentsButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark));
                    commentsButton.setAlpha(1.0f);
                    attendanceButton.setBackgroundColor(getResources().getColor(R.color.my_primary));
                    attendanceButton.setAlpha(.7f);
                    attendanceClicked = false;
                    OasysApplication.getInstance().popularity = "comments";
                }
            }
        });

        final Button hrButton=(Button)findViewById(R.id.minutes_button);
        final Button dayButton=(Button)findViewById(R.id.hours_button);

        //now sorting upcoming by hours rather than days
        hrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!minClicked) {
                    hrButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark));
                    hrButton.setAlpha(1.0f);

                    dayButton.setBackgroundColor(getResources().getColor(R.color.my_primary));
                    dayButton.setAlpha(.7f);

                    timeSeek.setMax(24);
                    timeSeek.setProgress(12);

                    timeInput.setText(Integer.toString(12));
                    maxTime.setText("24 h");

                    minClicked = true;
                    OasysApplication.getInstance().timeFormat = timeSeek + ":00:00";
                }
            }
        });

        // now sorting by days (0-7)
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minClicked) {
                    dayButton.setBackgroundColor(getResources().getColor(R.color.my_primary_dark));
                    dayButton.setAlpha(1.0f);

                    hrButton.setBackgroundColor(getResources().getColor(R.color.my_primary));
                    hrButton.setAlpha(.7f);

                    timeSeek.setMax(7);
                    timeSeek.setProgress(3);
                    timeInput.setText(Integer.toString(3));

                    maxTime.setText("7 D");
                    minClicked = false;
                    OasysApplication.getInstance().timeFormat = timeSeek + " 00:00:00";
                }
            }
        });

        //on setting the new time interval, update the query parameter, and the textview
        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeInput.setText(Integer.toString(progress));
                if(minClicked)
                    OasysApplication.getInstance().timeFormat = progress + ":00:00";
                else
                    OasysApplication.getInstance().timeFormat = progress + " 00:00:00";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        timeSeek.setProgress(prefs.getInt("timeBar", 12));

        String formatTime;
        String popular;

        //use the right formatting for the first run of the get Events http requests
        if(!prefs.getBoolean("minutes", true)){
            dayButton.performClick();
            formatTime = timeSeek.getProgress() + " 00:00:00";
        }
        else
            formatTime = timeSeek.getProgress() + ":00:00";

        if(!prefs.getBoolean("attendance", true)){
            commentsButton.performClick();
            popular = "comments";
        }
        else
            popular = "attendance";

        OasysApplication.getInstance().timeFormat = formatTime;
        OasysApplication.getInstance().popularity = popular;
    }

    @Override
    //overrides the on back pressed because swapping fragments in a view pager screws up the
    //back stack.  Need to be able to effectively move backward if fragments are stacked
    public void onBackPressed(){
        if(viewPager.getCurrentItem() == 0 && !OasysApplication.getInstance().onFirstT)
            eventBus.post(new BackEvent(0));
        else if(viewPager.getCurrentItem() == 1 && !OasysApplication.getInstance().onFirstP)
            eventBus.post(new BackEvent(1));
        else if(viewPager.getCurrentItem() == 2 && !OasysApplication.getInstance().onFirstM == false)
            eventBus.post(new BackEvent(2));
        else
            super.onBackPressed();
    }

    @OnClick(R.id.username)
    //show the change username dialog fragment
    public void openDialog(){
        projectDialFrag.show(getSupportFragmentManager(), "projectDialog");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newName) {
        if(newName.length() > 0) {
            username.setText(newName);
            OasysApplication.getInstance().username = newName;
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString("username", newName).apply();

            //need to update the username on the server
            service.createUser(OasysApplication.getInstance().adId, newName, new Callback<UserResponse>() {
                @Override
                public void success(UserResponse response, Response response2) {
                    OasysApplication.getInstance().uid = response.getUser_id();
                    prefs.edit().putBoolean("firstLoad", false).putInt("uid", response.getUser_id()).apply();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Failed to update the username", error);
                }
            });
        }
    }

    @Subscribe
    //need to update when my events have been updated
    //these events are used for the uber list in the nav drawer
    public void onRefresh(EventsRepoRefreshed event){
        if(event.getType() == 2) {
            if(repo.getMyEvents().size() > 0) {
                ((Button) findViewById(R.id.uber_button)).setAlpha(1.0f);
                ((Button) findViewById(R.id.uber_button)).setClickable(true);
            }
            else{
                ((Button) findViewById(R.id.uber_button)).setAlpha(.5f);
                ((Button) findViewById(R.id.uber_button)).setClickable(false);
            }
            radioAdapter.addAll(repo.getMyEvents());
        }
    }

    @OnClick(R.id.uber_button)
    public void clickingUber(){

        Event event = radioAdapter.getSelectedEvent();

        //need to open up the uber app using deep linking, pass in destination address
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri =
                    "uber://?action=setPickup&pickup=my_location&client_id=8Ql0XvNLma2ufXLgYYB4MsURFuWV_Tpq&dropoff[nickname]="+event.getAddress();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
        //cant find the uber app on the device, need to send the user to the marketplace
        catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
            String url = "https://m.uber.com/sign-up?client_id=8Ql0XvNLma2ufXLgYYB4MsURFuWV_Tpq";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    //fill the weather in the toolbar
    public void fillWeather(){
        weatherAPI.getWeather(new Callback<WeatherResponse>() {
            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                List<DailyWeather> daily = weatherResponse.getForecast().getSimple().getDailyForecast();

                picasso.load(daily.get(0).getIcon_url()).priority(Picasso.Priority.HIGH).into(conditions1);
                date1.setText(daily.get(0).getDate().getDate());
                high1.setText(daily.get(0).getHighTemp().getFahrenheit());
                low1.setText(daily.get(0).getLow().getFahrenheit());

                picasso.load(daily.get(1).getIcon_url()).priority(Picasso.Priority.HIGH).into(conditions2);
                date2.setText(daily.get(1).getDate().getDate());
                high2.setText(daily.get(1).getHighTemp().getFahrenheit());
                low2.setText(daily.get(1).getLow().getFahrenheit());

                picasso.load(daily.get(2).getIcon_url()).priority(Picasso.Priority.HIGH).into(conditions3);
                date3.setText(daily.get(2).getDate().getDate());
                high3.setText(daily.get(2).getHighTemp().getFahrenheit());
                low3.setText(daily.get(2).getLow().getFahrenheit());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to retrieve the weather conditions", error);
            }
        });
    }

}


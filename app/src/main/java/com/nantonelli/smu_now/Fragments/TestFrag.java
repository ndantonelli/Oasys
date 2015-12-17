package com.nantonelli.smu_now.Fragments;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nantonelli.smu_now.Adapters.EventTabsAdapter;
import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Adapters.TimePageFragmentListener;
import com.nantonelli.smu_now.Events.BackEvent;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.Model.EventsRepo;
import com.nantonelli.smu_now.OasysApplication;
import com.nantonelli.smu_now.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ndantonelli on 9/28/15.
 * An event specific fragment used to display an events specific info
 * used to display header for an event and the description and comments
 */
public class TestFrag extends BaseFragment {
    private TimePageFragmentListener listener;
    private TabLayout tabLayout;
    private Event event;
    private int pos;
    private boolean clicked = false;

    private static final String TAG = "TEST_FRAGMENT";

    public static TestFrag newInstance(TimePageFragmentListener listener, Event event, int pos){
        TestFrag t = new TestFrag();
        t.listener = listener;
        t.event = event;
        t.pos = pos;
        return t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);

        //inflate all of the header views
        ((TextView) v.findViewById(R.id.startTime)).setText(event.getTime_start());
        ((TextView) v.findViewById(R.id.startDate)).setText(event.getDate_start());
        ((TextView) v.findViewById(R.id.event_title)).setText(event.getEvent_name());
        ((TextView) v.findViewById(R.id.location)).setText(event.getAddress());
        final TextView aCount = ((TextView) v.findViewById(R.id.attendance_count));
        aCount.setText(Integer.toString(event.getAttendance()));

        ((OasysApplication)getActivity().getApplication()).getObjectGraph().inject(this);
        ButterKnife.bind(v);

        final ImageView check = ((ImageView) v.findViewById(R.id.checkmark));
        if(repo.checkAttending(event.getEvent_id())){
            check.setAlpha(1.0f);
            clicked = true;
        }

        //allow users to attend and unattend an event
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicked) {
                    check.setAlpha(.5f);
                    service.removeAttending(OasysApplication.getInstance().uid, event.getEvent_id(), "inactive", new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            event.setAttendance(event.getAttendance() - 1);
                            repo.updateAttending(event, false);
                            aCount.setText(Integer.toString(event.getAttendance()));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
                else {
                    check.setAlpha(1.0f);
                    service.postAttending(OasysApplication.getInstance().uid, event.getEvent_id(), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            event.setAttendance(event.getAttendance() + 1);
                            repo.updateAttending(event, true);
                            aCount.setText(Integer.toString(event.getAttendance()));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }

            }
        });

        //load the event photo into the background of the header
        ImageView image = (ImageView) v.findViewById(R.id.event_img);
        picasso.load(event.getPhoto_url()).priority(Picasso.Priority.HIGH).into(image);

        //setup the tabs
        tabLayout = (TabLayout) v.findViewById(R.id.eventTabLayout);
        setupTabs();

        //fill up the tabs with the description and comments fragments
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pagerEvents);
        final EventTabsAdapter adapter = new EventTabsAdapter(getChildFragmentManager(), tabLayout.getTabCount(), event);
        viewPager.setAdapter(adapter);
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
        return v;
    }
    private void setupTabs(){
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("Description"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments"));
    }

    @Subscribe
    //need to back out of the fragment because it was open in the forefront
    public void onBackPressed(BackEvent event){
        if(event.getPos() == pos){
            listener.onSwitchToNextFragment(this.event);
            switch (pos){
                case 0:
                    OasysApplication.getInstance().onFirstT = true;
                    break;
                case 1:
                    OasysApplication.getInstance().onFirstP = true;
                    break;
                case 2:
                    OasysApplication.getInstance().onFirstM = true;
            }
        }

    }
}
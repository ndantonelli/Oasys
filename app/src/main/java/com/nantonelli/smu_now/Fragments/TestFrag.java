package com.nantonelli.smu_now.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nantonelli.smu_now.Adapters.EventTabsAdapter;
import com.nantonelli.smu_now.Adapters.TimePageFragmentListener;
import com.nantonelli.smu_now.Events.BackEvent;
import com.nantonelli.smu_now.OasysApplication;
import com.nantonelli.smu_now.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 9/28/15.
 */
public class TestFrag extends Fragment {
    static TimePageFragmentListener listener;
    private TabLayout tabLayout;
    @Inject Bus eventBus;
    public static TestFrag newInstance(TimePageFragmentListener listener){
        TestFrag t = new TestFrag();
        t.listener = listener;
        return t;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);

        ((OasysApplication)getActivity().getApplication()).getObjectGraph().inject(this);
        tabLayout = (TabLayout) v.findViewById(R.id.eventTabLayout);
        setupTabs();
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pagerEvents);
        final EventTabsAdapter adapter = new EventTabsAdapter(getFragmentManager(), tabLayout.getTabCount());
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
    @Subscribe
    public void onBackPressed(BackEvent event){
        listener.onSwitchToNextFragment();
        OasysApplication.getInstance().onFirst = true;
    }
}
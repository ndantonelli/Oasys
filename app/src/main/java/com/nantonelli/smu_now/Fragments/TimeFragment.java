package com.nantonelli.smu_now.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.nantonelli.smu_now.Adapters.GridAdapter;
import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Adapters.TimePageFragmentListener;
import com.nantonelli.smu_now.Events.EventsRepoRefreshed;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.Model.EventsRepo;
import com.nantonelli.smu_now.OasysApplication;
import com.nantonelli.smu_now.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ndantonelli on 9/26/15.
 */
public class TimeFragment extends BaseFragment {
    static TimePageFragmentListener listener;
    private List<Event> events;
    private GridAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    private static final String TAG = "TIME_FRAGMENT";

    public static TimeFragment newInstance(TimePageFragmentListener listener){
        TimeFragment t = new TimeFragment();
        t.listener = listener;
        return t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_layout, container, false);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);

        //refresh the upcoming events when swiped up
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                service.getEvents(OasysApplication.getInstance().timeFormat, new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        if (events != null)
                            repo.setTimeEvents(events);
                        else
                            repo.setTimeEvents(new ArrayList<Event>());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        repo.setTimeEvents(new ArrayList<Event>());
                        Log.d(TAG, "failed to refresh the upcoming events", error);
                    }
                });
            }
        });

        //fill the events gridview with the upcoming events
        GridView view = (GridView) v.findViewById(R.id.event_grid);
        events = repo.getTimeEvents();

        adapter = new GridAdapter(getActivity(), events, picasso);
        view.setAdapter(adapter);

        //on click open up the event specific fragments
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onSwitchToNextFragment(events.get(position));
                OasysApplication.getInstance().onFirstT = false;
            }
        });
        return v;
    }

    @Subscribe
    //activate whenever the Upcoming events have been refreshed
    public void onRefresh(EventsRepoRefreshed event){
        if(event.getType() == 0) {
            events = repo.getTimeEvents();
            adapter.refresh(events);
            swipeLayout.setRefreshing(false);
        }
    }

}

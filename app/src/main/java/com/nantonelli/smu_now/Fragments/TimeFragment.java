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

import com.nantonelli.smu_now.Activity_Event;
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
    private Button switchButton;
    static TimePageFragmentListener listener;
    private List<Event> events;
    private GridAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    @Inject OasysRestfulAPI restfulAPI;
    @Inject EventsRepo repo;

    public static TimeFragment newInstance(TimePageFragmentListener listener){
        TimeFragment t = new TimeFragment();
        t.listener = listener;
        return t;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_layout, container, false);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restfulAPI.getEvents(OasysApplication.getInstance().timeFormat, new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        if (events != null) {
                            Log.d("Successful Refresh", "we successfully refreshed 1");
                            repo.setTimeEvents(events);
                        }
                        else {
                            Log.d("Successful Refresh", "we successfully refreshed 2");
                            repo.setTimeEvents(new ArrayList<Event>());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        repo.setTimeEvents(new ArrayList<Event>());
                        Log.d("failed Refresh", "we successfully refreshed 1");
                    }
                });
            }
        });
        GridView view = (GridView) v.findViewById(R.id.event_grid);
        events = repo.getTimeEvents();
        adapter = new GridAdapter(getActivity(), events, picasso);
        view.setAdapter(adapter);
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
    public void onRefresh(EventsRepoRefreshed event){
        if(event.getType() == 0) {
            Log.d("TIME FRAG", "Something cool happened in here!");
            events = repo.getTimeEvents();
            adapter.refresh(events);
            swipeLayout.setRefreshing(false);
        }
    }

}

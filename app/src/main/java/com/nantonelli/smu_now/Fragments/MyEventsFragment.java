package com.nantonelli.smu_now.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ndantonelli on 9/26/15.
 */
public class MyEventsFragment extends BaseFragment{

    static TimePageFragmentListener listener;
    private List<Event> events;
    private GridAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    private static final String TAG = "MY_EVENTS_FRAGMENT";

    public static MyEventsFragment newInstance(TimePageFragmentListener listener){
        MyEventsFragment t = new MyEventsFragment();
        t.listener = listener;
        return t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_layout, container, false);
        GridView view = (GridView) v.findViewById(R.id.event_grid);

        //find the swipe refresh layout and put swipe refresh code
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                service.getMyEvents(OasysApplication.getInstance().timeFormat, OasysApplication.getInstance().uid, new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        if (events != null)
                            repo.setMyEvents(events);
                        else
                            repo.setMyEvents(new ArrayList<Event>());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        repo.setMyEvents(new ArrayList<Event>());
                        Log.e(TAG, "Couldn't refresh My Events List", error);
                    }
                });
            }
        });

        //get all of my events from the event repository
        events = repo.getMyEvents();
        adapter = new GridAdapter(getActivity(), events, picasso);

        view.setAdapter(adapter);

        //switch to event fragment when an event image is clicked on
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onSwitchToNextFragment(events.get(position));
                OasysApplication.getInstance().onFirstM = false;
            }
        });
        return v;
    }

    @Subscribe
    // my events in the event repository has been updated
    public void onRefresh(EventsRepoRefreshed event){
        if(event.getType() == 2) {
            events = repo.getMyEvents();
            adapter.refresh(events);
            swipeLayout.setRefreshing(false);
        }
    }

}

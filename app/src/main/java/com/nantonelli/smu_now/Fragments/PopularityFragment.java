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
public class PopularityFragment extends BaseFragment {
    static TimePageFragmentListener listener;
    private List<Event> events;
    private GridAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    private static final String TAG = "POPULARITY_FRAGMENT";

    public static PopularityFragment newInstance(TimePageFragmentListener listener){
        PopularityFragment t = new PopularityFragment();
        t.listener = listener;
        return t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generic_layout, container, false);
        GridView view = (GridView) v.findViewById(R.id.event_grid);

        //refresh on swipe up behavior
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                service.getPopularEvents(OasysApplication.getInstance().timeFormat, OasysApplication.getInstance().popularity, new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        if (events != null)
                            repo.setPopularEvents(events);
                        else
                            repo.setPopularEvents(new ArrayList<Event>());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        repo.setPopularEvents(new ArrayList<Event>());
                        Log.d(TAG, "failed to refresh popular events", error);
                    }
                });
            }
        });

        //get all of the popular events from the repository
        events = repo.getPopularEvents();
        adapter = new GridAdapter(getActivity(), events, picasso);

        //used for switching to event fragment when an event is clicked on
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onSwitchToNextFragment(events.get(position));
                OasysApplication.getInstance().onFirstP = false;
            }
        });
        return v;
    }

    @Subscribe
    //Popularity events have been refreshed
    public void onRefresh(EventsRepoRefreshed event){
        if(event.getType() == 1) {
            events = repo.getPopularEvents();
            adapter.refresh(events);
            swipeLayout.setRefreshing(false);
        }
    }
}

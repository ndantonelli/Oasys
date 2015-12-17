package com.nantonelli.smu_now.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Model.EventsRepo;
import com.nantonelli.smu_now.OasysApplication;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 10/22/15.
 * a base fragment so that we dont always have to do the same things in every fragment
 */
public class BaseFragment extends Fragment {
    @Inject Picasso picasso;
    @Inject EventsRepo repo;
    @Inject Bus eventBus;
    @Inject OasysRestfulAPI service;

    protected android.os.Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new android.os.Handler();
        ((OasysApplication)getActivity().getApplication()).getObjectGraph().inject(this);
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

    public void hide_keyboard_from() {
        View v = getView();
        if(v == null)
            v = new View(getActivity());
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void postEvent(Object object){
        eventBus.post(object);
    }

    public void postEventAsync(final Object object){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eventBus.post(object);
            }
        });
    }
}

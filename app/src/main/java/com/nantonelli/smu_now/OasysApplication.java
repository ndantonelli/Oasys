package com.nantonelli.smu_now;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dagger.ObjectGraph;

/**
 * Created by ndantonelli on 9/29/15.
 */
public class OasysApplication extends Application {
    public boolean onFirst = true;
    private ObjectGraph objectGraph;
    private static OasysApplication myApp;
    public static OasysApplication getInstance(){
        return myApp;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        myApp = this;
        objectGraph = ObjectGraph.create(new OasysModule(getApplicationContext()));
    }
    public ObjectGraph getObjectGraph(){
        return objectGraph;
    }
}

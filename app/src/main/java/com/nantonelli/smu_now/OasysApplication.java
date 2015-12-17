package com.nantonelli.smu_now;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Response.UserResponse;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.io.IOException;

import javax.inject.Inject;

import dagger.ObjectGraph;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ndantonelli on 9/29/15.
 * base application used to hold all info for a specific user
 * holds the user id and the advertising ID along with username and formating
 */
public class OasysApplication extends Application {
    @Inject OasysRestfulAPI service;

    public boolean onFirstT = true;
    public boolean onFirstP = true;
    public boolean onFirstM = true;

    public String adId = "";
    public String username = "";
    public String timeFormat = "";
    public String popularity = "";

    public int uid = 0;

    private ObjectGraph objectGraph;
    private static OasysApplication myApp;

    //allows for non activities/fragments to inject singletons
    public static OasysApplication getInstance(){
        return myApp;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        myApp = this;
        objectGraph = ObjectGraph.create(new OasysModule(getApplicationContext()));
        objectGraph.inject(this);

        //uses preferences to get the user id for the phone
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getInstance());
        uid = prefs.getInt("uid", 0);

        if(uid > 0)
            new GetADIDTask().execute();
    }

    //object graph to inject the singletons into the classes that register
    public ObjectGraph getObjectGraph(){
        return objectGraph;
    }

    //async task to get the Google Advertising ID
    //use the Advertising ID to uniquely identify the user and avoid login
    private class GetADIDTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            AdvertisingIdClient.Info adInfo;
            adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getInstance());
                if (adInfo.isLimitAdTrackingEnabled()) // check if user has opted out of tracking
                    return "did not find ADID ... sorry";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
            if(adInfo == null)
                return "No ADID";
            return adInfo.getId();
        }

        @Override
        protected void onPostExecute(String s) {
            adId = s;

            //if the first load, need to register the device with the server and get a user id
            final SharedPreferences prefs = PreferenceManager
                                            .getDefaultSharedPreferences(getInstance());
            boolean firstLoad = prefs.getBoolean("firstLoad", true);
            if(firstLoad) {
                service.createUser(adId, username, new Callback<UserResponse>() {
                    @Override
                    public void success(UserResponse response, Response response2) {
                        uid = response.getUser_id();
                        prefs.edit().putBoolean("firstLoad", false).putInt("uid", uid).apply();
                        Log.d("success", "we successfully did things");
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }
}

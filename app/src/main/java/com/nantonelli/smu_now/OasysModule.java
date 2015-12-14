package com.nantonelli.smu_now;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Adapters.WeatherAPI;
import com.nantonelli.smu_now.Fragments.CommentFragment;
import com.nantonelli.smu_now.Fragments.MyEventsFragment;
import com.nantonelli.smu_now.Fragments.PopularityFragment;
import com.nantonelli.smu_now.Fragments.TestFrag;
import com.nantonelli.smu_now.Fragments.TimeFragment;
import com.nantonelli.smu_now.Model.EventsRepo;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by ndantonelli on 9/29/15.
 */
@Module(
        injects = {OasysApplication.class, Activity_Home.class, TestFrag.class,EventsRepo.class, TimeFragment.class, MyEventsFragment.class, PopularityFragment.class, CommentFragment.class}
)
public class OasysModule {
        private Context appContext;
        public OasysModule(Context context){
                appContext=context;
        }


        @Singleton
        @Provides
        public Bus getSingle(){
            return new Bus(ThreadEnforcer.MAIN);
        }

        @Singleton
        @Provides
        public EventsRepo getRepo(){
                return new EventsRepo();
        }

        @Singleton
        @Provides
        public Picasso getPicasso(){
                return new Picasso.Builder(appContext).build();
        }

        @Singleton
        @Provides
        public OasysRestfulAPI getRestful(){
                String BASE_URL="http://52.11.80.182/oasys/api/index.php";
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                        .create();
                return new RestAdapter.Builder()
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setEndpoint(BASE_URL)
                        .setConverter(new GsonConverter(gson))
                        .build().create(OasysRestfulAPI.class);
        }

        @Singleton
        @Provides
        public WeatherAPI getWeatherRest(){
                String BASE_URL="http://api.wunderground.com/api/7dbaa219f6da5880/forecast/q/TX";
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                        .create();
                return new RestAdapter.Builder()
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setEndpoint(BASE_URL)
                        .setConverter(new GsonConverter(gson))
                        .build().create(WeatherAPI.class);
        }

}

package com.nantonelli.smu_now;

import android.content.Context;

import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Fragments.TestFrag;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by ndantonelli on 9/29/15.
 */
@Module(
        injects = {Activity_Home.class, TestFrag.class,}
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
        public Picasso getPicasso(){
                return new Picasso.Builder(appContext).build();
        }

//        @Singleton
//        @Provides
//        public OasysRestfulAPI getRestful(){
//                return new Retrofit.Builder()
//                        .baseUrl("https://api.github.com")
//                        .build().create(OasysRestfulAPI.class);
//        }

}

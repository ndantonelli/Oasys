package com.nantonelli.smu_now.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nantonelli.smu_now.Activity_Event;
import com.nantonelli.smu_now.Adapters.TimePageFragmentListener;
import com.nantonelli.smu_now.OasysApplication;
import com.nantonelli.smu_now.R;

/**
 * Created by ndantonelli on 9/26/15.
 */
public class TimeFragment extends Fragment {
    private Button switchButton;
    static TimePageFragmentListener listener;

    public static TimeFragment newInstance(TimePageFragmentListener listener){
        TimeFragment t = new TimeFragment();
        t.listener = listener;
        return t;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_time, container, false);
        switchButton = (Button)V.findViewById(R.id.switch_frag);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSwitchToNextFragment();
                OasysApplication.getInstance().onFirst = false;
            }
        });
        return V;
    }

}

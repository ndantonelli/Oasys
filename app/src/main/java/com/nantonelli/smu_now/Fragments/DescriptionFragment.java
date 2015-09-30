package com.nantonelli.smu_now.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nantonelli.smu_now.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ndantonelli on 9/28/15.
 */
public class DescriptionFragment extends Fragment {
    @Bind(R.id.descriptions)TextView desc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        ButterKnife.bind(this,v);
        desc.setMovementMethod(ScrollingMovementMethod.getInstance());
        return v;
    }

}
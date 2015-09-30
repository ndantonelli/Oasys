package com.nantonelli.smu_now.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nantonelli.smu_now.R;

/**
 * Created by ndantonelli on 9/26/15.
 */
public class PopularityFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popularity, container, false);
    }
}

package com.nantonelli.smu_now.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.R;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ndantonelli on 9/28/15.
 */
public class DescriptionFragment extends Fragment {
    private Event event;
    @Bind(R.id.descriptions)TextView desc;

    public static DescriptionFragment newInstance(Event event){
        DescriptionFragment desc = new DescriptionFragment();
        desc.event = event;
        return desc;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        ButterKnife.bind(this,v);
        desc.setMovementMethod(ScrollingMovementMethod.getInstance());
        if(event != null)
            desc.setText(event.getDescription());
        return v;
    }
    @OnClick(R.id.uber_desc_button)
    public void stuff(){
        try {
            PackageManager pm = getActivity().getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri =
                    "uber://?action=setPickup&pickup=my_location&client_id=8Ql0XvNLma2ufXLgYYB4MsURFuWV_Tpq&dropoff[nickname]="+event.getAddress();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
            String url = "https://m.uber.com/sign-up?client_id=8Ql0XvNLma2ufXLgYYB4MsURFuWV_Tpq";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

}
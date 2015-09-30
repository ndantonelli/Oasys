package com.nantonelli.smu_now.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nantonelli.smu_now.Fragments.CommentFragment;
import com.nantonelli.smu_now.Fragments.DescriptionFragment;
import com.nantonelli.smu_now.Fragments.PopularityFragment;
import com.nantonelli.smu_now.Fragments.TestFrag;
import com.nantonelli.smu_now.Fragments.TimeFragment;

/**
 * Created by ndantonelli on 9/28/15.
 */
public class EventTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public EventTabsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new DescriptionFragment();
            case 1:
                return new CommentFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
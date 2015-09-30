package com.nantonelli.smu_now.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nantonelli.smu_now.Fragments.MyEventsFragment;
import com.nantonelli.smu_now.Fragments.PopularityFragment;
import com.nantonelli.smu_now.Fragments.TestFrag;
import com.nantonelli.smu_now.Fragments.TimeFragment;

import junit.framework.Test;

/**
 * Created by ndantonelli on 9/26/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private final class TimePageListener implements TimePageFragmentListener{
        public void onSwitchToNextFragment(){
            mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commit();
            if(mFragmentAtPos0 instanceof TimeFragment)
                mFragmentAtPos0 = TestFrag.newInstance(listener);
            else
                mFragmentAtPos0 = TimeFragment.newInstance(listener);
            notifyDataSetChanged();
        }
    }

    TimePageListener listener = new TimePageListener();
    private Fragment mFragmentAtPos0;
    private FragmentManager mFragmentManager;
    int mNumOfTabs;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        mFragmentManager = fm;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public int getItemPosition(Object object){
        if (object instanceof TimeFragment && mFragmentAtPos0 instanceof TestFrag)
            return POSITION_NONE;
        if (object instanceof TestFrag && mFragmentAtPos0 instanceof TimeFragment)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(mFragmentAtPos0 == null)
                    mFragmentAtPos0= TimeFragment.newInstance(listener);
                return mFragmentAtPos0;
            case 1:
                return new PopularityFragment();
            case 2:
                return new PopularityFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
package com.nantonelli.smu_now.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.nantonelli.smu_now.Fragments.MyEventsFragment;
import com.nantonelli.smu_now.Fragments.PopularityFragment;
import com.nantonelli.smu_now.Fragments.TestFrag;
import com.nantonelli.smu_now.Fragments.TimeFragment;
import com.nantonelli.smu_now.Model.Event;

import junit.framework.Test;

/**
 * Created by ndantonelli on 9/26/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private final class TimePageListener implements TimePageFragmentListener{
        public void onSwitchToNextFragment(Event event){
            mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commit();
            if(mFragmentAtPos0 instanceof TimeFragment)
                mFragmentAtPos0 = TestFrag.newInstance(listener0, event, 0);
            else
                mFragmentAtPos0 = TimeFragment.newInstance(listener0);
            notifyDataSetChanged();
        }
    }
    private final class PopPageListener implements TimePageFragmentListener{
        public void onSwitchToNextFragment(Event event){
            mFragmentManager.beginTransaction().remove(mFragmentAtPos1).commit();
            if(mFragmentAtPos1 instanceof PopularityFragment)
                mFragmentAtPos1 = TestFrag.newInstance(listener1, event, 1);
            else
                mFragmentAtPos1 = PopularityFragment.newInstance(listener1);
            notifyDataSetChanged();
        }
    }
    private final class MyPageListener implements TimePageFragmentListener{
        public void onSwitchToNextFragment(Event event){
            mFragmentManager.beginTransaction().remove(mFragmentAtPos2).commit();
            Log.d("remove", "removed fragment at position 2");
            if(mFragmentAtPos2 instanceof MyEventsFragment) {
                mFragmentAtPos2 = TestFrag.newInstance(listener2, event, 2);
                Log.d("add", "added test fragment at position 2");
            }
            else
                mFragmentAtPos2 = MyEventsFragment.newInstance(listener2);
            notifyDataSetChanged();
        }
    }

    TimePageListener listener0 = new TimePageListener();
    PopPageListener listener1 = new PopPageListener();
    MyPageListener listener2 = new MyPageListener();
    private Fragment mFragmentAtPos0;
    private Fragment mFragmentAtPos1;
    private Fragment mFragmentAtPos2;
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
        if (object instanceof PopularityFragment && mFragmentAtPos1 instanceof TestFrag)
            return POSITION_NONE;
        if (object instanceof TestFrag && mFragmentAtPos1 instanceof PopularityFragment)
            return POSITION_NONE;
        if (object instanceof MyEventsFragment && mFragmentAtPos2 instanceof TestFrag)
            return POSITION_NONE;
        if (object instanceof TestFrag && mFragmentAtPos2 instanceof MyEventsFragment)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(mFragmentAtPos0 == null)
                    mFragmentAtPos0= TimeFragment.newInstance(listener0);
                return mFragmentAtPos0;
            case 1:
                if(mFragmentAtPos1 == null)
                    mFragmentAtPos1= PopularityFragment.newInstance(listener1);
                return mFragmentAtPos1;
            case 2:
                if(mFragmentAtPos2 == null)
                    mFragmentAtPos2= MyEventsFragment.newInstance(listener2);
                return mFragmentAtPos2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
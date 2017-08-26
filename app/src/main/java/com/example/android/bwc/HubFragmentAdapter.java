package com.example.android.bwc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by TEST on 7/26/2017.
 */

public class HubFragmentAdapter extends FragmentPagerAdapter {

    public HubFragmentAdapter (FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
        return new HubFragment();
    }
    else {
            return new HubSearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Children";
        }
        else {
            return "Hubs";
        }
    }
}

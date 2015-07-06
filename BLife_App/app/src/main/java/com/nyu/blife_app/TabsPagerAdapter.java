package com.nyu.blife_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Yeshwant on 04/04/2015.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabs = { "Edit Profile", "Edit Notification"};
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new EditProfileActivity();
            case 1:
                // Games fragment activity
                return new EditNotificationActivity();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }


}
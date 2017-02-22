package com.example.oliverasker.skywarnmarkii;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.oliverasker.skywarnmarkii.Fragments.UserInfoHomeFragment;

/**
 * Created by oliverasker on 1/19/17.
 * Resource: http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumTabs;

    public PagerAdapter(FragmentManager fm, int NumTabs){
        super(fm);
        this.mNumTabs = NumTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
              UserInfoHomeFragment userInfoHomeFragment = new UserInfoHomeFragment();
                return userInfoHomeFragment;


            /*
            case 1:
                QueryLauncherActivity qL = new QueryLauncherActivity();


                break;
            case 2:
                break;
                */
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

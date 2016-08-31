package com.iosoft.hidayat.personalfinance.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iosoft.hidayat.personalfinance.FragmentCategoryIn;
import com.iosoft.hidayat.personalfinance.FragmentCategoryOut;

/**
 * Created by hidayat on 30/08/16.
 */
public class AdapterCategoryChoice extends FragmentStatePagerAdapter{

    int mNumOfTabs;

    public AdapterCategoryChoice(FragmentManager fm, int NumOfTabs){

        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                FragmentCategoryOut catOut = new FragmentCategoryOut();
                return catOut;
            case 1:
                FragmentCategoryIn catIn = new FragmentCategoryIn();
                return catIn;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

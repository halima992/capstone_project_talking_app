package com.example.halima.talkingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> TheFragment= new ArrayList<>();
    private final List<String> TheTitle= new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TheFragment.get(position);
    }

    @Override
    public int getCount() {
        return TheTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TheTitle.get(position);
    }
    public void addFragment(Fragment fragment,String title){
        TheFragment.add(fragment);
        TheTitle.add(title);
    }
}

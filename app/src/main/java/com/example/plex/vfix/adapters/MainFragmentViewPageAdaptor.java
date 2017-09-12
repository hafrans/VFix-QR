package com.example.plex.vfix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.plex.vfix.fragments.CurrentFragment;

import java.util.List;

/**
 * Created by Plex on 2017/9/8.
 */

public class MainFragmentViewPageAdaptor extends FragmentPagerAdapter {

    public List<Fragment> list = null;
    public List<String>   titles = null;


    public MainFragmentViewPageAdaptor(List<String> titles,List<Fragment> list, FragmentManager fm) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
//        return new CurrentFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

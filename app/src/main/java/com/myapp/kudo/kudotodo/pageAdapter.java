package com.myapp.kudo.kudotodo;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class pageAdapter extends FragmentPagerAdapter {

    public pageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return TestFragment1.newInstance(null,null);
            case 1:
                return TestFragment.newInstance(null,null);
            default:
                return TestFragment2.newInstance(null,null);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "TODOリスト";
            case 1:
                return "TODO登録";
            default:
                return "TODOメモ";
        }
    }

    public Fragment findFragmentByPosition(ViewPager viewPager,
                                           int position) {
        return (Fragment) instantiateItem(viewPager, position);
    }
}

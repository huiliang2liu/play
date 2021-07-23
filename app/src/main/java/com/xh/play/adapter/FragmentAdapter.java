package com.xh.play.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


/**
 * com.tvblack.lamp.adapter
 * 2019/2/28 12:54
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public FragmentAdapter(FragmentActivity activity, List<Fragment> fragments) {
        // TODO Auto-generated constructor stub
        super(activity.getSupportFragmentManager());
        this.fragments = fragments;
    }

    public FragmentAdapter(Fragment fragment, List<Fragment> fragments) {
        // TODO Auto-generated constructor stub
        super(fragment.getChildFragmentManager());
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return fragments.get(arg0);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return fragments == null ? 0 : fragments.size();
    }
}

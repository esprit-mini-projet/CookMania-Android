package tn.duoes.esprit.cookmania.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tn.duoes.esprit.cookmania.controllers.fragments.HomeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.ProfileFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.SearchFragment;

public class MainScreenPagerAdapter extends FragmentStatePagerAdapter {
    private int mTabsCount;

    public MainScreenPagerAdapter(FragmentManager fm, int tabsCount) {
        super(fm);
        this.mTabsCount = tabsCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = HomeFragment.newInstance(null, null);
                break;
            case 1:
                fragment = SearchFragment.newInstance(null, null);
                break;
            case 2:
                fragment = ProfileFragment.newInstance(null, null);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabsCount;
    }
}

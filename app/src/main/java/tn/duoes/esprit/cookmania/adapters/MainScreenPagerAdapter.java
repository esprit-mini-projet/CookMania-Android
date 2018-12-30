package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.AddRecipeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.HomeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.ProfileFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.SearchFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.ShoppingListFragment;

public class MainScreenPagerAdapter extends FragmentStatePagerAdapter {
    private int mTabsCount;
    private Context mContext;

    public MainScreenPagerAdapter(Context context, FragmentManager fm, int tabsCount) {
        super(fm);
        this.mTabsCount = tabsCount;
        mContext = context;
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
                fragment = AddRecipeFragment.newInstance(null, null);
                break;
            case 3:
                fragment = ShoppingListFragment.newInstance(null, null);
                break;
            case 4:
                String connectedUserId = mContext.getSharedPreferences(mContext.getString(R.string.prefs_name), Context.MODE_PRIVATE)
                        .getString(mContext.getString(R.string.prefs_user_id), null);
                fragment = ProfileFragment.newInstance(connectedUserId);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabsCount;
    }

}

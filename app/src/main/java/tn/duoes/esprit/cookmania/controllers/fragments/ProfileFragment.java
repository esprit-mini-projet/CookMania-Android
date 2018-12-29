package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ProfilePagerAdapter;
import tn.duoes.esprit.cookmania.controllers.activities.SettingsActivity;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class ProfileFragment extends Fragment {

    public static final String ARG_USER_ID = "user_id";

    private ProfileHeaderFragment mProfileHeaderFragment;
    private ProfileRecipeListFragment mRecipeListFragment;

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final SwipeRefreshLayout swipeRefreshLayout =  view.findViewById(R.id.fragment_profile_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProfileHeaderFragment.update();
                mRecipeListFragment.update();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        String userId = getArguments().getString(ARG_USER_ID);
        TabLayout tabLayout = view.findViewById(R.id.fragment_profile_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.fragment_profile_view_pager);
        List<Fragment> fragments = new ArrayList<>();
        mRecipeListFragment = ProfileRecipeListFragment.newInstance(userId);
        fragments.add(mRecipeListFragment);
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager, true);
       return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String connectedUserId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        String userId = getArguments().getString(ARG_USER_ID);
        setHasOptionsMenu(userId.equals(connectedUserId));
        mProfileHeaderFragment = ProfileHeaderFragment.newInstance(userId);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_profile_header_container, mProfileHeaderFragment)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings:
                startActivity(NavigationUtils.getNavigationFormattedIntent(getContext(), SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

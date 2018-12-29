package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.SettingsActivity;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class ProfileFragment extends Fragment {

    private ProfileHeaderFragment mProfileHeaderFragment;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final SwipeRefreshLayout swipeRefreshLayout =  view.findViewById(R.id.fragment_profile_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProfileHeaderFragment.update();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
       return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        String userId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE).getString(getString(R.string.prefs_user_id), null);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

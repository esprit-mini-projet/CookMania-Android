package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
       return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        String userId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE).getString(getString(R.string.prefs_user_id), null);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_profile_header_container, ProfileHeaderFragment.newInstance(userId))
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

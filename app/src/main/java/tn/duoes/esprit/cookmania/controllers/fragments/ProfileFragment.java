package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import tn.duoes.esprit.cookmania.services.UserService;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class ProfileFragment extends Fragment implements UserService.IsFollowingCallBack {

    private static final String TAG = "ProfileFragment";
    public static final String ARG_USER_ID = "user_id";

    private ProfileHeaderFragment mProfileHeaderFragment;
    private ProfileRecipeListFragment mRecipeListFragment;
    private ProfileFavoriteListFragment mFavoriteListFragment;
    private ProfileUserListFragment mFollowingListFragment;
    private ProfileUserListFragment mFollowerListFragment;

    private boolean mIsFollowing;
    private String mConnectedUserId;
    private String mUserId;

    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TabLayout tabLayout = view.findViewById(R.id.fragment_profile_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.fragment_profile_view_pager);

        String userId = getArguments().getString(ARG_USER_ID);
        String connectedUserId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);

        List<Fragment> fragments = new ArrayList<>();
        List<String> tabTitles = new ArrayList<>();
        mRecipeListFragment = ProfileRecipeListFragment.newInstance(userId);
        fragments.add(mRecipeListFragment);
        tabTitles.add(getString(R.string.recipes));
        if (userId.equals(connectedUserId)) {
            mFavoriteListFragment = ProfileFavoriteListFragment.newInstance();
            fragments.add(mFavoriteListFragment);
            tabTitles.add(getString(R.string.favorites));
        }
        mFollowingListFragment = ProfileUserListFragment.newInstance(userId, false);
        mFollowerListFragment = ProfileUserListFragment.newInstance(userId, true);
        fragments.add(mFollowingListFragment);
        tabTitles.add(getString(R.string.following));
        fragments.add(mFollowerListFragment);
        tabTitles.add(getString(R.string.followers));
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getChildFragmentManager(), fragments, tabTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectedUserId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        mUserId = getArguments().getString(ARG_USER_ID);
        if (mUserId.equals(mConnectedUserId)) {
            setHasOptionsMenu(true);
        } else {
            setHasOptionsMenu(false);
            UserService.getInstance().isFollowing(mConnectedUserId, mUserId, this);
        }
        mProfileHeaderFragment = ProfileHeaderFragment.newInstance(mUserId);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_profile_header_container, mProfileHeaderFragment)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mUserId.equals(mConnectedUserId)) {
            inflater.inflate(R.menu.menu_profile, menu);
        } else {
            inflater.inflate(R.menu.menu_other_profile, menu);
            menu.getItem(0).setTitle(mIsFollowing ? R.string.unfollow : R.string.follow);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings:
                startActivity(NavigationUtils.getNavigationFormattedIntent(getContext(), SettingsActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.menu_follow:
                if (mIsFollowing) {
                    mIsFollowing = false;
                    item.setTitle(R.string.follow);
                    mProfileHeaderFragment.updateFollowers(-1);
                    UserService.getInstance().unfollow(mConnectedUserId, mUserId, new UserService.FollowCallBack() {
                        @Override
                        public void onCompletion(Boolean result) {
                            if (!result) {
                                item.setTitle(R.string.unfollow);
                                mIsFollowing = true;
                                mProfileHeaderFragment.updateFollowers(1);
                            }
                            if (mFollowerListFragment.isResumed()) mFollowerListFragment.update();
                        }
                    });
                } else {
                    mIsFollowing = true;
                    item.setTitle(R.string.unfollow);
                    mProfileHeaderFragment.updateFollowers(1);
                    UserService.getInstance().follow(mConnectedUserId, mUserId, new UserService.FollowCallBack() {
                        @Override
                        public void onCompletion(Boolean result) {
                            if (!result) {
                                item.setTitle(R.string.follow);
                                mIsFollowing = false;
                                mProfileHeaderFragment.updateFollowers(-1);
                            }
                            if (mFollowerListFragment.isResumed()) mFollowerListFragment.update();
                        }
                    });
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCompletion(Boolean isFollowing) {
        if (isFollowing == null) return;
        mIsFollowing = isFollowing;
        setHasOptionsMenu(true);
    }
}

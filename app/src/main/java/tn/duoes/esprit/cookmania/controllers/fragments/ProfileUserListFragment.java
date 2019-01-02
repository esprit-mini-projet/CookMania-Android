package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ProfileUserListAdapter;
import tn.duoes.esprit.cookmania.controllers.activities.ProfileActivity;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.UserService;

public class ProfileUserListFragment extends Fragment implements ProfileUserListAdapter.UserViewHolder.UserItemCallBack, UserService.GetUsersCallBack {

    private static final String TAG = "ProfileUserListFrag";
    public static final String ARG_USER_ID = "user_id";
    public static final String ARG_IS_FOLLOWERS = "is_followers";
    private RecyclerView mRecyclerView;
    private List<User> mUsers = new ArrayList<>();
    private ProfileUserListAdapter mAdapter;
    private TextView mEmptyText;

    public static ProfileUserListFragment newInstance(String userId, boolean isFollowers) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_FOLLOWERS, isFollowers);
        ProfileUserListFragment fragment = new ProfileUserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user_followers, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_profile_user_followers_list);
        mEmptyText = view.findViewById(R.id.fragment_profile_user_followers_empty_text);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProfileUserListAdapter(mUsers, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        String userId = getArguments().getString(ARG_USER_ID);
        boolean isFollowers = getArguments().getBoolean(ARG_IS_FOLLOWERS);
        if (isFollowers) {
            mEmptyText.setText(R.string.no_followers);
            UserService.getInstance().getFollowers(userId, this);
        } else {
            mEmptyText.setText(R.string.no_following);
            UserService.getInstance().getFollowing(userId, this);
        }
    }

    @Override
    public void onResponse(List<User> users) {
        if (users == null || users.isEmpty()) {
            mEmptyText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        mUsers.clear();
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
        mEmptyText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure() {
        Log.i(TAG, "onFailure: ");
        mEmptyText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onUserItemClicked(String userId) {
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        i.putExtra(ProfileActivity.EXTRA_USER_ID, userId);
        startActivity(i);
    }
}

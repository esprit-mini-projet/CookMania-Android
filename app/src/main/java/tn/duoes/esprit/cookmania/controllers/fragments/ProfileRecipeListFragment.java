package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
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
import tn.duoes.esprit.cookmania.adapters.ProfileRecipeListAdapter;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;

public class ProfileRecipeListFragment extends Fragment implements RecipeService.RecipeServiceGetCallBack, ProfileRecipeListAdapter.RecipeViewHolder.RecipeItemCallBack {

    private static final String TAG = "ProfileRecipeListFrag";
    public static final String ARG_USER_ID = "user_id";
    private RecyclerView mRecyclerView;
    private List<Recipe> mRecipes = new ArrayList<>();
    private ProfileRecipeListAdapter mAdapter;
    private TextView mEmptyText;

    public static ProfileRecipeListFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        ProfileRecipeListFragment fragment = new ProfileRecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user_recipes, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_profile_user_recipe_list);
        mEmptyText = view.findViewById(R.id.fragment_profile_user_recipe_empty_text);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProfileRecipeListAdapter(mRecipes, getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        String userId = getArguments().getString(ARG_USER_ID);
        String connectedUserId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        if (!userId.equals(connectedUserId)) {
            mEmptyText.setText(R.string.no_recipes_found);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onResponse(List<Recipe> recipes) {
        Log.i(TAG, "onResponse: size: " + recipes.size());
        if(recipes.size() == 0){
            mEmptyText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        mRecipes.clear();
        mRecipes.addAll(recipes);
        mAdapter.notifyDataSetChanged();
        mEmptyText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure() {
        mEmptyText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeItemClicked(int recipeId) {
        Intent i = new Intent(getActivity(), RecipeDetailsActivity.class);
        i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, "" + recipeId);
        i.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, true);
        startActivity(i);
    }

    public void update(){
        String userId = getArguments().getString(ARG_USER_ID);
        RecipeService.getInstance().getRecipesByUser(userId, this);
    }
}

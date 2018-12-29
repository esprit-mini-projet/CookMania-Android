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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String userId = getArguments().getString(ARG_USER_ID);
        RecipeService.getInstance().getRecipesByUser(userId, this);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onResponse(List<Recipe> recipes) {
        Log.i(TAG, "onResponse: size: " + recipes.size());
        if(recipes.size() == 0){
            getView().findViewById(R.id.fragment_profile_user_recipe_empty_text).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        ProfileRecipeListAdapter adapter = new ProfileRecipeListAdapter(recipes, getActivity(), this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onFailure() {
        Log.i(TAG, "onFailure: ");
        getView().findViewById(R.id.fragment_profile_user_recipe_empty_text).setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeItemClicked(int recipeId) {
        Intent i = new Intent(getActivity(), RecipeDetailsActivity.class);
        i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, "" + recipeId);
        i.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, true);
        startActivity(i);
    }
}

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

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ProfileRecipeListAdapter;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.dao.FavoriteLab;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;

public class ProfileFavoriteListFragment extends Fragment implements RecipeService.RecipeServiceGetCallBack, ProfileRecipeListAdapter.RecipeViewHolder.RecipeItemCallBack {

    private static final String TAG = "ProfileFavoriteListFr";
    private RecyclerView mRecyclerView;
    private List<Recipe> mRecipes = new ArrayList<>();
    private ProfileRecipeListAdapter mAdapter;
    private Context mContext;
    private View mEmptyText;

    public static ProfileFavoriteListFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFavoriteListFragment fragment = new ProfileFavoriteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user_recipes, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_profile_user_recipe_list);
        mEmptyText = view.findViewById(R.id.fragment_profile_user_recipe_empty_text);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ProfileRecipeListAdapter(mRecipes, mContext, this);
        mRecyclerView.setAdapter(mAdapter);
        Log.i(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResponse(List<Recipe> recipes) {
        if (recipes.size() == 0) {
            return;
        }
        mRecipes.clear();
        mRecipes.addAll(recipes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure() {
        mEmptyText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeItemClicked(int recipeId) {
        Intent i = new Intent(mContext, RecipeDetailsActivity.class);
        i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, "" + recipeId);
        i.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, true);
        startActivity(i);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void update() {
        String userId = mContext.getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), "");
        List<Integer> favorites = FavoriteLab.getInstance(mContext).getList(userId);
        if (favorites.size() == 0) {
            mEmptyText.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mRecipes.clear();
            return;
        }
        mRecipes.clear();
        getRecipes(favorites);
    }

    private void getRecipes(final List<Integer> favorites) {
        if (favorites.isEmpty()) {
            mAdapter.notifyDataSetChanged();
            mEmptyText.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            return;
        }
        final int recipeId = favorites.remove(0);
        RecipeService.getInstance().getRecipeById(recipeId + "", new RecipeService.RecipeServiceGetCallBack() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                mRecipes.addAll(recipes);
                getRecipes(favorites);
            }

            @Override
            public void onFailure() {
                getRecipes(favorites);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}

package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.CategoryRecipiesRecyclerViewAdapter;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class CategoryRecipesActivity extends AppCompatActivity {

    private static final String TAG = CategoryRecipesActivity.class.getSimpleName();
    public static final String CATEGORY_NAME_KEY  = "categoryName";

    private RecyclerView mRecyclerView;
    private CategoryRecipiesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_recipes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String categoryName = getIntent().getStringExtra(CATEGORY_NAME_KEY);
        getSupportActionBar().setTitle(categoryName);

        mRecyclerView = findViewById(R.id.category_recipes_rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CategoryRecipiesRecyclerViewAdapter(new ArrayList<Recipe>());
        mRecyclerView.setAdapter(mAdapter);

        RecipeService.getInstance().getAllRecipesByLabel(categoryName.split("\\s+")[0], new RecipeService.RecipeServiceGetCallBack() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                mAdapter.mRecipes = recipes;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "onFailure: ", new Exception("Getting recipes from web service failed!"));
            }
        });
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return NavigationUtils.getParentActivityIntent(this, getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");/*
        InternetConnectivityObserver.get().start(new InternetConnectivityObserver.Consumer() {
            @Override
            public void accept(boolean isConnected) {
                if(!isConnected) startActivity(new Intent(CategoryRecipesActivity.this, ShoppingListActivity.class));
            }
        });*/
    }

    @Override
    protected void onStop() {
        super.onStop();/*
        InternetConnectivityObserver.get().stop();/*
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");/*
        InternetConnectivityObserver.get().stop();*/
    }
}

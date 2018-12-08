package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.RatingPagerAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsIngredientsAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingBarFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingCommentFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingPhotoFragment;
import tn.duoes.esprit.cookmania.dao.FavoriteLab;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailsActivity";
    public static final String EXTRA_RECIPE_ID = "recipeId";

    private ImageView mRecipeImageView;
    private AppCompatRatingBar mRatingInfoBar;
    private TextView mNameTextView;
    private TextView mIngredientsNumberTextView;
    private TextView mCaloriesTextView;
    private TextView mServingsTextView;
    private TextView mTimeTextView;
    private TextView mDescriptionTextView;
    private ImageButton mAddAllButton;
    private TextView mAddAllTextView;
    private TextView mShopItemsTextView;
    private ImageView mShopCartImageView;
    private RecyclerView mIngredientList;
    private RecyclerView mStepList;
    private ViewPager mRatingViewPager;
    private TabLayout mRatingTabLayout;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecipe = new Recipe();
        getViewReferences();
        setupIngredientList();
        setupStepList();
        setupViewPager();
        getRecipe(getIntent().getStringExtra(EXTRA_RECIPE_ID));
    }

    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(RatingBarFragment.newInstance());
        fragments.add(RatingPhotoFragment.newInstance());
        fragments.add(RatingCommentFragment.newInstance());
        RatingPagerAdapter adapter = new RatingPagerAdapter(getSupportFragmentManager(), fragments);
        mRatingViewPager.setAdapter(adapter);
        mRatingTabLayout.setupWithViewPager(mRatingViewPager, true);
    }

    private void setupStepList() {
        RecipeDetailsStepAdapter adapter = new RecipeDetailsStepAdapter(mRecipe.getSteps());
        mStepList.setLayoutManager(new LinearLayoutManager(this));
        mStepList.setAdapter(adapter);
    }

    private void setupIngredientList() {
        RecipeDetailsIngredientsAdapter adapter = new RecipeDetailsIngredientsAdapter(this, mRecipe.getIngredients());
        mIngredientList.setLayoutManager(new LinearLayoutManager(this));
        mIngredientList.setAdapter(adapter);
    }

    private void getViewReferences() {
        mRecipeImageView = findViewById(R.id.details_recipe_image_view);
        mRatingInfoBar = findViewById(R.id.details_recipe_rating_info_bar);
        mNameTextView = findViewById(R.id.details_recipe_name_text);
        mIngredientsNumberTextView = findViewById(R.id.details_info_ingredients_text);
        mCaloriesTextView = findViewById(R.id.details_info_calories_text);
        mServingsTextView = findViewById(R.id.details_info_servings_text);
        mTimeTextView = findViewById(R.id.details_recipe_time_text);
        mDescriptionTextView = findViewById(R.id.details_recipe_description_text);
        mAddAllButton = findViewById(R.id.details_recipe_ingredients_add_all_button);
        mAddAllTextView = findViewById(R.id.details_recipe_ingredients_add_all_text);
        mShopItemsTextView = findViewById(R.id.details_recipe_shop_items_text);
        mShopCartImageView = findViewById(R.id.details_recipe_shop_cart_image);
        mIngredientList = findViewById(R.id.details_recipe_ingredients_recycler);
        mStepList = findViewById(R.id.details_recipe_steps_recycler);
        mRatingViewPager = findViewById(R.id.details_recipe_rating_viewpager);
        mRatingTabLayout = findViewById(R.id.details_recipe_rating_tab_layout);
    }

    private void updateUI(){
        GlideApp.with(this).load(Constants.UPLOAD_FOLDER_URL + "/" + mRecipe.getImageURL()).into(mRecipeImageView);
        mRatingInfoBar.setRating(mRecipe.getRating());
        mIngredientsNumberTextView.setText("" + mRecipe.getIngredients().size());
        mCaloriesTextView.setText("" + mRecipe.getCalories());
        mServingsTextView.setText("" + mRecipe.getServings());
        mTimeTextView.setText("" + mRecipe.getTime() + "'");
        mNameTextView.setText(mRecipe.getName());
        mDescriptionTextView.setText(mRecipe.getDescription());
        ((RecipeDetailsStepAdapter)mStepList.getAdapter()).setSteps(mRecipe.getSteps());
        mStepList.getAdapter().notifyDataSetChanged();
        ((RecipeDetailsIngredientsAdapter)mIngredientList.getAdapter()).setIngredients(mRecipe.getIngredients());
        mIngredientList.getAdapter().notifyDataSetChanged();
    }

    private void getRecipe(String id) {
        RecipeService.getInstance().getRecipeById(id, new RecipeService.RecipeServiceGetCallBack() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                mRecipe = recipes.get(0);
                Log.d(TAG, "onResponse: " + mRecipe);
                updateUI();
            }

            @Override
            public void onFailure() {
                new AlertDialog.Builder(RecipeDetailsActivity.this)
                        .setTitle("Sorry")
                        .setMessage("An error has occured with the server.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_recipe_details, menu);
        MenuItem item = menu.getItem(0);
        String userId = getSharedPreferences(getResources().getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getResources().getString(R.string.prefs_user_id), null);
        int recipeId = Integer.parseInt(getIntent().getStringExtra(EXTRA_RECIPE_ID));
        if(!FavoriteLab.getInstance(this).recipeExists(userId, recipeId)){
            item.setTitle(R.string.favorite);
            item.setIcon(R.drawable.icon_heart_full);
        }else{
            item.setTitle(R.string.remove_favorite);
            item.setIcon(R.drawable.icon_heart_outline);
        }
        Log.d(TAG, "onCreateOptionsMenu: " + FavoriteLab.getInstance(this).getList(userId));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_favorite){
            String userId = getSharedPreferences(getResources().getString(R.string.prefs_name), MODE_PRIVATE)
                    .getString(getResources().getString(R.string.prefs_user_id), null);
            int recipeId = Integer.parseInt(getIntent().getStringExtra(EXTRA_RECIPE_ID));

            if(item.getTitle() == getResources().getString(R.string.favorite)){
                item.setTitle(R.string.remove_favorite);
                item.setIcon(R.drawable.icon_heart_outline);
                FavoriteLab.getInstance(this).insert(recipeId, userId);
            }else{
                item.setTitle(R.string.favorite);
                item.setIcon(R.drawable.icon_heart_full);
                FavoriteLab.getInstance(this).delete(recipeId, userId);
            }
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return NavigationUtils.getParentActivityIntent(this, getIntent());
    }
}

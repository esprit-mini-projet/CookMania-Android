package tn.duoes.esprit.cookmania.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsIngredientsAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

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

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mRecipe = new Recipe();
        getViewReferences();
        setupIngredientList();
        setupStepList();
        getRecipe(getIntent().getStringExtra(EXTRA_RECIPE_ID));
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
    }

    private void updateUI(){
        GlideApp.with(this).load(Constants.UPLOAD_FOLDER_URL + "/" + mRecipe.getImageURL()).centerCrop().into(mRecipeImageView);
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
        RecipeService.getInstance().getRecipeById(id, new RecipeService.RecipeServiceCallBack() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                mRecipe = recipes.get(0);
                Log.d(TAG, "onResponse: " + mRecipe);
                updateUI();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}

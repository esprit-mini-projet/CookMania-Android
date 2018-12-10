package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.RatingPagerAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsIngredientsAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingBarDoneFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingBarFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingCommentFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.RatingPhotoFragment;
import tn.duoes.esprit.cookmania.dao.FavoriteLab;
import tn.duoes.esprit.cookmania.models.Experience;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.ExperienceService;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;
import tn.duoes.esprit.cookmania.views.RatingViewPager;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RatingBarFragment.RatingBarCallBack,
        RatingCommentFragment.RatingCommentCallBack,
        RatingPhotoFragment.RatingPhotoCallBack,
        RatingBarDoneFragment.RatingBarDoneCallBack,
        ExperienceService.AddExperienceCallBack,
        ExperienceService.DeleteExperienceCallBack,
        ExperienceService.GetExperienceCallBack
{

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
    private RatingViewPager mRatingViewPager;
    private TabLayout mRatingTabLayout;

    private Recipe mRecipe;
    private int mRating;
    private String mRatingImagePath;
    private String mComment;
    private String mUserId;
    private List<Fragment> mRatingFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserId = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        mRecipe = new Recipe();
        getViewReferences();
        setupIngredientList();
        setupStepList();
        getRecipe(getIntent().getStringExtra(EXTRA_RECIPE_ID));
    }

    private void setupViewPager(Experience experience) {
        mRatingFragments = new ArrayList<>();
        Fragment ratingBarFragment = null;
        if(experience == null){
            ratingBarFragment = RatingBarFragment.newInstance(this);
        }else{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date = df.format(experience.getDate());
            ratingBarFragment = RatingBarDoneFragment.newInstance(
                    experience.getRating(),
                    getString(R.string.rated_on) + " " + date,
                    this);
        }
        mRatingFragments.add(ratingBarFragment);
        mRatingFragments.add(RatingPhotoFragment.newInstance(this));
        mRatingFragments.add(RatingCommentFragment.newInstance(this));
        RatingPagerAdapter adapter = new RatingPagerAdapter(getSupportFragmentManager(), mRatingFragments);
        mRatingViewPager.setPagingEnabled(false);
        mRatingViewPager.setAdapter(adapter);
        mRatingTabLayout.setupWithViewPager(mRatingViewPager, true);
        mRatingTabLayout.setVisibility(View.INVISIBLE);
    }

    private void getCurrentExperience() {
        ExperienceService.getInstance().getExperience(mRecipe.getId(), mUserId, this);
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
        getCurrentExperience();
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
        }else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSubmitClickListener(boolean canSwipe) {
        if (canSwipe){
            mRatingViewPager.setPagingEnabled(true);
            mRatingTabLayout.setVisibility(View.VISIBLE);
            mRatingViewPager.setCurrentItem(1, true);
        }else{
            mRatingViewPager.setPagingEnabled(false);
            mRatingTabLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRatingChangedListener(int rating) {
        mRating = rating;
    }

    @Override
    public void onImageChangedListener(String path) {
        mRatingImagePath = path;
    }

    @Override
    public void onFinishButtonClickListener(String comment) {
        mComment = comment;
        addExperience();
    }

    private void addExperience() {
        Experience experience = new Experience();
        experience.setComment(mComment);
        experience.setRating(mRating);
        experience.setRecipeId(mRecipe.getId());
        experience.setUserId(mUserId);
        ExperienceService.getInstance().addExperience(experience, mRatingImagePath, this);
    }

    @Override
    public void onAddExperienceSuccess() {
        Snackbar.make(findViewById(R.id.activity_recipe_details_coordinator_layout),
                R.string.experience_added,
                Snackbar.LENGTH_LONG)
                .show();

        mRatingFragments.remove(0);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = df.format(new Date());
        mRatingFragments.add(0, RatingBarDoneFragment.newInstance(mRating,
                getString(R.string.rated_on) + " " + date,
                this));
        mRatingViewPager.setCurrentItem(0, true);
        mRatingViewPager.getAdapter().notifyDataSetChanged();
        mRatingTabLayout.setVisibility(View.INVISIBLE);
        mRatingViewPager.setPagingEnabled(false);
    }

    @Override
    public void onAddExperienceFailure() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_recipe_details_coordinator_layout),
                R.string.experience_not_added,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExperience();
            }
        });
        snackbar.show();
    }

    @Override
    public void onDeleteExperienceClickListener() {
        ExperienceService.getInstance().deleteExperience(mRecipe.getId(), mUserId, this);
    }

    @Override
    public void onDeleteExperienceSuccess() {
        mRatingFragments.remove(0);
        mRatingFragments.add(0, RatingBarFragment.newInstance(this));
        mRatingViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDeleteExperienceFailure() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_recipe_details_coordinator_layout),
                R.string.experience_not_deleted,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteExperienceClickListener();
            }
        });
        snackbar.show();
    }

    @Override
    public void onGetExperienceSuccess(Experience experience) {
        setupViewPager(experience);
    }

    @Override
    public void onGetExperienceFailure() {
        mRatingViewPager.setVisibility(View.GONE);
    }
}

package com.esprit.cookmania.controllers.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.esprit.cookmania.R;
import com.esprit.cookmania.adapters.ExperienceListAdapter;
import com.esprit.cookmania.adapters.RatingPagerAdapter;
import com.esprit.cookmania.adapters.RecipeDetailsIngredientsAdapter;
import com.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import com.esprit.cookmania.adapters.SimilarListAdapter;
import com.esprit.cookmania.controllers.activities.MainScreenActivity;
import com.esprit.cookmania.controllers.activities.ProfileActivity;
import com.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import com.esprit.cookmania.controllers.activities.ShoppingListActivity;
import com.esprit.cookmania.dao.FavoriteLab;
import com.esprit.cookmania.dao.ShoppingListDAO;
import com.esprit.cookmania.models.Experience;
import com.esprit.cookmania.models.Ingredient;
import com.esprit.cookmania.models.Recipe;
import com.esprit.cookmania.models.User;
import com.esprit.cookmania.services.ExperienceService;
import com.esprit.cookmania.services.RecipeService;
import com.esprit.cookmania.services.UserService;
import com.esprit.cookmania.utils.Constants;
import com.esprit.cookmania.utils.GlideApp;
import com.esprit.cookmania.views.RatingViewPager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class RecipeDetailsFragment extends Fragment
        implements RatingBarFragment.RatingBarCallBack,
        RatingCommentFragment.RatingCommentCallBack,
        RatingPhotoFragment.RatingPhotoCallBack,
        RatingBarDoneFragment.RatingBarDoneCallBack,
        ExperienceService.AddExperienceCallBack,
        ExperienceService.DeleteExperienceCallBack,
        ExperienceService.GetExperienceCallBack,
        ExperienceService.GetExperiencesCallBack,
        RecipeService.RecipeServiceSimilarCallBack,
        SimilarListAdapter.SimilarViewHolder.SimilarRecipeItemCallBack, RecipeDetailsIngredientsAdapter.IngredientItemCallBack {

    private static final String TAG = "RecipeDetailsFragment";
    private static final String ARGS_RECIPE_ID = "recipeId";

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
    private ImageView mShopCartImageButton;
    private RecyclerView mIngredientList;
    private RecyclerView mStepList;
    private RatingViewPager mRatingViewPager;
    private TabLayout mRatingTabLayout;
    private RecyclerView mExperienceList;
    private RecyclerView mSimilarList;
    private View mExperienceListCard;
    private View mSimilarListCard;
    private View mRatingCard;
    private View mDeleteAllButton;
    private ImageView mUserImageView;
    private ConstraintLayout mUserImageLayout;

    private Recipe mRecipe;
    private int mRating;
    private String mRatingImagePath;
    private String mComment;
    private String mUserId;
    private List<Fragment> mRatingFragments;
    private RecipeDetailsStepAdapter.StepItemCallBack mStepItemCallBack;
    private ProgressDialog mProgressDialog;

    public static RecipeDetailsFragment newInstance(String recipeId, RecipeDetailsStepAdapter.StepItemCallBack callBack) {

        Bundle args = new Bundle();
        args.putString(ARGS_RECIPE_ID, recipeId);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(args);
        fragment.mStepItemCallBack = callBack;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        getViewReferences(view);
        mProgressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        mProgressDialog.show();
        mUserId = getActivity().getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        mRecipe = new Recipe();
        setupIngredientList();
        setupStepList();
        setupExperienceList();
        getRecipe(getArguments().getString(ARGS_RECIPE_ID));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    private void setupExperienceList() {
        ExperienceListAdapter adapter = new ExperienceListAdapter(new ArrayList<Experience>(), getActivity());
        mExperienceList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mExperienceList.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mExperienceList);
    }

    private void setupSimilarList(List<Recipe> recipes) {
        SimilarListAdapter adapter = new SimilarListAdapter(recipes, getActivity(), this);
        mSimilarList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mSimilarList.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mSimilarList);
    }

    private void setupViewPager(Experience experience) {
        mRatingFragments = new ArrayList<>();
        Fragment ratingBarFragment;
        if(experience == null){
            ratingBarFragment = RatingBarFragment.newInstance(this);
        }else{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date = df.format(experience.getDate());
            ratingBarFragment = RatingBarDoneFragment.newInstance(
                    (int) experience.getRating(),
                    getString(R.string.rated_on) + " " + date,
                    this);
        }
        mRatingFragments.add(ratingBarFragment);
        mRatingFragments.add(RatingPhotoFragment.newInstance(this));
        mRatingFragments.add(RatingCommentFragment.newInstance(this));
        RatingPagerAdapter adapter = new RatingPagerAdapter(getChildFragmentManager(), mRatingFragments);
        mRatingViewPager.setPagingEnabled(false);
        mRatingViewPager.setAdapter(adapter);
        mRatingTabLayout.setupWithViewPager(mRatingViewPager, true);
        mRatingTabLayout.setVisibility(View.INVISIBLE);
    }

    private void getCurrentExperience() {
        ExperienceService.getInstance().getExperience(mRecipe.getId(), mUserId, this);
    }

    private void setupStepList() {
        RecipeDetailsStepAdapter adapter = new RecipeDetailsStepAdapter(mRecipe.getSteps(), mStepItemCallBack);
        mStepList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStepList.setAdapter(adapter);
    }

    private void setupIngredientList() {
        RecipeDetailsIngredientsAdapter adapter = new RecipeDetailsIngredientsAdapter(getActivity(), mRecipe.getIngredients(), this);
        mIngredientList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientList.setAdapter(adapter);
    }

    private void getViewReferences(View view) {
        mRecipeImageView = view.findViewById(R.id.details_recipe_image_view);
        mRatingInfoBar = view.findViewById(R.id.details_recipe_rating_info_bar);
        mNameTextView = view.findViewById(R.id.details_recipe_name_text);
        mIngredientsNumberTextView = view.findViewById(R.id.details_info_ingredients_text);
        mCaloriesTextView = view.findViewById(R.id.details_info_calories_text);
        mServingsTextView = view.findViewById(R.id.details_info_servings_text);
        mTimeTextView = view.findViewById(R.id.details_recipe_time_text);
        mDescriptionTextView = view.findViewById(R.id.details_recipe_description_text);
        mAddAllButton = view.findViewById(R.id.details_recipe_ingredients_add_all_button);
        mDeleteAllButton = view.findViewById(R.id.details_recipe_ingredients_delete_all_button);
        mAddAllTextView = view.findViewById(R.id.details_recipe_ingredients_add_all_text);
        mShopItemsTextView = view.findViewById(R.id.details_recipe_shop_items_text);
        mShopCartImageButton = view.findViewById(R.id.details_recipe_shop_cart_image);
        mIngredientList = view.findViewById(R.id.details_recipe_ingredients_recycler);
        mStepList = view.findViewById(R.id.details_recipe_steps_recycler);
        mRatingViewPager = view.findViewById(R.id.details_recipe_rating_viewpager);
        mRatingTabLayout = view.findViewById(R.id.details_recipe_rating_tab_layout);
        mExperienceList = view.findViewById(R.id.details_recipe_experiences_recycler);
        mSimilarList = view.findViewById(R.id.details_recipe_similar_recipes_recycler);
        mExperienceListCard = view.findViewById(R.id.fragment_recipe_details_experience_list_cardview);
        mSimilarListCard = view.findViewById(R.id.fragment_recipe_details_similar_recipes_cardview);
        mRatingCard = view.findViewById(R.id.fragment_recipe_details_rating_cardview);
        mUserImageView = view.findViewById(R.id.fragment_recipe_details_user_image_view);
        mUserImageLayout = view.findViewById(R.id.fragment_recipe_details_user_image_layout);
    }

    private void updateUI(){
        setHasOptionsMenu(true);
        GlideApp.with(this).load(Constants.UPLOAD_FOLDER_URL + "/" + mRecipe.getImageURL())
                .centerCrop()
                .into(mRecipeImageView);
        UserService.getInstance().getUserById(mRecipe.getUserId(), new UserService.GetUserByIdCallBack() {
            @Override
            public void onCompletion(User user) {
                GlideApp.with(RecipeDetailsFragment.this).load(user.getImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .error(GlideApp.with(RecipeDetailsFragment.this)
                                .load(R.drawable.default_profile_picture).apply(RequestOptions.circleCropTransform()))
                        .into(mUserImageView);
            }
        });
        mUserImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_USER_ID, mRecipe.getUserId());
            startActivity(intent);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
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
        getExperienceList();
        getSimilarList();
        if(!mUserId.equals(mRecipe.getUserId())){
            getCurrentExperience();
        }else{
            mRatingCard.setVisibility(View.GONE);
        }
        //Setting up shopping section
        int count = ShoppingListDAO.getInstance(getActivity()).getShopItemsCount();
        mShopItemsTextView.setText(String.format(Locale.getDefault(), "%d", count));
        mAddAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddAllButton.setVisibility(View.GONE);
                mDeleteAllButton.setVisibility(View.VISIBLE);
                mAddAllTextView.setText(R.string.remove_all_from_shop_list);
                ShoppingListDAO.getInstance(getActivity()).addRecipe(mRecipe, mRecipe.getIngredients());
                for (Ingredient ingredient : mRecipe.getIngredients()) {
                    ingredient.setInShoppingList(true);
                }
                int count = ShoppingListDAO.getInstance(getActivity()).getShopItemsCount();
                mShopItemsTextView.setText(String.format(Locale.getDefault(), "%d", count));
                mIngredientList.getAdapter().notifyDataSetChanged();
            }
        });
        mDeleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddAllButton.setVisibility(View.VISIBLE);
                mDeleteAllButton.setVisibility(View.GONE);
                mAddAllTextView.setText(R.string.add_all_to_shop_list);
                ShoppingListDAO.getInstance(getActivity()).removeRecipe(mRecipe);
                for (Ingredient ingredient : mRecipe.getIngredients()) {
                    ingredient.setInShoppingList(false);
                }
                int count = ShoppingListDAO.getInstance(getActivity()).getShopItemsCount();
                mShopItemsTextView.setText(String.format(Locale.getDefault(), "%d", count));
                mIngredientList.getAdapter().notifyDataSetChanged();
            }
        });
        List<Ingredient> shopIngredients = ShoppingListDAO.getInstance(getActivity()).getRecipeIngredients(mRecipe);
        if (shopIngredients != null) {
            mAddAllButton.setVisibility(View.GONE);
            mDeleteAllButton.setVisibility(View.VISIBLE);
            mAddAllTextView.setText(R.string.remove_all_from_shop_list);
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                if (shopIngredients.contains(ingredient)) {
                    ingredient.setInShoppingList(true);
                } else {
                    ingredient.setInShoppingList(false);
                }
            }
        }
        mShopCartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShoppingListActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        mProgressDialog.dismiss();
    }

    private void getExperienceList() {
        ExperienceService.getInstance().getExperiencesForRecipe(mRecipe.getId(), this);
    }

    private void getSimilarList() {
        RecipeService.getInstance().getSimilarRecipes(mRecipe, this);
    }

    private void getRecipe(String id) {
        RecipeService.getInstance().getRecipeById(id, new RecipeService.RecipeServiceGetCallBack() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                if (isDetached()) return;
                mRecipe = recipes.get(0);
                updateUI();
                //update recipe views
                Set<String> viewedRecipes = getActivity().getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                        .getStringSet(getString(R.string.prefs_recipes_viewed_list), new HashSet<>());
                if (viewedRecipes.add(mRecipe.getId() + "")) {
                    RecipeService.getInstance().incrementViews(mRecipe.getId());
                    getActivity().getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                            .edit().putStringSet(getString(R.string.prefs_recipes_viewed_list), viewedRecipes).apply();
                }
            }

            @Override
            public void onFailure() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sorry")
                        .setMessage("An error has occured with the server.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe_details, menu);
        if (!mUserId.equals(mRecipe.getUserId())) {
            menu.removeItem(R.id.recipe_details_delete);
        }
        if (mUserId.equals(mRecipe.getUserId())) {
            menu.removeItem(R.id.menu_favorite);
            return;
        }
        MenuItem item = menu.getItem(0);
        String userId = getActivity().getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        int recipeId = Integer.parseInt(getArguments().getString(ARGS_RECIPE_ID));
        if(!FavoriteLab.getInstance(getActivity()).recipeExists(userId, recipeId)){
            item.setTitle(R.string.favorite);
            item.setIcon(R.drawable.icon_heart_outline);
        }else{
            item.setTitle(R.string.remove_favorite);
            item.setIcon(R.drawable.icon_heart_full);
        }
        Log.d(TAG, "onCreateOptionsMenu: " + FavoriteLab.getInstance(getActivity()).getList(userId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_favorite){
            String userId = getActivity().getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                    .getString(getString(R.string.prefs_user_id), null);
            int recipeId = Integer.parseInt(getArguments().getString(ARGS_RECIPE_ID));

            if (item.getTitle() == getString(R.string.favorite)) {
                item.setTitle(R.string.remove_favorite);
                item.setIcon(R.drawable.icon_heart_full);
                FavoriteLab.getInstance(getActivity()).insert(recipeId, userId);
                RecipeService.getInstance().incrementFavorites(recipeId);
            }else{
                item.setTitle(R.string.favorite);
                item.setIcon(R.drawable.icon_heart_outline);
                FavoriteLab.getInstance(getActivity()).delete(recipeId, userId);
                RecipeService.getInstance().decrementFavorites(recipeId);
            }
            return true;
        } else if (item.getItemId() == R.id.recipe_details_delete) {
            showDeleteConfirmationDialog();
            return true;
        } else if (item.getItemId() == R.id.recipe_details_home) {
            Intent intent = new Intent(getActivity(), MainScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirmation)
                .setMessage(R.string.confirmation_message_delete_recipe)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog.setMessage("Please wait...");
                        mProgressDialog.show();
                        RecipeService.getInstance().delete(mRecipe.getId(), new RecipeService.DeleteRecipeCallBack() {
                            @Override
                            public void onResponse(boolean isSuccessful) {
                                mProgressDialog.dismiss();
                                getActivity().onBackPressed();
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    public void onSubmitClickListener() {
        mRatingViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onRatingChangedListener(int rating) {
        mRating = rating;
    }

    @Override
    public void onImageChangedListener(String path) {
        if (path == null) {
            mRatingTabLayout.setVisibility(View.INVISIBLE);
            mRatingViewPager.setPagingEnabled(false);
        } else {
            mRatingImagePath = path;
            mRatingViewPager.setCurrentItem(2, true);
            mRatingTabLayout.setVisibility(View.VISIBLE);
            mRatingViewPager.setPagingEnabled(true);
        }
    }

    @Override
    public void onFinishButtonClickListener(String comment) {
        mComment = comment;
        addExperience();
    }

    private void addExperience() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Experience experience = new Experience();
                experience.setComment(mComment);
                experience.setRating(mRating);
                experience.setRecipeId(mRecipe.getId());
                experience.setRecipeOwnerId(mRecipe.getUserId());
                User user = new User();
                user.setId(mUserId);
                experience.setUser(user);
                ExperienceService.getInstance().addExperience(experience, mRatingImagePath, RecipeDetailsFragment.this);
            }
        });
    }

    @Override
    public void onAddExperienceSuccess() {
        Snackbar.make(getView(),
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

        ExperienceService.getInstance().getExperiencesForRecipe(mRecipe.getId(), new ExperienceService.GetExperiencesCallBack() {
            @Override
            public void onGetExperiencesSuccess(List<Experience> experiences) {
                updateExperienceList(experiences);
            }

            @Override
            public void onGetExperiencesFailure() {
                //
            }
        });
    }

    private void updateExperienceList(List<Experience> experiences) {
        mExperienceListCard.setVisibility(View.VISIBLE);
        ExperienceListAdapter adapter = (ExperienceListAdapter) mExperienceList.getAdapter();
        adapter.setExperiences(experiences);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddExperienceFailure() {
        Snackbar snackbar = Snackbar.make(getView(),
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

        ExperienceService.getInstance().getExperiencesForRecipe(mRecipe.getId(), new ExperienceService.GetExperiencesCallBack() {
            @Override
            public void onGetExperiencesSuccess(List<Experience> experiences) {
                if (experiences.size() == 0) {
                    mExperienceListCard.setVisibility(View.GONE);
                    return;
                }
                ExperienceListAdapter adapter = (ExperienceListAdapter) mExperienceList.getAdapter();
                adapter.setExperiences(experiences);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGetExperiencesFailure() {
                mExperienceListCard.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDeleteExperienceFailure() {
        Snackbar snackbar = Snackbar.make(getView(),
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

    @Override
    public void onGetExperiencesSuccess(List<Experience> experiences) {
        if (experiences.size() == 0) {
            mExperienceListCard.setVisibility(View.GONE);
            return;
        }
        updateExperienceList(experiences);
    }

    @Override
    public void onGetExperiencesFailure() {
        mExperienceListCard.setVisibility(View.GONE);
        Log.i(TAG, "onGetExperiencesFailure: ");
    }

    @Override
    public void onGetSimilarResponse(List<Recipe> recipes) {
        if (recipes.size() == 0) {
            mSimilarListCard.setVisibility(View.GONE);
            return;
        }
        setupSimilarList(recipes);
    }

    @Override
    public void onSimilarRecipeClicked(Recipe recipe) {
        Intent i = new Intent(getActivity(), RecipeDetailsActivity.class);
        i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipe.getId() + "");
        i.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, true);
        startActivity(i);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onAddIngredientButtonClicked(Ingredient ingredient) {
        ShoppingListDAO.getInstance(getActivity()).addIngredient(mRecipe, ingredient);
        int count = ShoppingListDAO.getInstance(getActivity()).getShopItemsCount();
        mShopItemsTextView.setText(String.format(Locale.getDefault(), "%d", count));
    }

    @Override
    public void onDeleteIngredientButtonClicked(Ingredient ingredient) {
        ShoppingListDAO.getInstance(getActivity()).removeIngredient(mRecipe, ingredient);
        int count = ShoppingListDAO.getInstance(getActivity()).getShopItemsCount();
        mShopItemsTextView.setText(String.format(Locale.getDefault(), "%d", count));
    }
}

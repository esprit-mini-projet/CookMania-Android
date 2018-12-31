package tn.duoes.esprit.cookmania.controllers.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ExperienceListAdapter;
import tn.duoes.esprit.cookmania.adapters.RatingPagerAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsIngredientsAdapter;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import tn.duoes.esprit.cookmania.adapters.SimilarListAdapter;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.dao.FavoriteLab;
import tn.duoes.esprit.cookmania.models.Experience;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.ExperienceService;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;
import tn.duoes.esprit.cookmania.views.RatingViewPager;

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
        SimilarListAdapter.SimilarViewHolder.SimilarRecipeItemCallBack {

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
    private ImageView mShopCartImageView;
    private RecyclerView mIngredientList;
    private RecyclerView mStepList;
    private RatingViewPager mRatingViewPager;
    private TabLayout mRatingTabLayout;
    private RecyclerView mExperienceList;
    private RecyclerView mSimilarList;

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
        getRecipe(getArguments().getString(ARGS_RECIPE_ID));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    private void setupExperienceList(List<Experience> experiences) {
        ExperienceListAdapter adapter = new ExperienceListAdapter(experiences, getActivity());
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
        RecipeDetailsIngredientsAdapter adapter = new RecipeDetailsIngredientsAdapter(getActivity(), mRecipe.getIngredients());
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
        mAddAllTextView = view.findViewById(R.id.details_recipe_ingredients_add_all_text);
        mShopItemsTextView = view.findViewById(R.id.details_recipe_shop_items_text);
        mShopCartImageView = view.findViewById(R.id.details_recipe_shop_cart_image);
        mIngredientList = view.findViewById(R.id.details_recipe_ingredients_recycler);
        mStepList = view.findViewById(R.id.details_recipe_steps_recycler);
        mRatingViewPager = view.findViewById(R.id.details_recipe_rating_viewpager);
        mRatingTabLayout = view.findViewById(R.id.details_recipe_rating_tab_layout);
        mExperienceList = view.findViewById(R.id.details_recipe_experiences_recycler);
        mSimilarList = view.findViewById(R.id.details_recipe_similar_recipes_recycler);
    }

    private void updateUI(){
        setHasOptionsMenu(true);
        GlideApp.with(this).load(Constants.UPLOAD_FOLDER_URL + "/" + mRecipe.getImageURL())
                .centerCrop()
                .into(mRecipeImageView);
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
            getView().findViewById(R.id.fragment_recipe_details_rating_cardview).setVisibility(View.GONE);
        }
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
                mRecipe = recipes.get(0);
                Log.d(TAG, "onGetSimilarResponse: " + mRecipe);
                updateUI();
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

            if(item.getTitle() == getString(R.string.favorite)){
                item.setTitle(R.string.remove_favorite);
                item.setIcon(R.drawable.icon_heart_full);
                FavoriteLab.getInstance(getActivity()).insert(recipeId, userId);
            }else{
                item.setTitle(R.string.favorite);
                item.setIcon(R.drawable.icon_heart_outline);
                FavoriteLab.getInstance(getActivity()).delete(recipeId, userId);
            }
            return true;
        } else if (item.getItemId() == R.id.recipe_details_delete) {
            showDeleteConfirmationDialog();
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
                                getActivity().finish();
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
        if(path == null){
            mRatingTabLayout.setVisibility(View.INVISIBLE);
            mRatingViewPager.setPagingEnabled(false);
        }else{
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
        Experience experience = new Experience();
        experience.setComment(mComment);
        experience.setRating(mRating);
        experience.setRecipeId(mRecipe.getId());
        User user = new User();
        user.setId(mUserId);
        experience.setUser(user);
        ExperienceService.getInstance().addExperience(experience, mRatingImagePath, this);
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
                getView().findViewById(R.id.fragment_recipe_details_experience_list_cardview).setVisibility(View.VISIBLE);
                ExperienceListAdapter adapter = (ExperienceListAdapter) mExperienceList.getAdapter();
                adapter.setExperiences(experiences);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGetExperiencesFailure() {
                //
            }
        });
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
                if(experiences.size() == 0){
                    getView().findViewById(R.id.fragment_recipe_details_experience_list_cardview).setVisibility(View.GONE);
                    return;
                }
                ExperienceListAdapter adapter = (ExperienceListAdapter) mExperienceList.getAdapter();
                adapter.setExperiences(experiences);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGetExperiencesFailure() {
                getView().findViewById(R.id.fragment_recipe_details_experience_list_cardview).setVisibility(View.GONE);
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
        if(experiences.size() == 0){
            getView().findViewById(R.id.fragment_recipe_details_experience_list_cardview).setVisibility(View.GONE);
            return;
        }
        setupExperienceList(experiences);
    }

    @Override
    public void onGetExperiencesFailure() {
        getView().findViewById(R.id.fragment_recipe_details_experience_list_cardview).setVisibility(View.GONE);
        Log.i(TAG, "onGetExperiencesFailure: ");
    }

    @Override
    public void onGetSimilarResponse(List<Recipe> recipes) {
        if(recipes.size() == 0){
            getView().findViewById(R.id.fragment_recipe_details_similar_recipes_cardview).setVisibility(View.GONE);
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
    }
}

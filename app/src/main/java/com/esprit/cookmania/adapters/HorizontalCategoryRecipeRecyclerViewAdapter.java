package com.esprit.cookmania.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esprit.cookmania.R;
import com.esprit.cookmania.controllers.activities.MainScreenActivity;
import com.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import com.esprit.cookmania.controllers.dialogs.RecipeDialog;
import com.esprit.cookmania.controllers.fragments.HomeFragment;
import com.esprit.cookmania.models.Recipe;
import com.esprit.cookmania.models.User;
import com.esprit.cookmania.services.UserService;
import com.esprit.cookmania.utils.Constants;
import com.esprit.cookmania.utils.NavigationUtils;

import java.util.List;

public class HorizontalCategoryRecipeRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalCategoryRecipeRecyclerViewAdapter.ViewHolder> {

    public List<Recipe> mRecipes;
    private FragmentActivity activity;
    public RecipeDialog recipeDialog;

    public HorizontalCategoryRecipeRecyclerViewAdapter(List<Recipe> recipes, FragmentActivity activity){
        super();
        mRecipes = recipes;
        this.activity = activity;
        recipeDialog = new RecipeDialog();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_horizontal_category_recipe_recycler, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = mRecipes.get(position);

        UserService.getInstance().getUserById(recipe.getUserId(), new UserService.GetUserByIdCallBack() {
            @Override
            public void onCompletion(User user) {
                viewHolder.user = user;
            }
        });

        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(viewHolder.recipeImageView);
        viewHolder.recipeNameTV.setText(recipe.getName());
        viewHolder.recipeRatingBar.setRating(recipe.getRating());
        viewHolder.recipe = recipe;
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView recipeNameTV;
        ImageView recipeImageView;
        AppCompatRatingBar recipeRatingBar;
        public Recipe recipe;
        User user;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            final CardView cardView = (CardView) itemView;
            cardView.setPreventCornerOverlap(false);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = NavigationUtils.getNavigationFormattedIntent(v.getContext(), RecipeDetailsActivity.class);
                    i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipe.getId()+"");
                    v.getContext().startActivity(i);
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MainScreenActivity.viewPager.setPagingEnabled(false);
                    HomeFragment.scrollView.setScrollingEnabled(false);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RecipeDialog.RECIPE_KEY, recipe);
                    bundle.putString(RecipeDialog.USER_IMAGE_KEY, user.getImageUrl());
                    bundle.putString(RecipeDialog.USER_NAME_KEY, user.getUserName());
                    recipeDialog.setArguments(bundle);
                    recipeDialog.show(activity.getSupportFragmentManager(), "TEST");
                    return false;
                }
            });
            recipeNameTV = itemView.findViewById(R.id.cat_res_row_tv);
            recipeImageView = itemView.findViewById(R.id.cat_res_row_iv);
            recipeRatingBar = itemView.findViewById(R.id.cat_res_recipe_rb);
        }
    }
}

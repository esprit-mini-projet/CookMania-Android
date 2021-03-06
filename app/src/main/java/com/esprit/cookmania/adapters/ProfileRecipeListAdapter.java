package com.esprit.cookmania.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.cookmania.R;
import com.esprit.cookmania.models.Recipe;
import com.esprit.cookmania.utils.Constants;
import com.esprit.cookmania.utils.GlideApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProfileRecipeListAdapter extends RecyclerView.Adapter<ProfileRecipeListAdapter.RecipeViewHolder> {

    private static final String TAG = "ProfileRecipeListAdapter";

    private List<Recipe> mRecipes;
    private Context mContext;
    private RecipeViewHolder.RecipeItemCallBack mCallBack;
    private int mPosition;

    public ProfileRecipeListAdapter(List<Recipe> recipes, Context context, RecipeViewHolder.RecipeItemCallBack callBack) {
        mRecipes = recipes;
        mContext = context;
        mCallBack = callBack;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_profile_user_recipe, viewGroup, false);
        return new RecipeViewHolder(view, mContext, mCallBack);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.bind(mRecipes.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setExperiences(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mRecipeNameText;
        TextView mDateText;
        TextView mFavoritesText;
        TextView mViewsText;
        TextView mRatingText;
        Context mContext;
        RecipeItemCallBack mCallBack;

        RecipeViewHolder(View itemView, Context context, RecipeItemCallBack callBack) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.list_item_profile_user_recipe_image_view);
            mRecipeNameText = itemView.findViewById(R.id.list_item_profile_user_recipe_name_text);
            mDateText = itemView.findViewById(R.id.list_item_profile_user_recipe_date_text);
            mFavoritesText = itemView.findViewById(R.id.list_item_profile_user_recipe_favorites_text);
            mViewsText = itemView.findViewById(R.id.list_item_profile_user_recipe_views_text);
            mRatingText = itemView.findViewById(R.id.list_item_profile_user_recipe_rating_text);
            mContext = context;
            mCallBack = callBack;
        }

        void bind(final Recipe recipe, final int i) {
            GlideApp.with(mContext).load(Constants.UPLOAD_FOLDER_URL + "/" + recipe.getImageURL())
                    .centerCrop()
                    .error(GlideApp.with(mContext).load(R.drawable.image_placeholder).centerCrop())
                    .into(mImageView);
            mRecipeNameText.setText(recipe.getName());
            mViewsText.setText(String.format(Locale.getDefault(), "%d", recipe.getViews()));
            mFavoritesText.setText(String.format(Locale.getDefault(), "%d", recipe.getFavorites()));
            mRatingText.setText(String.format(Locale.getDefault(), "%.1f", recipe.getRating()));
            DateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            mDateText.setText(String.format("Added on %s", df.format(recipe.getDate())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onRecipeItemClicked(recipe.getId());
                }
            });
            //getting the pixel value of 8dp
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    r.getDisplayMetrics()
            );
            //add 8dp top margin to first item or remove it
            if (i == 0) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.topMargin = px;
                itemView.setLayoutParams(params);
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.topMargin = 0;
                itemView.setLayoutParams(params);
            }
        }

        public interface RecipeItemCallBack {
            void onRecipeItemClicked(int recipeId);
        }

    }
}

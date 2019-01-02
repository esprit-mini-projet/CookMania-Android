package tn.duoes.esprit.cookmania.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.MainScreenActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.controllers.dialogs.RecipeDialog;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.UserService;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = SearchResultRecyclerViewAdapter.class.getSimpleName();

    public List<Recipe> recipes;
    private MainScreenActivity activity;
    public RecipeDialog recipeDialog;

    public SearchResultRecyclerViewAdapter(MainScreenActivity activity){
        super();
        recipes = new ArrayList<>();
        recipeDialog = new RecipeDialog();
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search_recipe, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = recipes.get(position);
        UserService.getInstance().getUserById(recipe.getUserId(), new UserService.GetUserByIdCallBack() {
            @Override
            public void onCompletion(User user) {
                viewHolder.user = user;
                Glide.with(viewHolder.itemView).load(user.getImageUrl()).into(viewHolder.userImageView);
            }
        });
        viewHolder.recipe = recipe;
        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(viewHolder.recipeImageView);
        viewHolder.ratingBar.setRating(recipe.getRating());
        viewHolder.recipeName.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImageView, recipeImageView;
        RatingBar ratingBar;
        TextView recipeName;
        Recipe recipe;
        User user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = NavigationUtils.getNavigationFormattedIntent(v.getContext(), RecipeDetailsActivity.class);
                    i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipe.getId()+"");
                    v.getContext().startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MainScreenActivity.viewPager.setPagingEnabled(false);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RecipeDialog.RECIPE_KEY, recipe);
                    bundle.putString(RecipeDialog.USER_IMAGE_KEY, user.getImageUrl());
                    bundle.putString(RecipeDialog.USER_NAME_KEY, user.getUserName());
                    recipeDialog.setArguments(bundle);
                    recipeDialog.show(activity.getSupportFragmentManager(), "TEST");
                    return false;
                }
            });

            userImageView = itemView.findViewById(R.id.search_item_user_iv);
            recipeImageView = itemView.findViewById(R.id.dialog_recipe_iv);
            ratingBar = itemView.findViewById(R.id.search_item_recipe_rb);
            recipeName = itemView.findViewById(R.id.search_item_recipe_tv);

            //recipeImageView.getLayoutParams().height = MesurementConvertionUtils.dpToPx(200, itemView.getContext());
        }
    }
}

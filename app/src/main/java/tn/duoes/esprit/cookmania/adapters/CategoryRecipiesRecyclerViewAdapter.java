package tn.duoes.esprit.cookmania.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.CategoryRecipesActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class CategoryRecipiesRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecipiesRecyclerViewAdapter.ViewHolder>{

    public List<Recipe> mRecipes;

    public CategoryRecipiesRecyclerViewAdapter(List<Recipe> recipes){
        super();
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_category_recipies, viewGroup, false);
        return new CategoryRecipiesRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = mRecipes.get(position);

        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(viewHolder.imageView);
        viewHolder.nameTV.setText(recipe.getName());
        viewHolder.ratingTV.setText(String.valueOf(recipe.getRating()));
        viewHolder.caloriesTV.setText(String.valueOf(recipe.getCalories()));
        viewHolder.servingsTV.setText(String.valueOf(recipe.getServings()));
        viewHolder.id = recipe.getId();

    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        int id;

        ImageView imageView;
        TextView nameTV;
        TextView ratingTV;
        TextView caloriesTV;
        TextView servingsTV;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.row_cat_recipies_iv);
            nameTV = itemView.findViewById(R.id.row_cat_recipies_name_tv);
            ratingTV = itemView.findViewById(R.id.row_cat_recipies_rating_tv);
            caloriesTV = itemView.findViewById(R.id.row_cat_recipies_calories_tv);
            servingsTV = itemView.findViewById(R.id.row_cat_recipies_servings_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = NavigationUtils.getNavigationFormattedIntent(v.getContext(), RecipeDetailsActivity.class);
                    intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, id+"");
                    intent.putExtra(RecipeDetailsActivity.EXTRA_PARENT_ACTIVITY_CLASS, CategoryRecipesActivity.class.getCanonicalName());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}

package tn.duoes.esprit.cookmania.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.ProfileActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class CategoryRecipeRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> mRecipes;

    public CategoryRecipeRecyclerViewAdapter(List<Recipe> recipes){
        super();
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_recipe_recycler_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = mRecipes.get(position);

        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(viewHolder.recipeImageView);
        viewHolder.recipeNameTV.setText(recipe.getName());
        viewHolder.id = recipe.getId();
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView recipeNameTV;
        ImageView recipeImageView;
        public int id;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            final CardView cardView = (CardView) itemView;
            cardView.setPreventCornerOverlap(false);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = NavigationUtils.getNavigationFormattedIntent(v.getContext(), RecipeDetailsActivity.class);
                    i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, id+"");
                    v.getContext().startActivity(i);
                }
            });
            recipeNameTV = itemView.findViewById(R.id.cat_res_row_tv);
            recipeImageView = itemView.findViewById(R.id.cat_res_row_iv);
        }
    }
}

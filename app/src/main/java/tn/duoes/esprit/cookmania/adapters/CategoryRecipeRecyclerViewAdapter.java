package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Recipe;

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
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Context context = viewHolder.recipeImageView.getContext();
        Recipe recipe = mRecipes.get(position);
        viewHolder.recipeImageView.setImageResource(context.getResources().getIdentifier(recipe.getImageURL(), "drawable", context.getPackageName()));
        viewHolder.recipeNameTV.setText(recipe.getName());
        viewHolder.id = recipe.getId();
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView recipeNameTV;
        public ImageView recipeImageView;
        public int id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            final CardView cardView = (CardView) itemView;
            cardView.setPreventCornerOverlap(false);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Recipe with id = "+id+" clicked!");
                }
            });
            recipeNameTV = itemView.findViewById(R.id.cat_res_row_tv);
            recipeImageView = itemView.findViewById(R.id.cat_res_row_iv);
        }
    }
}

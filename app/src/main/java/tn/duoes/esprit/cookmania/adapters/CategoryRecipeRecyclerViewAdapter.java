package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.ImageUtils;

public class CategoryRecipeRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> mRecipes;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public CategoryRecipeRecyclerViewAdapter(List<Recipe> recipes, Context context){
        super();
        mRecipes = recipes;

        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
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

        viewHolder.recipeImageView.setImageUrl(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL(), mImageLoader);
        viewHolder.recipeNameTV.setText(recipe.getName());
        viewHolder.id = recipe.getId();
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView recipeNameTV;
        public NetworkImageView recipeImageView;
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

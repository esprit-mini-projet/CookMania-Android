package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

public class SimilarListAdapter extends RecyclerView.Adapter<SimilarListAdapter.SimilarViewHolder> {

    private static final String TAG = "SimilarListAdapter";

    private List<Recipe> mRecipes;
    private Context mContext;
    private SimilarViewHolder.SimilarRecipeItemCallBack mCallBack;

    public SimilarListAdapter(List<Recipe> recipes, Context context, SimilarViewHolder.SimilarRecipeItemCallBack callBack){
        mRecipes = recipes;
        mContext = context;
        mCallBack = callBack;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_similar, viewGroup, false);
        return new SimilarViewHolder(view, mContext, mCallBack);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder similarViewHolder, int i) {
        similarViewHolder.bind(mRecipes.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setExperiences(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    public static class SimilarViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mRecipeNameText;
        AppCompatRatingBar mRatingBar;
        Context mContext;
        SimilarRecipeItemCallBack mCallBack;

        SimilarViewHolder(View itemView, Context context, SimilarRecipeItemCallBack callBack){
            super(itemView);
            mImageView = itemView.findViewById(R.id.list_item_similar_image_view);
            mRecipeNameText = itemView.findViewById(R.id.list_item_similar_recipe_name_text);
            mRatingBar = itemView.findViewById(R.id.list_item_similar_rating_bar);
            mContext = context;
            mCallBack = callBack;
        }

        void bind(final Recipe recipe, int i){
            GlideApp.with(mContext).load(Constants.UPLOAD_FOLDER_URL + "/" + recipe.getImageURL())
                    .centerCrop()
                    .error(GlideApp.with(mContext).load(R.drawable.image_placeholder).centerCrop())
                    .into(mImageView);
            mRecipeNameText.setText(recipe.getName());
            mRatingBar.setRating(recipe.getRating());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onSimilarRecipeClicked(recipe);
                }
            });
            //getting the pixel value of 8dp
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    r.getDisplayMetrics()
            );
            //add 8dp start margin to first item or remove it
            if(i == 0){
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMarginStart(px);
                itemView.setLayoutParams(params);
            }else{
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMarginStart(0);
                itemView.setLayoutParams(params);
            }
        }

        public interface SimilarRecipeItemCallBack{
            void onSimilarRecipeClicked(Recipe recipe);
        }
    }
}

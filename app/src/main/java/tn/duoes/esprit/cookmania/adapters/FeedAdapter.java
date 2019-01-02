package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.ProfileActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.dao.FavoriteLab;
import tn.duoes.esprit.cookmania.models.FeedResult;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private static final String TAG = FeedAdapter.class.getSimpleName();

    public List<FeedResult> feedResults;
    private SimpleDateFormat simpleDateFormat;

    public FeedAdapter(){
        Log.d(TAG, "FeedAdapter: ");
        this.feedResults = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_feed, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FeedResult feedResult = feedResults.get(position);


        Glide.with(viewHolder.itemView).load(feedResult.getUser().getImageUrl()).into(viewHolder.userIv);

        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+feedResult.getRecipe().getImageURL()).into(viewHolder.recipeFgIv);
        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+feedResult.getRecipe().getImageURL()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                .into(viewHolder.recipeBgIv);

        viewHolder.userNameTv.setText(feedResult.getUser().getUserName());
        viewHolder.userDateTv.setText(simpleDateFormat.format(feedResult.getUser().getDate()));
        viewHolder.recipeViewsTv.setText(String.valueOf(feedResult.getRecipe().getViews()));
        viewHolder.recipeNameTv.setText(feedResult.getRecipe().getName());
        viewHolder.timeTv.setText(getTime(feedResult.getRecipe().getDate()));
        viewHolder.recipeRating.setRating(feedResult.getRecipe().getRating());
        viewHolder.recipe = feedResult.getRecipe();
        String userId = viewHolder.itemView.getContext().getSharedPreferences(viewHolder.itemView.getContext().getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(viewHolder.itemView.getContext().getString(R.string.prefs_user_id), "");
        if (FavoriteLab.getInstance(viewHolder.itemView.getContext()).recipeExists(userId, viewHolder.recipe.getId())) {
            viewHolder.favoriteBt.setTag(true);
            Drawable heartIcon = viewHolder.itemView.getContext().getDrawable(R.drawable.icon_heart_full);
            viewHolder.favoriteBt.setImageDrawable(heartIcon);
        } else {
            viewHolder.favoriteBt.setTag(false);
            Drawable heartIcon = viewHolder.itemView.getContext().getDrawable(R.drawable.icon_heart_outline);
            viewHolder.favoriteBt.setImageDrawable(heartIcon);
        }
    }

    private String getTime(Date startDate){
        Date endDate = new Date();

        long diff = endDate.getTime() - startDate.getTime();
        long elapsedSeconds = diff / 1000;
        long elapsedMinutes = elapsedSeconds / 60;
        long elapsedHours = elapsedMinutes / 60;
        long elapsedDays = elapsedHours / 24;

        if(elapsedDays==0){
            if(elapsedHours == 0){
                if(elapsedMinutes == 0){
                    return elapsedSeconds+" SECOND"+(elapsedSeconds>1?"S":"")+" AGO";
                }
                return elapsedMinutes+" MINUTE"+(elapsedMinutes>1?"S":"")+" AGO";
            }
            return elapsedHours+" HOUR"+(elapsedHours>1?"S":"")+" AGO";
        }
        return elapsedDays+" DAY"+(elapsedDays>1?"S":"")+" AGO";
    }

    @Override
    public int getItemCount() {
        return feedResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout userLayout, recipeLayout;
        ImageView userIv, recipeBgIv, recipeFgIv, favoriteBt;
        TextView userNameTv, userDateTv, recipeViewsTv, recipeNameTv, timeTv;
        AppCompatRatingBar recipeRating;

        Recipe recipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Layouts
            userLayout = itemView.findViewById(R.id.feed_item_user_layout);
            recipeLayout = itemView.findViewById(R.id.feed_item_recipe_layout);

            //ImageViews
            userIv = itemView.findViewById(R.id.feed_item_user_iv);
            recipeBgIv = itemView.findViewById(R.id.feed_item_recipe_bg_iv);
            recipeFgIv = itemView.findViewById(R.id.feed_item_recipe_fg_iv);
            favoriteBt = itemView.findViewById(R.id.feed_item_recipe_fav_bt);

            //TextViews
            userNameTv = itemView.findViewById(R.id.feed_item_user_name_tv);
            userDateTv = itemView.findViewById(R.id.feed_item_user_date_tv);
            recipeViewsTv = itemView.findViewById(R.id.feed_item_recipe_view_tv);
            recipeNameTv = itemView.findViewById(R.id.feed_item_recipe_name_tv);
            timeTv = itemView.findViewById(R.id.feed_item_time_tv);

            //RatingBar
            recipeRating = itemView.findViewById(R.id.feed_item_recipe_rb);

            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ProfileActivity.class);
                    i.putExtra(ProfileActivity.EXTRA_USER_ID, recipe.getUserId());
                    v.getContext().startActivity(i);
                }
            });

            recipeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = NavigationUtils.getNavigationFormattedIntent(v.getContext(), RecipeDetailsActivity.class);
                    i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipe.getId()+"");
                    v.getContext().startActivity(i);
                }
            });

            favoriteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = v.getContext().getSharedPreferences(v.getContext().getString(R.string.prefs_name), Context.MODE_PRIVATE)
                            .getString(v.getContext().getString(R.string.prefs_user_id), "");
                    if(!(boolean)favoriteBt.getTag()){
                        favoriteBt.setImageResource(R.drawable.icon_heart_full);
                        favoriteBt.setColorFilter(itemView.getResources().getColor(R.color.colorAccent));
                        favoriteBt.setTag(true);
                        FavoriteLab.getInstance(v.getContext()).insert(recipe.getId(), userId);
                    }else{
                        favoriteBt.setImageResource(R.drawable.icon_heart_outline);
                        favoriteBt.setColorFilter(Color.parseColor("#000000"));
                        favoriteBt.setTag(false);
                        FavoriteLab.getInstance(v.getContext()).delete(recipe.getId(), userId);
                    }
                }
            });
        }
    }
}

package tn.duoes.esprit.cookmania.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.glide.transformations.BlurTransformation;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.MainScreenActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private static final String TAG = FeedAdapter.class.getSimpleName();

    public List<Recipe> recipes;

    public FeedAdapter(){
        this.recipes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_feed, viewGroup, false);
        return new FeedAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = recipes.get(position);

        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(viewHolder.userIv);

        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(viewHolder.recipeFgIv);
        Glide.with(viewHolder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                .into(viewHolder.recipeBgIv);

        viewHolder.userNameTv.setText("Seif Abdennadher");
        viewHolder.userDateTv.setText("25 Dec, 2018");
        viewHolder.recipeViewsTv.setText(String.valueOf(recipe.getViews()));
        viewHolder.recipeNameTv.setText(recipe.getName());
        viewHolder.timeTv.setText(getTime(recipe.getDate()));
        viewHolder.recipeRating.setRating(recipe.getRating());
        viewHolder.recipe = recipe;
        viewHolder.favoriteBt.setTag(false);
    }

    private String getTime(Date startDate){
        Date endDate = new Date();
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

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
        return recipes.size();
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
                    Log.d(TAG, "onClick: user");
                }
            });

            recipeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = NavigationUtils.getNavigationFormattedIntent(v.getContext(), RecipeDetailsActivity.class);
                    i.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipe.getId()+"");
                    i.putExtra(RecipeDetailsActivity.EXTRA_PARENT_ACTIVITY_CLASS, MainScreenActivity.class.getCanonicalName());
                    v.getContext().startActivity(i);
                }
            });

            favoriteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(boolean)favoriteBt.getTag()){
                        favoriteBt.setImageResource(R.drawable.icon_heart_full);
                        favoriteBt.setColorFilter(itemView.getResources().getColor(R.color.colorAccent));
                        favoriteBt.setTag(true);
                    }else{
                        favoriteBt.setImageResource(R.drawable.icon_heart_outline);
                        favoriteBt.setColorFilter(Color.parseColor("#000000"));
                        favoriteBt.setTag(false);
                    }
                }
            });
        }
    }
}

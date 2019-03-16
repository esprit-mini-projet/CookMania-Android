package com.esprit.cookmania.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.esprit.cookmania.R;
import com.esprit.cookmania.controllers.activities.ProfileActivity;
import com.esprit.cookmania.models.Experience;
import com.esprit.cookmania.utils.Constants;
import com.esprit.cookmania.utils.GlideApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExperienceListAdapter extends RecyclerView.Adapter<ExperienceListAdapter.ExperienceViewHolder> {

    private static final String TAG = "ExperienceListAdapter";

    private List<Experience> mExperiences;
    private Context mContext;

    public ExperienceListAdapter(List<Experience> experiences, Context context){
        mExperiences = experiences;
        mContext = context;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_experience, viewGroup, false);
        return new ExperienceViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder experienceViewHolder, int i) {
        experienceViewHolder.bind(mExperiences.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mExperiences.size();
    }

    public void setExperiences(List<Experience> experiences) {
        mExperiences = experiences;
    }

    static class ExperienceViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        ImageView mUserImageView;
        TextView mUserNameText;
        TextView mDateText;
        TextView mCommentText;
        AppCompatRatingBar mRatingBar;
        Context mContext;

        ExperienceViewHolder(View itemView, Context context){
            super(itemView);
            mImageView = itemView.findViewById(R.id.list_item_experience_image_view);
            mUserImageView = itemView.findViewById(R.id.list_item_experience_user_image);
            mUserNameText = itemView.findViewById(R.id.list_item_experience_user_name_text);
            mDateText = itemView.findViewById(R.id.list_item_experience_date_text);
            mCommentText = itemView.findViewById(R.id.list_item_experience_comment_text);
            mRatingBar = itemView.findViewById(R.id.list_item_experience_rating_bar);
            mContext = context;
        }

        void bind(final Experience experience, int i) {
            //set experience photo
            GlideApp.with(mContext).load(Constants.UPLOAD_FOLDER_URL + "/" + experience.getImageUrl())
                    .error(GlideApp.with(mContext).load(R.drawable.image_placeholder).centerCrop())
                    .centerCrop()
                    .into(mImageView);
            //set user photo
            GlideApp.with(mContext).load(experience.getUser().getImageUrl())
                    .placeholder(R.drawable.default_profile_picture)
                    .error(GlideApp.with(mContext).load(R.drawable.default_profile_picture).apply(RequestOptions.circleCropTransform()))
                    .apply(RequestOptions.circleCropTransform())
                    .into(mUserImageView);
            mUserImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.EXTRA_USER_ID, experience.getUser().getId());
                    mContext.startActivity(intent);
                    ((AppCompatActivity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            //set the remaining fields
            mRatingBar.setRating(experience.getRating());
            mUserNameText.setText(experience.getUser().getUserName());
            mCommentText.setText(experience.getComment());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            mDateText.setText(dateFormat.format(experience.getDate()));
            //getting the pixel value of 8dp
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    r.getDisplayMetrics()
            );
            //add 8dp start margin to first item or remove it
            if (i == 0) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMarginStart(px);
                itemView.setLayoutParams(params);
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMarginStart(0);
                itemView.setLayoutParams(params);
            }
        }
    }
}

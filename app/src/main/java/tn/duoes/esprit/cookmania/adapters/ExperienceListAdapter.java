package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Experience;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

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
        experienceViewHolder.bind(mExperiences.get(i));
    }

    @Override
    public int getItemCount() {
        return mExperiences.size();
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

        void bind(Experience experience){
            //set experience photo
            if(experience.getImageUrl().isEmpty()){
                GlideApp.with(mContext).load(mContext.getDrawable(R.drawable.image_placeholder)).centerCrop().into(mImageView);
            }else{
                GlideApp.with(mContext).load(Constants.UPLOAD_FOLDER_URL + "/" + experience.getImageUrl())
                        .centerCrop().into(mImageView);
            }
            //set user photo
            GlideApp.with(mContext).load(experience.getUser().getImageUrl()).centerCrop().into(mUserImageView);
            //set the remaining fields
            mRatingBar.setRating(experience.getRating());
            mUserNameText.setText(experience.getUser().getUserName());
            mCommentText.setText(experience.getComment());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            mDateText.setText(dateFormat.format(experience.getDate()));
        }
    }
}

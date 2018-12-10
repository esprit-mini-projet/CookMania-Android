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

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Experience;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

public class ExperienceListAdapter extends RecyclerView.Adapter<ExperienceListAdapter.ExperienceViewHolder> {

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
        //experienceViewHolder.bind(mExperiences.get(i));
    }

    @Override
    public int getItemCount() {
        return 4;
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
            GlideApp.with(mContext).load(Constants.UPLOAD_FOLDER_URL + experience.getImageUrl())
                    .centerCrop().into(mImageView);
        }
    }
}

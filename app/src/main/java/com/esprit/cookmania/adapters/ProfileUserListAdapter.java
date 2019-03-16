package com.esprit.cookmania.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.esprit.cookmania.R;
import com.esprit.cookmania.models.User;
import com.esprit.cookmania.utils.GlideApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProfileUserListAdapter extends RecyclerView.Adapter<ProfileUserListAdapter.UserViewHolder> {

    private static final String TAG = "ProfileUserListAdapter";

    private List<User> mUsers;
    private Context mContext;
    private UserViewHolder.UserItemCallBack mCallBack;

    public ProfileUserListAdapter(List<User> users, Context context, UserViewHolder.UserItemCallBack callBack) {
        mUsers = users;
        mContext = context;
        mCallBack = callBack;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_profile_user_card, viewGroup, false);
        return new UserViewHolder(view, mContext, mCallBack);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        userViewHolder.bind(mUsers.get(i), i);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void setExperiences(List<User> users) {
        mUsers = users;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mUserNameText;
        TextView mDateText;
        Context mContext;
        UserItemCallBack mCallBack;

        UserViewHolder(View itemView, Context context, UserItemCallBack callBack) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.list_item_profile_user_card_image_view);
            mUserNameText = itemView.findViewById(R.id.list_item_profile_user_card_name_text);
            mDateText = itemView.findViewById(R.id.list_item_profile_user_card_since_text);
            mContext = context;
            mCallBack = callBack;
        }

        void bind(final User user, int i) {
            GlideApp.with(mContext).load(user.getImageUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .error(GlideApp.with(mContext).load(R.drawable.default_profile_picture)
                            .apply(RequestOptions.circleCropTransform()))
                    .into(mImageView);
            mUserNameText.setText(user.getUserName());
            DateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            mDateText.setText(String.format("Started following on %s", df.format(user.getFollowDate())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onUserItemClicked(user.getId());
                }
            });
            //getting the pixel value of 8dp
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    r.getDisplayMetrics()
            );
            //add 8dp top margin to first item or remove it
            if (i == 0) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.topMargin = px;
                itemView.setLayoutParams(params);
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.topMargin = 0;
                itemView.setLayoutParams(params);
            }
        }

        public interface UserItemCallBack {
            void onUserItemClicked(String userId);
        }
    }
}

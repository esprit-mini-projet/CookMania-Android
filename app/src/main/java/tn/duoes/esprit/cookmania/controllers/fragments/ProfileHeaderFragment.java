package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.UserService;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

public class ProfileHeaderFragment extends Fragment {

    public static final String ARG_USER_ID = "user_id";

    private ImageView mUserImage;
    private ImageView mCameraImage;
    private ImageView mCoverImage;
    private TextView mFollowersText;
    private TextView mFollowingText;
    private TextView mUsernameText;
    private TextView mMembershipText;

    private User mUser;
    private UserService.GetUserByIdCallBack mUserCallback = new UserService.GetUserByIdCallBack() {
        @Override
        public void onCompletion(User user) {
            if(user == null){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Error")
                        .setMessage("There has been a connection error.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        })
                        .show();
                return;
            }
            mUser = user;
            UserService.getInstance().getUserCoverPhoto(mUser.getId(), mUserCoverCallback);
            updateUI();
        }
    };
    private UserService.GetUserCoverPhotoCallBack mUserCoverCallback = new UserService.GetUserCoverPhotoCallBack() {
        @Override
        public void onCompletion(String imageUrl) {
            GlideApp.with(ProfileHeaderFragment.this).load(Constants.UPLOAD_FOLDER_URL + "/" + imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.image_placeholder)
                    .error(GlideApp.with(ProfileHeaderFragment.this).load(R.drawable.image_placeholder)
                            .centerCrop())
                    .into(mCoverImage);
        }
    };

    public static ProfileHeaderFragment newInstance(String userId) {
        ProfileHeaderFragment fragment = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        getViewReferences(view);
        String connectedUserId = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        String userId = getArguments().getString(ARG_USER_ID);
        //hide or show camera
        if(connectedUserId.equals(userId)){
            mCameraImage.setVisibility(View.VISIBLE);
            mCameraImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
        UserService.getInstance().getUserById(userId, mUserCallback);

        return view;
    }

    private void updateUI() {
        GlideApp.with(this).load(mUser.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.default_profile_picture)
                .error(GlideApp.with(this).load(R.drawable.default_profile_picture).apply(RequestOptions.circleCropTransform()))
                .into(mUserImage);
        mFollowingText.setText(String.format(Locale.getDefault(), "Following: %d", mUser.getFollowing()));
        mFollowersText.setText(String.format(Locale.getDefault(), "Followers: %d", mUser.getFollowers()));
        mUsernameText.setText(mUser.getUserName());
        DateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        mMembershipText.setText(String.format("Member since %s", df.format(mUser.getDate())));
    }

    private void getViewReferences(View view) {
        mUserImage = view.findViewById(R.id.fragment_profile_header_user_image);
        mCameraImage = view.findViewById(R.id.fragment_profile_header_camera_image);
        mCoverImage = view.findViewById(R.id.fragment_profile_header_cover_image);
        mFollowersText = view.findViewById(R.id.fragment_profile_header_followers_text);
        mFollowingText = view.findViewById(R.id.fragment_profile_header_following_text);
        mUsernameText = view.findViewById(R.id.fragment_profile_header_user_name_text);
        mMembershipText = view.findViewById(R.id.fragment_profile_header_member_since_text);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

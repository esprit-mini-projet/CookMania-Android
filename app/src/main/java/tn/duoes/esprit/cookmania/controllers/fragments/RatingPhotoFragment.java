package tn.duoes.esprit.cookmania.controllers.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tn.duoes.esprit.cookmania.R;

public class RatingPhotoFragment extends Fragment {

    private static final String TAG = "RatingPhotoFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mImageView;
    private ImageButton mImageButton;
    private TextView mAddTextView;

    public static RatingPhotoFragment newInstance() {

        Bundle args = new Bundle();

        RatingPhotoFragment fragment = new RatingPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_photo, container, false);

        mAddTextView = view.findViewById(R.id.fragment_rating_photo_add_text);
        mImageView = view.findViewById(R.id.fragment_rating_photo_image_view);
        mImageButton = view.findViewById(R.id.fragment_rating_photo_button);
        //mImageButton.setOnClickListener();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            mImageView.setVisibility(View.VISIBLE);
            mImageButton.setVisibility(View.GONE);
            mAddTextView.setVisibility(View.GONE);
            //GlideApp.with(this).load(mCurrentPath).into(mImageView);
        }
    }
}

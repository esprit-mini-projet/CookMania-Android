package com.esprit.cookmania.controllers.fragments;

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

import com.esprit.cookmania.R;
import com.esprit.cookmania.utils.GlideApp;
import com.fxn.pix.Pix;

import java.util.ArrayList;

public class RatingPhotoFragment extends Fragment {

    private static final String TAG = "RatingPhotoFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mImageView;
    private ImageButton mDeleteButton;

    private RatingPhotoCallBack mCallBack;

    public static RatingPhotoFragment newInstance(RatingPhotoCallBack callBack) {

        Bundle args = new Bundle();

        RatingPhotoFragment fragment = new RatingPhotoFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callBack);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_photo, container, false);

        mImageView = view.findViewById(R.id.fragment_rating_photo_image_view);

        ImageButton imageButton = view.findViewById(R.id.fragment_rating_photo_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(RatingPhotoFragment.this, REQUEST_IMAGE_CAPTURE, 1);
            }
        });

        mDeleteButton = view.findViewById(R.id.fragment_rating_delete_button);
        mDeleteButton.setEnabled(false);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlideApp.with(RatingPhotoFragment.this)
                        .load(getActivity().getDrawable(R.drawable.image_placeholder))
                        .centerCrop()
                        .into(mImageView);
                mCallBack.onImageChangedListener(null);
                mDeleteButton.setEnabled(false);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            GlideApp.with(this).load(returnValue.get(0)).centerCrop().into(mImageView);
            mDeleteButton.setEnabled(true);
            mCallBack.onImageChangedListener(returnValue.get(0));
        }
    }

    public interface RatingPhotoCallBack {
        void onImageChangedListener(String path);
    }

    public void setCallBack(RatingPhotoCallBack callBack) {
        mCallBack = callBack;
    }
}

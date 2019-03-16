package com.esprit.cookmania.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.esprit.cookmania.R;

public class RatingBarDoneFragment extends Fragment {

    private static final String TAG = "RatingBarDoneFragment";
    private static final String ARG_RATING = "rating";
    private static final String ARG_DATE = "date";

    private RatingBarDoneCallBack mCallBack;

    public static RatingBarDoneFragment newInstance(int rating, String date, RatingBarDoneCallBack callBack) {

        Bundle args = new Bundle();
        args.putInt(ARG_RATING, rating);
        args.putString(ARG_DATE, date);

        RatingBarDoneFragment fragment = new RatingBarDoneFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callBack);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_bar_done, container, false);

        final Button deleteButton = view.findViewById(R.id.fragment_rating_bar_done_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onDeleteExperienceClickListener();
            }
        });

        AppCompatRatingBar ratingBar = view.findViewById(R.id.fragment_rating_bar_done_rating_bar);
        ratingBar.setNumStars(getArguments().getInt(ARG_RATING));
        ratingBar.setRating(getArguments().getInt(ARG_RATING));
        ratingBar.setIsIndicator(true);

        TextView dateText = view.findViewById(R.id.fragment_rating_bar_done_date);
        dateText.setText(getArguments().getString(ARG_DATE));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public interface RatingBarDoneCallBack{
        void onDeleteExperienceClickListener();
    }

    public void setCallBack(RatingBarDoneCallBack callBack) {
        mCallBack = callBack;
    }
}

package tn.duoes.esprit.cookmania.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import tn.duoes.esprit.cookmania.R;

public class RatingBarFragment extends Fragment {

    private static final String TAG = "RatingBarFragment";

    private List<String> mOpinions;
    private int mRating = 0;
    private RatingBarCallBack mCallBack;

    public static RatingBarFragment newInstance(RatingBarCallBack callBack) {

        Bundle args = new Bundle();

        RatingBarFragment fragment = new RatingBarFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callBack);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_bar, container, false);

        final TextView opinionText = view.findViewById(R.id.fragment_rating_bar_opinion_text);
        final Button submitButton = view.findViewById(R.id.fragment_rating_bar_submit_button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onSubmitClickListener(true);
            }
        });

        AppCompatRatingBar ratingBar = view.findViewById(R.id.fragment_rating_bar_rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRating = (int) rating;
                if(mRating == 0){
                    mCallBack.onSubmitClickListener(false);
                    mCallBack.onRatingChangedListener(mRating);
                    submitButton.setEnabled(false);
                    opinionText.setVisibility(View.INVISIBLE);
                    return;
                }
                submitButton.setEnabled(true);
                opinionText.setText(mOpinions.get(mRating - 1));
                opinionText.setVisibility(View.VISIBLE);
                mCallBack.onRatingChangedListener(mRating);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOpinions = Arrays.asList(getResources().getStringArray(R.array.rating_opinions));
    }

    public interface RatingBarCallBack{
        void onSubmitClickListener(boolean canSwipe);
        void onRatingChangedListener(int rating);
    }

    public RatingBarCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(RatingBarCallBack callBack) {
        mCallBack = callBack;
    }
}

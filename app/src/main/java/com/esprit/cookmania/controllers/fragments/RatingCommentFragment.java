package com.esprit.cookmania.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esprit.cookmania.R;

public class RatingCommentFragment extends Fragment {

    private static final String TAG = "RatingCommentFragment";

    private RatingCommentCallBack mCallBack;

    public static RatingCommentFragment newInstance(RatingCommentCallBack callBack) {

        Bundle args = new Bundle();

        RatingCommentFragment fragment = new RatingCommentFragment();
        fragment.setArguments(args);
        fragment.setCallBack(callBack);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_comment, container, false);

        final EditText commentInput = view.findViewById(R.id.fragment_rating_comment_input);
        final Button finishButton = view.findViewById(R.id.fragment_rating_comment_finish_button);
        finishButton.setEnabled(false);

        commentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                finishButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onFinishButtonClickListener(commentInput.getText().toString());
            }
        });

        return view;
    }

    public interface RatingCommentCallBack{
        void onFinishButtonClickListener(String comment);
    }

    public RatingCommentCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(RatingCommentCallBack callBack) {
        mCallBack = callBack;
    }
}

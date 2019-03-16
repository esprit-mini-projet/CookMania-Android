package com.esprit.cookmania.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esprit.cookmania.R;
import com.esprit.cookmania.models.Step;
import com.esprit.cookmania.utils.Constants;
import com.esprit.cookmania.utils.GlideApp;

import java.util.List;

public class RecipeDetailsStepAdapter extends RecyclerView.Adapter<RecipeDetailsStepAdapter.StepHolder> {

    private static final String TAG = "RecipeDetailsStepAdapte";

    private List<Step> mSteps;
    private StepItemCallBack mCallBack;

    public RecipeDetailsStepAdapter(List<Step> steps, StepItemCallBack callBack){
        mSteps = steps;
        mCallBack = callBack;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_step, viewGroup, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder stepHolder, int i) {
        stepHolder.bind(mSteps.get(i), i, mCallBack);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
    }

    static class StepHolder extends RecyclerView.ViewHolder{

        private FrameLayout mTopLine;
        private TextView mStepNumberTextView;
        private TextView mDescriptionTextView;
        private ImageView mImageView;
        private LinearLayout mTimeLayout;
        private FrameLayout mDottedRingLayout;

        StepHolder(@NonNull View itemView) {
            super(itemView);
            mTopLine = itemView.findViewById(R.id.step_item_top_line);
            mStepNumberTextView = itemView.findViewById(R.id.step_item_number);
            mDescriptionTextView = itemView.findViewById(R.id.step_item_description_text);
            mImageView = itemView.findViewById(R.id.step_item_image);
            mTimeLayout = itemView.findViewById(R.id.list_item_step_time_layout);
            mDottedRingLayout = itemView.findViewById(R.id.list_item_step_dotted_ring);
        }

        void bind(final Step step, int i, final StepItemCallBack callBack){
            if(i == 0){
                mTopLine.setVisibility(View.INVISIBLE);
            }else{
                mTopLine.setVisibility(View.VISIBLE);
            }
            if(step.getImageUrl() == null || step.getImageUrl().isEmpty()){
                mImageView.setVisibility(View.GONE);
            }else{
                mImageView.setVisibility(View.VISIBLE);
                GlideApp.with(mImageView).load(Constants.UPLOAD_FOLDER_URL + "/" + step.getImageUrl()).into(mImageView);
            }
            mDescriptionTextView.setText(step.getDescription());
            if(step.getTime() > 0){
                mStepNumberTextView.setText(step.getTime() + "'");
                mTimeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.onTimeLayoutClicked(step.getTime());
                    }
                });
                mDottedRingLayout.setVisibility(View.VISIBLE);
                RotateAnimation rotate = new RotateAnimation(
                        0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );

                rotate.setDuration(10000);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());
                mDottedRingLayout.startAnimation(rotate);
            }else{
                mStepNumberTextView.setText("");
                mTimeLayout.setOnClickListener(null);
                mDottedRingLayout.setVisibility(View.INVISIBLE);
                mDottedRingLayout.clearAnimation();
            }
        }
    }

    public interface StepItemCallBack{
        void onTimeLayoutClicked(int time);
    }
}

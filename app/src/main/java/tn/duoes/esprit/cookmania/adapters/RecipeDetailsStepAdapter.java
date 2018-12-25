package tn.duoes.esprit.cookmania.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Step;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

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

        StepHolder(@NonNull View itemView) {
            super(itemView);
            mTopLine = itemView.findViewById(R.id.step_item_top_line);
            mStepNumberTextView = itemView.findViewById(R.id.step_item_number);
            mDescriptionTextView = itemView.findViewById(R.id.step_item_description_text);
            mImageView = itemView.findViewById(R.id.step_item_image);
            mTimeLayout = itemView.findViewById(R.id.list_item_step_time_layout);
        }

        void bind(final Step step, int i, final StepItemCallBack callBack){
            if(i == 0){
                mTopLine.setVisibility(View.GONE);
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
            }else{
                mStepNumberTextView.setText("");
                mTimeLayout.setOnClickListener(null);
            }
        }
    }

    public interface StepItemCallBack{
        void onTimeLayoutClicked(int time);
    }
}

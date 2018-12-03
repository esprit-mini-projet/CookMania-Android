package tn.duoes.esprit.cookmania.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Step;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.GlideApp;

public class RecipeDetailsStepAdapter extends RecyclerView.Adapter<RecipeDetailsStepAdapter.StepHolder> {

    private static final String TAG = "RecipeDetailsStepAdapte";

    private List<Step> mSteps;

    public RecipeDetailsStepAdapter(List<Step> steps){
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_step, viewGroup, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder stepHolder, int i) {
        stepHolder.bind(mSteps.get(i), i);
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

        StepHolder(@NonNull View itemView) {
            super(itemView);
            mTopLine = itemView.findViewById(R.id.step_item_top_line);
            mStepNumberTextView = itemView.findViewById(R.id.step_item_number);
            mDescriptionTextView = itemView.findViewById(R.id.step_item_description_text);
            mImageView = itemView.findViewById(R.id.step_item_image);
        }

        void bind(Step step, int i){
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
            mStepNumberTextView.setText("" + (i + 1));
        }
    }
}

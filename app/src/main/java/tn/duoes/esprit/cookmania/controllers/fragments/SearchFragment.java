package tn.duoes.esprit.cookmania.controllers.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.LabelCategory;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.MesurementConvertionUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = SearchFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter ap_1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View collapsablelayout;
    private FlexboxLayout labelsFlexBox;
    private ImageView collapseArrowIV;
    private int finalHeight;
    private boolean isCollapsed = false;
    private List<String> labels = new ArrayList<>();

    View.OnClickListener labelClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button)v;
            boolean isSelected = Boolean.valueOf((String)button.getTag());
            if(!isSelected){
                labels.add(button.getText().toString());
                button.setBackground(getContext().getDrawable(R.drawable.shape_selected_label));
                button.setTextColor(getContext().getResources().getColorStateList(R.color.colorAccent));
            }else{
                labels.remove(button.getText().toString());
                button.setBackground(getContext().getDrawable(R.drawable.shape_unselected_label));
                button.setTextColor(getContext().getResources().getColorStateList(R.color.colorPrimary));
            }
            button.setTag(String.valueOf(!isSelected));
        }
    };

    private void insertLabelButton(String label){
        Button button = new Button(getContext());
        button.setOnClickListener(labelClicked);

        button.setBackground(getResources().getDrawable(R.drawable.shape_unselected_label));
        button.setPadding(MesurementConvertionUtils.dpToPx(20, getContext()), 0, MesurementConvertionUtils.dpToPx(20, getContext()), 0);
        button.setText(label);
        button.setTextSize(16);
        button.setTypeface(button.getTypeface(), Typeface.BOLD);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));

        labelsFlexBox.addView(button);
        FlexboxLayout.LayoutParams flp = (FlexboxLayout.LayoutParams) button.getLayoutParams();
        flp.setMargins(0, 0, MesurementConvertionUtils.dpToPx(15, getContext()), MesurementConvertionUtils.dpToPx(15, getContext()));
        flp.setFlexGrow(1);
    }

    private void insertCategoryTextView(String category){
        TextView textView = new TextView(getContext());

        textView.setText(category);
        textView.setTextSize(18);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(Color.parseColor("#000000"));

        labelsFlexBox.addView(textView);
        FlexboxLayout.LayoutParams flp = (FlexboxLayout.LayoutParams) textView.getLayoutParams();
        flp.setMargins(MesurementConvertionUtils.dpToPx(10, getContext()), 0, 0, MesurementConvertionUtils.dpToPx(10, getContext()));
        flp.setFlexBasisPercent(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_search, container, false);

        labelsFlexBox = fragment.findViewById(R.id.label_flexlayout);

        RecipeService.getInstance().getLabels(new RecipeService.LabelGetCallBack() {
            @Override
            public void onResponse(List<LabelCategory> categories) {
                for (LabelCategory category : categories){
                    insertCategoryTextView(category.getCategory());
                    for (String label : category.getLabels()){
                        insertLabelButton(label);
                    }
                }
                Log.d(TAG, "onResponse: "+categories);
            }

            @Override
            public void onFailure() {

            }
        });
        collapsablelayout = fragment.findViewById(R.id.search_filter_collapsable);
        collapseArrowIV = fragment.findViewById(R.id.collapse_arrow_iv);
        fragment.findViewById(R.id.search_filters_card_drop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collapsablelayout.getVisibility()==View.GONE)
                {
                    expand();
                }
                else
                {
                    collapse();
                }
            }
        });
        finalHeight = collapsablelayout.getHeight();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            collapse();
        }
    }

    private void collapse() {
        if(!isCollapsed){
            finalHeight = collapsablelayout.getHeight();

            collapseArrowIV.animate().rotation(90).setDuration(300);

            ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //Height=0, but it set visibility to GONE
                    collapsablelayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

            });
            mAnimator.start();
            isCollapsed = true;
        }
    }

    private void expand()
    {
        if(isCollapsed){
            collapsablelayout.setVisibility(View.VISIBLE);
            collapseArrowIV.animate().rotation(0).setDuration(300);

            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            collapsablelayout.measure(widthSpec, heightSpec);

            ValueAnimator mAnimator = slideAnimator(0, finalHeight);
            mAnimator.start();
            isCollapsed = false;
        }
    }

    private ValueAnimator slideAnimator(int start, int end)
    {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = collapsablelayout.getLayoutParams();
                layoutParams.height = value;
                collapsablelayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

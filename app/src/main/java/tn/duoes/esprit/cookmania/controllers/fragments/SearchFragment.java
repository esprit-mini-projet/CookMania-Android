package tn.duoes.esprit.cookmania.controllers.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import io.apptik.widget.MultiSlider;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.HorizontalCategoryRecipeRecyclerViewAdapter;
import tn.duoes.esprit.cookmania.adapters.SearchResultRecyclerViewAdapter;
import tn.duoes.esprit.cookmania.controllers.activities.MainScreenActivity;
import tn.duoes.esprit.cookmania.models.LabelCategory;
import tn.duoes.esprit.cookmania.models.Recipe;
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
    private RadioButton ratingSortButton, caloriesSortButton;
    private boolean ratingUp = true, caloriesUp = false, ratingIsSelected = true, caloriesIsSelected = false;
    private int finalHeight;
    private boolean isCollapsed = false;
    private List<String> labels = new ArrayList<>();
    private MultiSlider servingsSlider;
    private TextView minServingTV, maxServingTV;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchResultRecyclerViewAdapter mAdapter;
    private List<Recipe> mRecipes;

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
        button.setPadding(MesurementConvertionUtils.dpToPx(15, getContext()), 0, MesurementConvertionUtils.dpToPx(15, getContext()), 0);
        button.setText(label);
        button.setTextSize(14);
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
        setHasOptionsMenu(true);

        //Loading views
        labelsFlexBox = fragment.findViewById(R.id.label_flexlayout);
        servingsSlider = fragment.findViewById(R.id.search_servings_slider);
        minServingTV = fragment.findViewById(R.id.search_servings_min);
        maxServingTV = fragment.findViewById(R.id.search_sevings_max);
        collapsablelayout = fragment.findViewById(R.id.search_filter_collapsable);
        collapseArrowIV = fragment.findViewById(R.id.collapse_arrow_iv);
        ratingSortButton = fragment.findViewById(R.id.sortRating);
        caloriesSortButton = fragment.findViewById(R.id.sortCalories);
        MaterialSearchView searchView = getActivity().findViewById(R.id.searchview);
        mRecyclerView = fragment.findViewById(R.id.search_result);

        //Settings
        finalHeight = collapsablelayout.getHeight();
        caloriesSortButton.getCompoundDrawables()[2].setTint(getResources().getColor(R.color.colorPrimary));
        //Setting recycler view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new SearchResultRecyclerViewAdapter((MainScreenActivity)getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //Loading data
        //Labels
        RecipeService.getInstance().getLabels(new RecipeService.LabelGetCallBack() {
            @Override
            public void onResponse(List<LabelCategory> categories) {
                for (LabelCategory category : categories){
                    insertCategoryTextView(category.getCategory());
                    for (String label : category.getLabels()){
                        insertLabelButton(label);
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        });
        //Recipes
        RecipeService.getInstance().getTopRatedRecipes(new RecipeService.RecipeServiceGetCallBack() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                mAdapter.recipes = recipes;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });

        //Listeners
        servingsSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                switch (thumbIndex){
                    case 0:
                        minServingTV.setText(String.valueOf(value));
                        break;
                    case 1:
                        maxServingTV.setText(String.valueOf(value));
                        break;
                }
            }
        });

        servingsSlider.setOnTrackingChangeListener(new MultiSlider.OnTrackingChangeListener() {
            @Override
            public void onStartTrackingTouch(MultiSlider multiSlider, MultiSlider.Thumb thumb, int value) {
                
            }

            @Override
            public void onStopTrackingTouch(MultiSlider multiSlider, MultiSlider.Thumb thumb, int value) {
                if(thumb == multiSlider.getThumb(0)){
                    Log.d(TAG, "onStopTrackingTouch: min");
                }else{
                    Log.d(TAG, "onStopTrackingTouch: max");
                }
            }
        });

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

        ratingSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caloriesIsSelected = false;
                Log.d(TAG, "onClick: "+ratingIsSelected);
                if(ratingIsSelected){
                    ratingUp = !ratingUp;
                    ratingSortButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, ratingUp?R.drawable.ic_icon_arrow_up:R.drawable.ic_icon_arrow_down, 0);
                }
                ratingSortButton.getCompoundDrawables()[2].setTint(getResources().getColor(R.color.white));
                caloriesSortButton.getCompoundDrawables()[2].setTint(getResources().getColor(R.color.colorPrimary));
                ratingIsSelected = true;
            }
        });

        caloriesSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingIsSelected = false;
                Log.d(TAG, "onClick: "+caloriesIsSelected);
                if(caloriesIsSelected){
                    caloriesUp = !caloriesUp;
                    caloriesSortButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, caloriesUp?R.drawable.ic_icon_arrow_up:R.drawable.ic_icon_arrow_down, 0);
                }
                caloriesSortButton.getCompoundDrawables()[2].setTint(getResources().getColor(R.color.white));
                ratingSortButton.getCompoundDrawables()[2].setTint(getResources().getColor(R.color.colorPrimary));
                caloriesIsSelected = true;
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: "+newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (mAdapter.recipeDialog.getDialog() != null){
                        MainScreenActivity.viewPager.setPagingEnabled(true);
                        mAdapter.recipeDialog.dismiss();
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            close();
        }
    }

    private void close(){
        if(!isCollapsed){
            finalHeight = collapsablelayout.getHeight();
            Log.d(TAG, "close: "+finalHeight);
            ViewGroup.LayoutParams params = collapsablelayout.getLayoutParams();
            params.height = 0;
            collapsablelayout.setLayoutParams(params);
            collapsablelayout.setVisibility(View.GONE);
            collapseArrowIV.animate().rotation(90).setDuration(300);
            isCollapsed = true;
        }
    }

    private void collapse() {
        if(!isCollapsed){
            Log.d(TAG, "collapse: "+finalHeight);
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
            finalHeight = collapsablelayout.getHeight();
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

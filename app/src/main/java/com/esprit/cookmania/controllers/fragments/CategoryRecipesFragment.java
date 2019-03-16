package com.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esprit.cookmania.R;
import com.esprit.cookmania.adapters.HorizontalCategoryRecipeRecyclerViewAdapter;
import com.esprit.cookmania.controllers.activities.CategoryRecipesActivity;
import com.esprit.cookmania.controllers.activities.MainScreenActivity;
import com.esprit.cookmania.models.Recipe;
import com.esprit.cookmania.utils.NavigationUtils;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryRecipesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String CATEGORY_NAME_KEY = "categoryName";
    public static final String RECIPES_KEY = "recipes";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoryRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryRecipesFragment newInstance(String param1, String param2) {
        CategoryRecipesFragment fragment = new CategoryRecipesFragment();
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

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HorizontalCategoryRecipeRecyclerViewAdapter mAdapter;
    private List<Recipe> mRecipes;
    private String mCategoryName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_categorie_recipes, container, false);

        Bundle bundle = getArguments();
        mRecipes = Objects.requireNonNull(bundle).getParcelableArrayList(RECIPES_KEY);
        mCategoryName = bundle.getString(CATEGORY_NAME_KEY);

        TextView categoryNameTV = fragment.findViewById(R.id.category_name_tv);
        categoryNameTV.setText(mCategoryName);

        fragment.findViewById(R.id.category_more_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NavigationUtils.getNavigationFormattedIntent(getContext(), CategoryRecipesActivity.class);
                intent.putExtra(CategoryRecipesActivity.CATEGORY_NAME_KEY, mCategoryName);
                startActivity(intent);
            }
        });

        mRecyclerView = fragment.findViewById(R.id.categorie_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HorizontalCategoryRecipeRecyclerViewAdapter(mRecipes, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if (mAdapter.recipeDialog.getDialog() != null){
                        MainScreenActivity.viewPager.setPagingEnabled(true);
                        HomeFragment.scrollView.setScrollingEnabled(true);
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

    public void updateRecipies(List<Recipe> recipes){
        mAdapter.mRecipes = mRecipes = recipes;
        mAdapter.notifyDataSetChanged();
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

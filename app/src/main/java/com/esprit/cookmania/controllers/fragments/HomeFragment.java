package com.esprit.cookmania.controllers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.cookmania.R;
import com.esprit.cookmania.models.Recipe;
import com.esprit.cookmania.services.RecipeService;
import com.esprit.cookmania.views.CustomScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = HomeFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter ap_1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private SuggestedFragment suggestedFragment;
    private CategoryRecipesFragment topRatedFragment;
    private CategoryRecipesFragment healthyFragment;
    private CategoryRecipesFragment cheapFragment;
    private FeedFragment feedFragment;
    public static CustomScrollView scrollView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Stack<Integer> loadersStack = new Stack<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        suggestedFragment = SuggestedFragment.newInstance(null, null);
        feedFragment = FeedFragment.newInstance(null, null);
    }

    private CategoryRecipesFragment buildCategoryFragment(String name, List<Recipe> recipes){
        CategoryRecipesFragment fragment = CategoryRecipesFragment.newInstance(null, null);
        Bundle bundle = new Bundle();
        bundle.putString(CategoryRecipesFragment.CATEGORY_NAME_KEY, name);
        bundle.putParcelableArrayList(CategoryRecipesFragment.RECIPES_KEY, (ArrayList)recipes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home, container, false);

        scrollView = fragment.findViewById(R.id.home_scroll);

        (swipeRefreshLayout = fragment.findViewById(R.id.home_refresh_layout)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadersStack.push(1);
                feedFragment.updateData(new FeedFragment.FeedUpdateCallback() {
                    @Override
                    public void updateFinished() {
                        loadersStack.pop();
                        if(loadersStack.empty()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

                loadersStack.push(2);
                suggestedFragment.updateSuggested(new SuggestedFragment.SuggestedUpdatedCallback() {
                    @Override
                    public void updateFinished() {
                        loadersStack.pop();
                        if(loadersStack.empty()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

                loadersStack.push(2);
                RecipeService.getInstance().getTopRatedRecipes(new RecipeService.RecipeServiceGetCallBack() {
                    @Override
                    public void onResponse(List<Recipe> recipes) {
                        topRatedFragment.updateRecipies(recipes);
                        loadersStack.pop();
                        if(loadersStack.empty()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure() {}
                });
                loadersStack.push(4);
                RecipeService.getInstance().getTopRatedRecipes(new RecipeService.RecipeServiceGetCallBack() {
                    @Override
                    public void onResponse(List<Recipe> recipes) {
                        healthyFragment.updateRecipies(recipes);
                        loadersStack.pop();
                        if(loadersStack.empty()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure() {}
                });
                loadersStack.push(5);
                RecipeService.getInstance().getTopRatedRecipes(new RecipeService.RecipeServiceGetCallBack() {
                    @Override
                    public void onResponse(List<Recipe> recipes) {
                        cheapFragment.updateRecipies(recipes);
                        loadersStack.pop();
                        if(loadersStack.empty()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure() {}
                });
            }
        });

        //Fragment suggestedFragment = SuggestedFragment.newInstance(null, null);
        getFragmentManager().beginTransaction().replace(R.id.home_suggested_container, suggestedFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.home_feed_container, feedFragment).commit();


        if(topRatedFragment == null || healthyFragment == null || cheapFragment == null){
            //Top rated recipes
            final ProgressDialog topRatedProgressDialog = new ProgressDialog(getContext());
            topRatedProgressDialog.setMessage("Loading...");
            topRatedProgressDialog.show();

            RecipeService.getInstance().getTopRatedRecipes(new RecipeService.RecipeServiceGetCallBack() {
                @Override
                public void onResponse(List<Recipe> recipes) {
                    topRatedFragment = buildCategoryFragment("Top rated", recipes);
                    topRatedProgressDialog.dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_toprated_container, topRatedFragment).commit();
                }

                @Override
                public void onFailure() {
                    topRatedProgressDialog.dismiss();
                }
            });

            //Healthy recipes
            final ProgressDialog healthyProgressDialog = new ProgressDialog(getContext());
            healthyProgressDialog.setMessage("Loading...");
            healthyProgressDialog.show();

            RecipeService.getInstance().getHealthyRecipes(new RecipeService.RecipeServiceGetCallBack() {
                @Override
                public void onResponse(List<Recipe> recipes) {
                    healthyFragment = buildCategoryFragment("Healthy", recipes);
                    healthyProgressDialog.dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_healthy_container, healthyFragment).commit();
                }

                @Override
                public void onFailure() {
                    healthyProgressDialog.dismiss();
                }
            });

            //Cheap recipes
            final ProgressDialog cheapProgressDialog = new ProgressDialog(getContext());
            healthyProgressDialog.setMessage("Loading...");
            healthyProgressDialog.show();

            RecipeService.getInstance().getCheapRecipes(new RecipeService.RecipeServiceGetCallBack() {
                @Override
                public void onResponse(List<Recipe> recipes) {
                    cheapFragment = buildCategoryFragment("Cheap", recipes);
                    cheapProgressDialog.dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_cheap_container, cheapFragment).commitAllowingStateLoss();
                }

                @Override
                public void onFailure() {
                    cheapProgressDialog.dismiss();
                }
            });

            return fragment;
        }
        getFragmentManager().beginTransaction().replace(R.id.home_toprated_container, topRatedFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.home_healthy_container, healthyFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.home_cheap_container, cheapFragment).commit();

        return fragment;
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

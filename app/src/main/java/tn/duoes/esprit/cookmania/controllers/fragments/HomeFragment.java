package tn.duoes.esprit.cookmania.controllers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.ShoppingListActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home, container, false);

        Fragment suggestedFragment = SuggestedFragment.newInstance(null, null);
        getFragmentManager().beginTransaction().replace(R.id.home_suggested_container, suggestedFragment).commit();

        //Top rated recipes
        final ProgressDialog toRatedProgressDialog = new ProgressDialog(getContext());
        toRatedProgressDialog.setMessage("Loading...");
        toRatedProgressDialog.show();

        RecipeService.getTopRatedRecipes(getContext(), new RecipeService.DataListener() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                Fragment fragment = CategoryRecipesFragment.newInstance(null, null);
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", "Top rated");
                bundle.putParcelableArrayList("recipes", (ArrayList)recipes);
                fragment.setArguments(bundle);

                toRatedProgressDialog.dismiss();

                getFragmentManager().beginTransaction().replace(R.id.home_toprated_container, fragment).commit();
            }

            @Override
            public void onError(VolleyError error) {
                System.out.println(error);
                toRatedProgressDialog.dismiss();
            }
        });

        //Healthy recipes
        final ProgressDialog healthyProgressDialog = new ProgressDialog(getContext());
        healthyProgressDialog.setMessage("Loading...");
        healthyProgressDialog.show();

        RecipeService.getHealthyRecipes(getContext(), new RecipeService.DataListener() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                Fragment fragment = CategoryRecipesFragment.newInstance(null, null);
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", "Healthy");
                bundle.putParcelableArrayList("recipes", (ArrayList)recipes);
                fragment.setArguments(bundle);

                healthyProgressDialog.dismiss();

                getFragmentManager().beginTransaction().replace(R.id.home_healthy_container, fragment).commit();
            }

            @Override
            public void onError(VolleyError error) {
                System.out.println(error);
                healthyProgressDialog.dismiss();
            }
        });

        //Cheap recipes
        final ProgressDialog cheapProgressDialog = new ProgressDialog(getContext());
        healthyProgressDialog.setMessage("Loading...");
        healthyProgressDialog.show();

        RecipeService.getCheapRecipes(getContext(), new RecipeService.DataListener() {
            @Override
            public void onResponse(List<Recipe> recipes) {
                Fragment fragment = CategoryRecipesFragment.newInstance(null, null);
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", "Cheap");
                bundle.putParcelableArrayList("recipes", (ArrayList)recipes);
                fragment.setArguments(bundle);

                cheapProgressDialog.dismiss();

                getFragmentManager().beginTransaction().replace(R.id.home_cheap_container, fragment).commit();
            }

            @Override
            public void onError(VolleyError error) {
                System.out.println(error);
                cheapProgressDialog.dismiss();
            }
        });

        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_menu_shopping:
                Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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

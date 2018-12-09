package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.AddStepActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.services.RecipeService;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = AddRecipeFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipeFragment newInstance(String param1, String param2) {
        AddRecipeFragment fragment = new AddRecipeFragment();
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

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        toAddStepActivity(new Recipe());
        imageView = fragment.findViewById(R.id.add_recipe_image_view);
        fragment.findViewById(R.id.add_recipe_add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickSetup pickSetup = new PickSetup();
                pickSetup.setGalleryIcon(R.mipmap.gallery_colored);
                pickSetup.setCameraIcon(R.mipmap.camera_colored);
                PickImageDialog.build(pickSetup).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                            Glide.with(getActivity()).load(pickResult.getUri()).into(imageView);
                        } else {
                            Toast.makeText(getContext(), pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).show(getFragmentManager());
            }
        });
        fragment.findViewById(R.id.add_recipe_next_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameEditText;
                if((nameEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_name_layout)).getEditText())).getText().toString().isEmpty()){
                    nameEditText.setError("Recipe name is required!");
                    return;
                }

                EditText descriptionEditText;
                if((descriptionEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_description_layout)).getEditText())).getText().toString().isEmpty()){
                    descriptionEditText.setError("Recipe description is required!");
                    return;
                }

                EditText durationEditText;
                if((durationEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_duration_layout)).getEditText())).getText().toString().isEmpty()){
                    durationEditText.setError("Recipe duration is required!");
                    return;
                }

                EditText servingsEditText;
                if((servingsEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_servings_layout)).getEditText())).getText().toString().isEmpty()){
                    servingsEditText.setError("Recipe servings is required!");
                    return;
                }

                EditText caloriesEditText;
                if((caloriesEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_calories_layout)).getEditText())).getText().toString().isEmpty()){
                    caloriesEditText.setError("Recipe calories is required!");
                    return;
                }

                Recipe recipe = new Recipe(
                        nameEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        Integer.valueOf(caloriesEditText.getText().toString()),
                        Integer.valueOf(servingsEditText.getText().toString()),
                        "",
                        new Date(),
                        0,
                        0,
                        Integer.valueOf(durationEditText.getText().toString()),
                        getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("user_id", ""),
                        0.0f);
                toAddStepActivity(recipe);
            }
        });

        return fragment;
    }

    private void toAddStepActivity(Recipe recipe){
        Intent intent = NavigationUtils.getNavigationFormattedIntent(getContext(), AddStepActivity.class);
        Gson gson = new Gson();
        intent.putExtra(AddStepActivity.RECIPE_KEY, gson.toJson(recipe));
        startActivity(intent);
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

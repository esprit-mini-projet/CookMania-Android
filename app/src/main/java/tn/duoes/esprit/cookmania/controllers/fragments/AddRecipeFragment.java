package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
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
    public static final int PICK_REQUEST_CODE = 100;
    public static final int STEP_REQUEST_CODE = 110;

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
    private Button addImageBTError;
    private File image;
    private List<String> labels = new ArrayList<>();
    TextView labelTV;

    View.OnClickListener labelCliecked = new View.OnClickListener() {
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
            labelTV.setError(null);
        }
    };

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText durationEditText;
    private EditText servingsEditText;
    private EditText caloriesEditText;

    private Button healthyButton;
    private Button cheapButton;
    private Button easyButton;
    private Button fastButton;
    private Button vegButton;
    private Button kidsButton;
    private Button breakfastButton;
    private Button dinnerButton;
    private Button dateButton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == AddRecipeFragment.PICK_REQUEST_CODE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Glide.with(getActivity()).load(returnValue.get(0)).into(imageView);
            image = new File(returnValue.get(0));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: image: "+image+" labels: "+labels);
        final View fragment = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        imageView = fragment.findViewById(R.id.add_recipe_image_view);
        labelTV = fragment.findViewById(R.id.add_recipe_label_tv);
        addImageBTError = fragment.findViewById(R.id.add_recipe_add_image_error);
        fragment.findViewById(R.id.add_recipe_add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(AddRecipeFragment.this, AddRecipeFragment.PICK_REQUEST_CODE , 1);
                addImageBTError.setError(null);
            }
        });
        fragment.findViewById(R.id.add_recipe_next_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image == null){
                    addImageBTError.setError("Recipe image is required!");
                    addImageBTError.requestFocus();
                    return;
                }

                if((nameEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_name_layout)).getEditText())).getText().toString().isEmpty()){
                    nameEditText.setError("Recipe name is required!");
                    nameEditText.requestFocus();
                    return;
                }

                if((descriptionEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_description_layout)).getEditText())).getText().toString().isEmpty()){
                    descriptionEditText.setError("Recipe description is required!");
                    descriptionEditText.requestFocus();
                    return;
                }

                if((durationEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_duration_layout)).getEditText())).getText().toString().isEmpty()){
                    durationEditText.setError("Recipe duration is required!");
                    durationEditText.requestFocus();
                    return;
                }

                if((servingsEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_servings_layout)).getEditText())).getText().toString().isEmpty()){
                    servingsEditText.setError("Recipe servings is required!");
                    servingsEditText.requestFocus();
                    return;
                }

                if((caloriesEditText = (((TextInputLayout)fragment.findViewById(R.id.recipe_calories_layout)).getEditText())).getText().toString().isEmpty()){
                    caloriesEditText.setError("Recipe calories is required!");
                    caloriesEditText.requestFocus();
                    return;
                }

                if(labels.isEmpty()){
                    labelTV.setError("At least one label must be selected!");
                    labelTV.requestFocus();
                    return;
                }

                Recipe recipe = new Recipe(
                        nameEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        Integer.valueOf(caloriesEditText.getText().toString()),
                        Integer.valueOf(servingsEditText.getText().toString()),
                        Integer.valueOf(durationEditText.getText().toString()),
                        getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("user_id", ""),
                        image,
                        labels);
                toAddStepActivity(recipe);
            }
        });

        (healthyButton = fragment.findViewById(R.id.Healthy)).setOnClickListener(labelCliecked);
        (cheapButton = fragment.findViewById(R.id.Cheap)).setOnClickListener(labelCliecked);
        (easyButton = fragment.findViewById(R.id.Easy)).setOnClickListener(labelCliecked);
        (fastButton = fragment.findViewById(R.id.Fast)).setOnClickListener(labelCliecked);
        (vegButton = fragment.findViewById(R.id.Vegetarian)).setOnClickListener(labelCliecked);
        (kidsButton = fragment.findViewById(R.id.For_Kids)).setOnClickListener(labelCliecked);
        (breakfastButton = fragment.findViewById(R.id.Breakfast)).setOnClickListener(labelCliecked);
        (dinnerButton = fragment.findViewById(R.id.Dinner)).setOnClickListener(labelCliecked);
        (dateButton = fragment.findViewById(R.id.Date_Night)).setOnClickListener(labelCliecked);

        return fragment;
    }

    private void toAddStepActivity(Recipe recipe){
        Intent intent = NavigationUtils.getNavigationFormattedIntent(getContext(), AddStepActivity.class);
        Gson gson = new Gson();
        intent.putExtra(AddStepActivity.RECIPE_KEY, gson.toJson(recipe));
        startActivityForResult(intent, STEP_REQUEST_CODE);
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

package tn.duoes.esprit.cookmania.controllers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Suggestion;
import tn.duoes.esprit.cookmania.services.SuggestionService;
import tn.duoes.esprit.cookmania.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SuggestedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SuggestedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SuggestedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter ap_1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuggestedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestedFragment newInstance(String param1, String param2) {
        SuggestedFragment fragment = new SuggestedFragment();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.fragment_suggested, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        SuggestionService.getInstance().getSuggestions(new SuggestionService.SuggestionServiceCallback() {
            @Override
            public void onResponse(Suggestion suggestion) {
                TextView suggestionTitleTV = fragment.findViewById(R.id.suggested_title_tv);
                suggestionTitleTV.setText(suggestion.getTitle());


                ImageView firstImageView = fragment.findViewById(R.id.suggested_first);
                ImageView secondImageView = fragment.findViewById(R.id.suggested_second);
                ImageView thirdImageView = fragment.findViewById(R.id.suggested_third);
                ImageView forthImageView = fragment.findViewById(R.id.suggested_forth);

                Glide.with(fragment).load(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(0).getImageURL()).into(firstImageView);
                Glide.with(fragment).load(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(1).getImageURL()).into(secondImageView);
                Glide.with(fragment).load(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(2).getImageURL()).into(thirdImageView);
                Glide.with(fragment).load(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(3).getImageURL()).into(forthImageView);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
            }
        });
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

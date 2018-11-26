package tn.duoes.esprit.cookmania.controllers.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.fragment_suggested, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        SuggestionService.getSuggestions(getContext(), new SuggestionService.DataListener() {
            @Override
            public void onResponse(Suggestion suggestion) {
                TextView suggestionTitleTV = fragment.findViewById(R.id.suggested_title_tv);
                suggestionTitleTV.setText(suggestion.getTitle());


                NetworkImageView firstImageView = fragment.findViewById(R.id.suggested_first);
                NetworkImageView secondImageView = fragment.findViewById(R.id.suggested_second);
                NetworkImageView thirdImageView = fragment.findViewById(R.id.suggested_third);
                NetworkImageView forthImageView = fragment.findViewById(R.id.suggested_forth);

                RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
                ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                    public void putBitmap(String url, Bitmap bitmap) {
                        mCache.put(url, bitmap);
                    }
                    public Bitmap getBitmap(String url) {
                        return mCache.get(url);
                    }
                });

                firstImageView.setImageUrl(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(0).getImageURL(), mImageLoader);
                secondImageView.setImageUrl(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(1).getImageURL(), mImageLoader);
                thirdImageView.setImageUrl(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(2).getImageURL(), mImageLoader);
                forthImageView.setImageUrl(Constants.UPLOAD_FOLDER_URL+"/"+suggestion.getRecipes().get(3).getImageURL(), mImageLoader);

                progressDialog.dismiss();
            }

            @Override
            public void onError(VolleyError error) {
                System.out.println(error);
                progressDialog.dismiss();
            }
        });


        /*firstImageView.setImageResource(R.drawable.im_1);
        secondImageView.setImageResource(R.drawable.im_2);
        thirdImageView.setImageResource(R.drawable.im_3);
        forthImageView.setImageResource(R.drawable.im_4);*/
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

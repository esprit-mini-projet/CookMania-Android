package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import tn.duoes.esprit.cookmania.ProfileActivity;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.UserService;

public class MainLoginFragment extends Fragment {

    private static final String TAG = "MainLoginFragment";

    private static final int RC_SIGN_IN_GOOGLE = 1;


    public static final String PER_FB_PUBLIC = "public_profile";

    private LoginButton mFacebookButton;
    private SignInButton mGoogleButton;
    private Button mEmailButton;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    public static MainLoginFragment newInstance() {

        Bundle args = new Bundle();

        MainLoginFragment fragment = new MainLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_login, container, false);
        mFacebookButton = v.findViewById(R.id.button_login_fb);
        mGoogleButton = v.findViewById(R.id.button_login_google);
        mEmailButton = v.findViewById(R.id.button_login_email);

        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        mFacebookButton.setFragment(this);
        mFacebookButton.setReadPermissions(Arrays.asList(PER_FB_PUBLIC));
        mFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: id: " + loginResult.getAccessToken().getUserId());
                retrieveUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: ");
                showErrorAlert();
            }
        });
        return v;
    }

    private void retrieveUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(object != null){
                            Log.d(TAG, "onCompleted: " + object.toString());
                            User user = new User();
                            try {
                                user.setId("f_" + object.getString("id"));
                                user.setUserName(object.getString("name"));
                                user.setEmail(object.getString("email"));
                                user.setPassword("");
                                JSONObject picture = object.getJSONObject("picture");
                                JSONObject data = picture.getJSONObject("data");
                                user.setImageUrl(data.getString("url"));
                                Log.d(TAG, "onCompleted: user: " + user);
                                createUser(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showErrorAlert();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void createUser(User user) {
        UserService.getInstance().create(user, new UserService.UserServiceCallBack() {
            @Override
            public void onCreateCompleted(String id) {
                if(id == null){
                    showErrorAlert();
                    return;
                }
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void showErrorAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage("We're sorry. An error has occured!")
                .setTitle("Oops")
                .setPositiveButton("ok", null)
                .show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

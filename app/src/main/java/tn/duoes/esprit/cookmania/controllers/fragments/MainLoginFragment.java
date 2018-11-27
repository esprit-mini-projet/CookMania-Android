package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
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
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import tn.duoes.esprit.cookmania.controllers.activities.MainScreenActivity;
import tn.duoes.esprit.cookmania.controllers.activities.ProfileActivity;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.UserService;
import tn.duoes.esprit.cookmania.utils.Constants;

public class MainLoginFragment extends Fragment {

    private static final String TAG = "MainLoginFragment";

    private static final int RC_SIGN_IN_GOOGLE = 1;
    public static final String PER_FB_PUBLIC = "public_profile";
    public static final String PREFS_NAME = "prefs";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_SIGNIN_METHOD = "signin_method";
    public static final String METHOD_FACEBOOK = "facebook";
    public static final String METHOD_GOOGLE = "google";
    public static final String PREF_IMAGE_URL = "image_url";
    public static final String PREF_USERNAME = "username";

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private View.OnClickListener mOnEmailButtonClickListener;

    private LinearLayout mProgressBar;

    public static MainLoginFragment newInstance(View.OnClickListener onClickListener) {

        Bundle args = new Bundle();

        MainLoginFragment fragment = new MainLoginFragment();
        fragment.setArguments(args);
        fragment.setOnEmailButtonClickListener(onClickListener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_login, container, false);
        LoginButton facebookButton = v.findViewById(R.id.button_login_fb);
        SignInButton googleButton = v.findViewById(R.id.button_login_google);
        Button emailButton = v.findViewById(R.id.button_login_email);
        mProgressBar = v.findViewById(R.id.fragment_login_progress_bar);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        facebookButton.setFragment(this);
        facebookButton.setReadPermissions(Arrays.asList(PER_FB_PUBLIC));
        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: id: " + loginResult.getAccessToken().getUserId());
                retrieveFbUserProfile(loginResult.getAccessToken());
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

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnEmailButtonClickListener.onClick(v);
            }
        });

        return v;
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void retrieveFbUserProfile(final AccessToken accessToken) {
        showProgressBar();
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
                                user.setImageUrl("https://graph.facebook.com/" + accessToken.getUserId() + "/picture?height=600");
                                Log.d(TAG, "onCompleted: user: " + user);
                                loginOrCreateFromSocialMedia(user, METHOD_FACEBOOK);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                hideProgressBar();
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

    private void loginOrCreateFromSocialMedia(final User user, final String signInMethod) {
        UserService.getInstance().createFromSocialMedia(user, new UserService.CreateFromSocialMediaCallBack() {
            @Override
            public void onCreateFromSocialMediaCompleted(User user) {
                hideProgressBar();
                if(user == null){
                    showErrorAlert();
                    return;
                }
                getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putString(PREF_SIGNIN_METHOD, signInMethod)
                        .putString(PREF_USER_ID, user.getId())
                        .putString(PREF_IMAGE_URL, user.getImageUrl())
                        .putString(PREF_USERNAME, user.getUserName())
                        .apply();
                goToProfile();
            }
        });
    }

    private void goToProfile() {
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        startActivity(i);
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

        if(isLoggedIn()){
            goToProfile();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private boolean isLoggedIn() {
        //Facebook check
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            Log.d(TAG, "isLoggedIn: facebook true");
            return true;
        }

        //Google check
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        isLoggedIn = account != null && !account.isExpired();
        if(isLoggedIn){
            Log.d(TAG, "isLoggedIn: google true");
            return true;
        }

        //Email check
        String userId = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(PREF_USER_ID, null);
        if(userId != null){
            Log.d(TAG, "isLoggedIn: email true");
            return true;
        }

        return false;
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            showProgressBar();
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            User user = new User();
            user.setId("g_" + account.getId());
            user.setEmail(account.getEmail());
            user.setPassword("");
            user.setUserName(account.getDisplayName());
            user.setImageUrl(account.getPhotoUrl() == null ? Constants.DEFAULT_PROFILE_PICTURE_URL : account.getPhotoUrl().toString());
            loginOrCreateFromSocialMedia(user, METHOD_GOOGLE);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            hideProgressBar();
            showErrorAlert();
        } catch (NullPointerException e){
            Log.e(TAG, "handleGoogleSignInResult: ", e);
            hideProgressBar();
            showErrorAlert();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    public View.OnClickListener getOnEmailButtonClickListener() {
        return mOnEmailButtonClickListener;
    }

    public void setOnEmailButtonClickListener(View.OnClickListener onEmailButtonClickListener) {
        mOnEmailButtonClickListener = onEmailButtonClickListener;
    }
}

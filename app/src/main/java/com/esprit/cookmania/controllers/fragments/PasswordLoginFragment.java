package com.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.esprit.cookmania.R;
import com.esprit.cookmania.controllers.activities.MainScreenActivity;
import com.esprit.cookmania.models.User;
import com.esprit.cookmania.services.UserService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.UUID;

public class PasswordLoginFragment extends Fragment {

    private static final String TAG = "PasswordLoginFragment";
    public static final String ARG_EMAIL = "email";

    private TextInputLayout mPasswordInputLayout;
    private Button mSignInButton;
    private LinearLayout mProgressBar;
    private ImageButton mBackButton;

    public static PasswordLoginFragment newInstance(String email) {

        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        PasswordLoginFragment fragment = new PasswordLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_password_login, container, false);

        mPasswordInputLayout = v.findViewById(R.id.fragment_password_login_password_input_layout);
        mSignInButton = v.findViewById(R.id.fragment_password_login_signin_button);
        mProgressBar = v.findViewById(R.id.fragment_password_login_progress_bar);
        mBackButton = v.findViewById(R.id.fragment_password_login_back_btn);

        mBackButton.setOnClickListener(v1 -> {
            getActivity().onBackPressed();
        });

        mPasswordInputLayout.requestFocus();
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = mPasswordInputLayout.getEditText().getText().toString();
                if(password.isEmpty()) {
                    mPasswordInputLayout.getEditText().setError(getString(R.string.empty_field_warning));
                    return;
                }
                if(password.length() < 4){
                    mPasswordInputLayout.getEditText().setError(getString(R.string.password_short_error));
                    return;
                }
                showProgressBar();

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        Log.d(TAG, "onSuccess: " + token);
                        String uuid = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                                .getString(getString(R.string.prefs_uuid), null);
                        if (uuid == null) {
                            uuid = UUID.randomUUID().toString();
                            getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                                    .edit().putString(getString(R.string.prefs_uuid), uuid).apply();
                        }

                        User user = new User();
                        user.setEmail(getArguments().getString(ARG_EMAIL));
                        user.setPassword(password);
                        user.setUuid(uuid);
                        user.setToken(token);
                        UserService.getInstance().signInWithEmail(user, new UserService.SignInWithEmailCallBack() {
                            @Override
                            public void onCompletion(User user, int statusCode) {
                                hideProgressBar();
                                if (statusCode == 500) {
                                    showAlert();
                                    return;
                                }
                                if (statusCode == 400) {
                                    mPasswordInputLayout.getEditText().setError(getString(R.string.wrong_password));
                                    return;
                                }
                                Log.d(TAG, "onCompletion: user verified");
                                saveUserData(user);
                                hideSoftKeyboard();
                                goToHome();
                            }
                        });
                    }
                });
            }
        });

        return v;
    }

    private void goToHome() {
        Intent intent = new Intent(getActivity(), MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void saveUserData(User user) {
        getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE).edit()
                .putString(getString(R.string.prefs_user_id), user.getId())
                .putString(getString(R.string.pref_image_url), user.getImageUrl())
                .putString(getString(R.string.prefs_username), user.getUserName())
                .putString(getString(R.string.prefs_signin_method), getString(R.string.method_email))
                .putString(getString(R.string.prefs_user_email), user.getEmail())
                .apply();
    }

    private void showAlert(){
        new AlertDialog.Builder(getActivity())
                .setMessage("We're sorry. An error has occured!")
                .setTitle("Connection problem")
                .setPositiveButton("ok", null)
                .show();
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        showSoftKeyboard();
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(),0);
    }
}

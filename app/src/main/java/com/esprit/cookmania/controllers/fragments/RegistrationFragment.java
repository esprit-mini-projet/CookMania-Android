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
import com.esprit.cookmania.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.UUID;

public class RegistrationFragment extends Fragment {

    private static final String TAG = "RegistrationFragment";

    public static final String ARG_EMAIL = "email";

    private TextInputLayout mUsernameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mConfirmPasswordLayout;
    private Button mRegisterButton;
    private LinearLayout mProgressBar;
    private ImageButton mBackButton;

    public static RegistrationFragment newInstance(String email) {

        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        mUsernameLayout = v.findViewById(R.id.activity_main_username_input_layout);
        mPasswordLayout = v.findViewById(R.id.activity_main_password_input_layout);
        mConfirmPasswordLayout = v.findViewById(R.id.activity_main_password_confirm_input_layout);
        mRegisterButton = v.findViewById(R.id.activity_main_register_button);
        mProgressBar = v.findViewById(R.id.fragment_registration_progress_bar);
        mBackButton = v.findViewById(R.id.fragment_registration_login_back_btn);

        mBackButton.setOnClickListener(v1 -> {
            getActivity().onBackPressed();
        });

        mUsernameLayout.requestFocus();
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameLayout.getEditText().getText().toString();
                final String password = mPasswordLayout.getEditText().getText().toString();
                String confirm = mConfirmPasswordLayout.getEditText().getText().toString();
                boolean valid = true;
                if (username.isEmpty()) {
                    mUsernameLayout.getEditText().setError(getString(R.string.empty_field_warning));
                    valid = false;
                }
                if (password.length() < 4) {
                    mPasswordLayout.getEditText().setError(getString(R.string.password_short_error));
                    valid = false;
                }
                if (!confirm.equals(password)) {
                    mConfirmPasswordLayout.getEditText().setError(getString(R.string.password_match_error));
                    valid = false;
                }
                if (confirm.isEmpty()) {
                    mConfirmPasswordLayout.getEditText().setError(getString(R.string.empty_field_warning));
                    valid = false;
                }
                if (!valid) {
                    return;
                }
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        Log.d(TAG, "onSuccess: " + token);

                        final User user = new User();
                        user.setEmail(getArguments().getString(ARG_EMAIL));
                        user.setUserName(username);
                        user.setPassword(password);
                        user.setImageUrl(Constants.DEFAULT_PROFILE_PICTURE_URL);
                        String uuid = getActivity().getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE)
                                .getString(getString(R.string.prefs_uuid), UUID.randomUUID().toString());
                        user.setUuid(uuid);
                        user.setToken(token);

                        showProgressBar();
                        UserService.getInstance().createFromEmail(user, new UserService.CreateFromEmailCallBack() {
                            @Override
                            public void onCompletion(String id) {
                                hideProgressBar();
                                if (id == null) {
                                    showAlert();
                                    return;
                                }
                                Log.d(TAG, "onCompletion: user created with id " + id);
                                user.setId(id);
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

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage("We're sorry. An error has occured!")
                .setTitle("Connection problem")
                .setPositiveButton("ok", null)
                .show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSoftKeyboard();
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }
}

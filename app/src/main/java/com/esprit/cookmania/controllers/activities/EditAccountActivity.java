package com.esprit.cookmania.controllers.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.esprit.cookmania.R;
import com.esprit.cookmania.helpers.InternetConnectivityObserver;
import com.esprit.cookmania.models.User;
import com.esprit.cookmania.services.UserService;
import com.esprit.cookmania.utils.GlideApp;
import com.esprit.cookmania.utils.NavigationUtils;
import com.fxn.pix.Pix;

import java.io.File;
import java.util.ArrayList;

public class EditAccountActivity extends AppCompatActivity implements UserService.UpdateCredCallBack {

    private static final String TAG = "EditAccountActivity";

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextInputLayout mEmailLayout;
    private TextInputLayout mUsernameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mConfirmPasswordLayout;

    private User mUser;
    private String mUserId;
    private ImageView mUserImageView;
    private ImageView mCameraImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        getViewReferences();
        String imageUrl = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.pref_image_url), "");
        String email = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_email), "");
        String username = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_username), "");
        GlideApp.with(this).load(imageUrl).apply(RequestOptions.circleCropTransform())
                .error(GlideApp.with(this).load(R.drawable.default_profile_picture)
                        .apply(RequestOptions.circleCropTransform()))
                .into(mUserImageView);
        mEmailLayout.getEditText().setText(email);
        mUsernameLayout.getEditText().setText(username);

        //hide or show camera
        mUserId = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), null);
        String userId = getIntent().getStringExtra(mUserId);
        String loginMethod = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_signin_method), "");
        boolean isEmailMethod = loginMethod.equals(getString(R.string.method_email));
        if (isEmailMethod) {
            mCameraImage.setVisibility(View.VISIBLE);
            mCameraImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pix.start(EditAccountActivity.this, REQUEST_IMAGE_CAPTURE, 1);
                }
            });
        }
    }

    private void getViewReferences() {
        mUserImageView = findViewById(R.id.fragment_edit_account_image_view);
        mCameraImage = findViewById(R.id.fragment_edit_account_camera_image);
        mEmailLayout = findViewById(R.id.fragment_edit_account_email_input_layout);
        mUsernameLayout = findViewById(R.id.fragment_edit_account_username_input_layout);
        mPasswordLayout = findViewById(R.id.fragment_edit_account_password_input_layout);
        mConfirmPasswordLayout = findViewById(R.id.fragment_edit_account_password_confirm_input_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        }

        String email = mEmailLayout.getEditText().getText().toString();
        String username = mUsernameLayout.getEditText().getText().toString();
        String password = mPasswordLayout.getEditText().getText().toString();
        String confirm = mConfirmPasswordLayout.getEditText().getText().toString();
        boolean valid = true;
        if (email.isEmpty()) {
            mEmailLayout.getEditText().setError(getString(R.string.empty_field_warning));
            valid = false;
        }
        if (!isValidEmail(email)) {
            mEmailLayout.getEditText().setError(getString(R.string.email_not_valid));
            valid = false;
        }
        if (username.isEmpty()) {
            mUsernameLayout.getEditText().setError(getString(R.string.empty_field_warning));
            valid = false;
        }
        if (!password.isEmpty() && password.length() < 4) {
            mPasswordLayout.getEditText().setError(getString(R.string.password_short_error));
            valid = false;
        }
        if (!confirm.equals(password)) {
            mConfirmPasswordLayout.getEditText().setError(getString(R.string.password_match_error));
            valid = false;
        }
        if (!valid) {
            return true;
        }

        mUser = new User();
        mUser.setId(getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), ""));
        mUser.setEmail(email);
        mUser.setUserName(username);
        mUser.setPassword(password.isEmpty() ? "e" : password);

        UserService.getInstance().updateCredentials(mUser, this);

        return true;
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onCompletion(Boolean isSuccessful) {
        if (!isSuccessful) {
            showAlertDialog();
            return;
        }
        getSharedPreferences(getString(R.string.prefs_name), Context.MODE_PRIVATE).edit()
                .putString(getString(R.string.prefs_username), mUser.getUserName())
                .putString(getString(R.string.prefs_user_email), mUser.getEmail())
                .apply();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message_update_creds)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            showSnackBar();
            updateUserPhoto(data);
        }
    }

    private void showSnackBar() {
        Snackbar.make(findViewById(android.R.id.content), R.string.image_saved, Snackbar.LENGTH_LONG).show();
    }

    private void updateUserPhoto(Intent data) {
        ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
        String imagePath = returnValue.get(0);
        GlideApp.with(this).load(imagePath)
                .apply(RequestOptions.circleCropTransform())
                .into(mUserImageView);
        UserService.getInstance().updateUserPhoto(new File(imagePath), mUserId, new UserService.UpdateUserPhotoCallBack() {
            @Override
            public void onCompletion(String imageUrl) {
                if (imageUrl == null) {
                    //TODO: show error message
                    return;
                }
                getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                        .edit()
                        .putString(getString(R.string.pref_image_url), imageUrl)
                        .apply();
                Log.i(TAG, "onCompletion: ");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");
        InternetConnectivityObserver.get().setConsumer(new InternetConnectivityObserver.Consumer() {
            @Override
            public void accept(boolean isConnected) {
                if(!isConnected) startActivity(new Intent(EditAccountActivity.this, ShoppingListActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");
    }
}

package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.services.UserService;
import tn.duoes.esprit.cookmania.utils.GlideApp;

public class EditAccountActivity extends AppCompatActivity implements UserService.UpdateCredCallBack {

    private TextInputLayout mEmailLayout;
    private TextInputLayout mUsernameLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mConfirmPasswordLayout;

    private User mUser;
    private ImageView mUserImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getViewReferences();
        String imageUrl = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.pref_image_url), "");
        String email = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_email), "");
        String username = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_username), "");
        GlideApp.with(this).load(imageUrl).apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.default_profile_picture)
                .error(GlideApp.with(this).load(R.drawable.default_profile_picture)
                        .apply(RequestOptions.circleCropTransform()))
                .into(mUserImageView);
        mEmailLayout.getEditText().setText(email);
        mUsernameLayout.getEditText().setText(username);
    }

    private void getViewReferences() {
        mUserImageView = findViewById(R.id.fragment_edit_account_image_view);
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
            return true;
        } else if (item.getItemId() != R.id.edit_account_save) {
            return super.onOptionsItemSelected(item);
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
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message_update_creds)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}

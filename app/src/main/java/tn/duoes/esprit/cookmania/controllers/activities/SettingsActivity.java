package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.MainLoginFragment;

public class SettingsActivity extends AppCompatActivity {

    private String mMethodString;
    private Button mLogoutButton;
    private Button mDeleteButton;
    private Button mEditButton;
    private TextView mLoginMethodText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMethodString = getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).getString(MainLoginFragment.PREF_SIGNIN_METHOD, null);
        getViewReferences();
        setupLogoutButton();
        setupEditAccount();
        setupDeleteButton();
        setupSigninMethodText();
    }

    private void setupSigninMethodText() {
        String text;
        if (mMethodString.equals(getString(R.string.method_email))) {
            text = "your email: " + getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                    .getString(getString(R.string.prefs_user_email), "");
        } else if (mMethodString.equals(getString(R.string.method_facebook))) {
            text = "your Facebook account.";
        } else {
            text = "your Google account.";
        }
        mLoginMethodText.setText(text);
    }

    private void setupDeleteButton() {
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupEditAccount() {
        if (!mMethodString.equals(getString(R.string.method_email))) {
            mEditButton.setVisibility(View.GONE);
            findViewById(R.id.activity_settings_first_separator).setVisibility(View.GONE);
            return;
        }
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EditAccountActivity.class));
            }
        });
    }

    private void setupLogoutButton() {
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMethodString.equals(MainLoginFragment.METHOD_FACEBOOK)) {
                    LoginManager.getInstance().logOut();
                } else if (mMethodString.equals(MainLoginFragment.METHOD_GOOGLE)) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    GoogleSignIn.getClient(SettingsActivity.this, gso).signOut();
                }
                getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void getViewReferences() {
        mLogoutButton = findViewById(R.id.settings_logout_button);
        mDeleteButton = findViewById(R.id.settings_delete_button);
        mEditButton = findViewById(R.id.settings_edit_button);
        mLoginMethodText = findViewById(R.id.activity_settings_method_text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

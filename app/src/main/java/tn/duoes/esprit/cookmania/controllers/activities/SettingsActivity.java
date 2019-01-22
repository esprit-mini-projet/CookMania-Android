package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.MainLoginFragment;
import tn.duoes.esprit.cookmania.services.UserService;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

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
        getSupportActionBar().setTitle("Settings");

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
        final String userId = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_user_id), "");
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle(R.string.confirmation)
                        .setMessage(R.string.confirmation_message_delete_account)
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserService.getInstance().delete(userId, new UserService.DeleteCallBack() {
                                    @Override
                                    public void onCompletion(Boolean result) {
                                        if (result) logout();
                                        else {
                                            new AlertDialog.Builder(SettingsActivity.this)
                                                    .setTitle(R.string.error)
                                                    .setMessage(R.string.error_message_delete_account)
                                                    .setPositiveButton(android.R.string.ok, null)
                                                    .show();
                                        }
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
    }

    private void setupEditAccount() {
        if (!mMethodString.equals(getString(R.string.method_email))) {
            findViewById(R.id.activity_settings_edit_container).setVisibility(View.GONE);
            findViewById(R.id.activity_settings_first_separator).setVisibility(View.GONE);
            return;
        }
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EditAccountActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void setupLogoutButton() {
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        if (mMethodString.equals(MainLoginFragment.METHOD_FACEBOOK)) {
            LoginManager.getInstance().logOut();
        } else if (mMethodString.equals(MainLoginFragment.METHOD_GOOGLE)) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignIn.getClient(SettingsActivity.this, gso).signOut();
        }
        getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE).edit()
                .remove(getString(R.string.prefs_user_id))
                .remove(getString(R.string.pref_image_url))
                .remove(getString(R.string.prefs_username))
                .remove(getString(R.string.prefs_user_email))
                .remove(getString(R.string.prefs_signin_method))
                .apply();
        String uuid = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE)
                .getString(getString(R.string.prefs_uuid), "");
        UserService.getInstance().logout(uuid);
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");/*
        InternetConnectivityObserver.get().start(new InternetConnectivityObserver.Consumer() {
            @Override
            public void accept(boolean isConnected) {
                if(!isConnected) startActivity(new Intent(SettingsActivity.this, ShoppingListActivity.class));
            }
        });*/
    }

    @Override
    protected void onStop() {
        super.onStop();/*
        InternetConnectivityObserver.get().stop();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");/*
        InternetConnectivityObserver.get().stop();*/
    }
}

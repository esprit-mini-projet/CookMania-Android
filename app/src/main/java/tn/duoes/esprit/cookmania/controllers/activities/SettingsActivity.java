package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.MainLoginFragment;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String methodString = getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).getString(MainLoginFragment.PREF_SIGNIN_METHOD, null);
        findViewById(R.id.settings_logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methodString.equals(MainLoginFragment.METHOD_FACEBOOK)){
                    LoginManager.getInstance().logOut();
                }else if(methodString.equals(MainLoginFragment.METHOD_GOOGLE)){
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

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return NavigationUtils.getParentActivityIntent(this, getIntent());
    }
}
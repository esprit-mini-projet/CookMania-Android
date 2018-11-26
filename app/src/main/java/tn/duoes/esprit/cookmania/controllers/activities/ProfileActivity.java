package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.MainLoginFragment;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView photo = findViewById(R.id.profile_picture);
        TextView name = findViewById(R.id.name_text);
        TextView method = findViewById(R.id.method_text);
        Button logoutButton = findViewById(R.id.logout_button);

        String photoUrl = getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).getString(MainLoginFragment.PREF_IMAGE_URL, null);
        String nameString = getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).getString(MainLoginFragment.PREF_USERNAME, null);
        final String methodString = getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).getString(MainLoginFragment.PREF_SIGNIN_METHOD, null);

        Picasso.get().load(photoUrl).into(photo);
        name.setText(nameString);
        method.setText("By " + methodString);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methodString.equals(MainLoginFragment.METHOD_FACEBOOK)){
                    LoginManager.getInstance().logOut();
                }else if(methodString.equals(MainLoginFragment.METHOD_GOOGLE)){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    GoogleSignIn.getClient(ProfileActivity.this, gso).signOut();
                }
                getSharedPreferences(MainLoginFragment.PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }
}

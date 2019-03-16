package com.esprit.cookmania.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.esprit.cookmania.R;
import com.esprit.cookmania.controllers.fragments.EmailLoginFragment;
import com.esprit.cookmania.controllers.fragments.MainLoginFragment;
import com.esprit.cookmania.controllers.fragments.PasswordLoginFragment;
import com.esprit.cookmania.controllers.fragments.RegistrationFragment;
import com.esprit.cookmania.utils.NavigationUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_fragment_container);
        if(fragment == null) {
            fragment = MainLoginFragment.newInstance(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment_container, EmailLoginFragment.newInstance(new EmailLoginFragment.EmailLoginFragmentCallBack() {
                        @Override
                        public void register(String email) {
                            getSupportFragmentManager().beginTransaction()
                                .replace(R.id.login_fragment_container, RegistrationFragment.newInstance(email))
                                .addToBackStack(RegistrationFragment.class.getName())
                                .commit();
                        }

                        @Override
                        public void continueToPassword(String email) {
                            getSupportFragmentManager().beginTransaction()
                                .replace(R.id.login_fragment_container, PasswordLoginFragment.newInstance(email))
                                .addToBackStack(PasswordLoginFragment.class.getName())
                                .commit();
                        }
                    }))
                    .addToBackStack(EmailLoginFragment.class.getName())
                    .commit();
                }
            });
            getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_container, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");
    }
}

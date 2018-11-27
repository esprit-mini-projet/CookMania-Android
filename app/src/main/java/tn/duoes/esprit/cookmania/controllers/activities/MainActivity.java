package tn.duoes.esprit.cookmania.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.EmailLoginFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.MainLoginFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.RegistrationFragment;

public class MainActivity extends AppCompatActivity {


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
                        public void login(String email) {

                        }
                    }))
                    .addToBackStack(EmailLoginFragment.class.getName())
                    .commit();
                }
            });
            getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_container, fragment).commit();
        }
    }
}

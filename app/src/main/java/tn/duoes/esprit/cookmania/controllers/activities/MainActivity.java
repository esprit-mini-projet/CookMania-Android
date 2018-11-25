package tn.duoes.esprit.cookmania.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.MainLoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_fragment_container);
        if(fragment == null) {
            fragment = MainLoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.login_fragment_container, fragment).commit();
        }
    }
}

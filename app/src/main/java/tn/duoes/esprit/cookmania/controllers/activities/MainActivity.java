package tn.duoes.esprit.cookmania.controllers.activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tn.duoes.esprit.cookmania.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        TextInputLayout loginInputLayout = findViewById(R.id.login_input_layout);
    }
}

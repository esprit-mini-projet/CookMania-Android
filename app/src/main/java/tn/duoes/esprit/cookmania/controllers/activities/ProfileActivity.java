package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_SHOULD_FINISH = "should_finish";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        Fragment profileFragment = getSupportFragmentManager().findFragmentById(R.id.activity_profile_fragment_container);
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance(userId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_profile_fragment_container, profileFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            boolean shouldFinish = getIntent().getBooleanExtra(EXTRA_SHOULD_FINISH, true);
            if (shouldFinish) finish();
            else startActivity(new Intent(this, MainScreenActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        boolean shouldFinish = getIntent().getBooleanExtra(EXTRA_SHOULD_FINISH, true);
        if (shouldFinish) finish();
        else super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}

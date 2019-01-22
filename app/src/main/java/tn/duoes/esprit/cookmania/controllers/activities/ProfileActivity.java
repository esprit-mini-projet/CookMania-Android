package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.ProfileFragment;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

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
            Log.i(TAG, "onOptionsItemSelected: should finish: " + shouldFinish);
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

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");/*
        InternetConnectivityObserver.get().start(new InternetConnectivityObserver.Consumer() {
            @Override
            public void accept(boolean isConnected) {
                if(!isConnected) startActivity(new Intent(ProfileActivity.this, ShoppingListActivity.class));
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

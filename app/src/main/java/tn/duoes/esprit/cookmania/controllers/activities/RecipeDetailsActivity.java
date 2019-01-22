package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import io.alterac.blurkit.BlurLayout;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import tn.duoes.esprit.cookmania.controllers.fragments.RecipeDetailsFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.TimerFragment;
import tn.duoes.esprit.cookmania.helpers.InternetConnectivityObserver;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailsStepAdapter.StepItemCallBack,
        TimerFragment.TimerFragmentCallBack
{

    private static final String TAG = "RecipeDetailsActivity";
    public static final String EXTRA_RECIPE_ID = "recipeId";
    public static final String EXTRA_SHOULD_FINISH = "should_finish";

    private boolean timerIsShown = false;
    private BlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mBlurLayout = findViewById(R.id.timer_fragment_container);
        
        Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment_container);
        if(detailsFragment == null){
            detailsFragment = RecipeDetailsFragment.newInstance(getIntent().getStringExtra(EXTRA_RECIPE_ID), this);
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_details_fragment_container, detailsFragment).commit();
        }
    }

    @Override
    public void onTimeLayoutClicked(int time) {
        mBlurLayout.invalidate();
        mBlurLayout.setVisibility(View.VISIBLE);
        Fragment timerFragment = getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container);
        if(timerFragment == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.timer_fragment_container, TimerFragment.newInstance(time, this))
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.timer_fragment_container, TimerFragment.newInstance(time, this))
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();
        }
        mBlurLayout.startBlur();
        timerIsShown = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(timerIsShown){
                removeTimerFragment();
                return true;
            }
            boolean shouldFinish = getIntent().getBooleanExtra(EXTRA_SHOULD_FINISH, true);
            if (shouldFinish) finish();
            else {
                Intent intent = new Intent(this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void removeTimerFragment() {
        getSupportFragmentManager().beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container))
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();
        timerIsShown = false;
        mBlurLayout.pauseBlur();
        mBlurLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        mBlurLayout.pauseBlur();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(timerIsShown) mBlurLayout.startBlur();
    }

    @Override
    public void onBackPressed() {
        if(timerIsShown){
            removeTimerFragment();
            return;
        }
        boolean shouldFinish = getIntent().getBooleanExtra(EXTRA_SHOULD_FINISH, true);
        if (shouldFinish) finish();
        else {
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onViewClicked() {
        removeTimerFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");
        InternetConnectivityObserver.get().setConsumer(new InternetConnectivityObserver.Consumer() {
            @Override
            public void accept(boolean isConnected) {
                if(!isConnected) startActivity(new Intent(RecipeDetailsActivity.this, ShoppingListActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");
    }
}

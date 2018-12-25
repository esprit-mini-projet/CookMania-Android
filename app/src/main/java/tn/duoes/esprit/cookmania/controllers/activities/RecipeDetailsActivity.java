package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import io.alterac.blurkit.BlurLayout;
import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.RecipeDetailsStepAdapter;
import tn.duoes.esprit.cookmania.controllers.fragments.RecipeDetailsFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.TimerFragment;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsStepAdapter.StepItemCallBack
{

    private static final String TAG = "RecipeDetailsActivity";
    public static final String EXTRA_RECIPE_ID = "recipeId";

    private boolean timerIsShown = false;
    private BlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBlurLayout = findViewById(R.id.timer_fragment_container);

        Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment_container);
        if(detailsFragment == null){
            detailsFragment = RecipeDetailsFragment.newInstance(getIntent().getStringExtra(EXTRA_RECIPE_ID), this);
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_details_fragment_container, detailsFragment).commit();
        }
    }

    @Override
    public void onTimeLayoutClicked(int time) {
        mBlurLayout.setVisibility(View.VISIBLE);
        Fragment timerFragment = getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container);
        if(timerFragment == null){
            getSupportFragmentManager().beginTransaction().add(R.id.timer_fragment_container, TimerFragment.newInstance(time))
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.timer_fragment_container, TimerFragment.newInstance(time))
                    .commit();
        }
        mBlurLayout.startBlur();
        timerIsShown = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(timerIsShown){
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container)).commit();
                timerIsShown = false;
                mBlurLayout.pauseBlur();
                mBlurLayout.setVisibility(View.GONE);
                return true;
            }
            startActivity(new Intent(this, MainScreenActivity.class));
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.timer_fragment_container));
            timerIsShown = false;
            mBlurLayout.pauseBlur();
            mBlurLayout.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}

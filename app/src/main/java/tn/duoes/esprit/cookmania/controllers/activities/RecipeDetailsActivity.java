package tn.duoes.esprit.cookmania.controllers.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.RecipeDetailsFragment;

public class RecipeDetailsActivity extends AppCompatActivity
{

    private static final String TAG = "RecipeDetailsActivity";
    public static final String EXTRA_RECIPE_ID = "recipeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment detailsFragment = getSupportFragmentManager().findFragmentById(R.id.recipe_details_fragment_container);
        if(detailsFragment == null){
            detailsFragment = RecipeDetailsFragment.newInstance(getIntent().getStringExtra(EXTRA_RECIPE_ID));
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_details_fragment_container, detailsFragment).commit();
        }
    }

}

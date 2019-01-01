package tn.duoes.esprit.cookmania.controllers.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.ShoppingListFragment;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_shopping_list_fragment_container);
        if (fragment == null) {
            fragment = ShoppingListFragment.newInstance(null, null);
            getSupportFragmentManager().beginTransaction().add(R.id.activity_shopping_list_fragment_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

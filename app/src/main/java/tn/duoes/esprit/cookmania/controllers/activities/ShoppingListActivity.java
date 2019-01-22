package tn.duoes.esprit.cookmania.controllers.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.fragments.ShoppingListFragment;
import tn.duoes.esprit.cookmania.helpers.InternetConnectivityObserver;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListFragment.OnFragmentInteractionListener {

    private static final String TAG = "ShoppingListActivity";

    private TextView mOfflineText;
    private boolean mIsOnline = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        getSupportActionBar().setTitle("Shopping list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_shopping_list_fragment_container);
        if (fragment == null) {
            fragment = ShoppingListFragment.newInstance(null, null);
            getSupportFragmentManager().beginTransaction().add(R.id.activity_shopping_list_fragment_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_go_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        } else if (item.getItemId() == R.id.menu_go_home_home) {
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");
        InternetConnectivityObserver.get().setConsumer(new InternetConnectivityObserver.Consumer() {
            @Override
            public void accept(boolean internet) {
                Log.i(TAG, "accept: " + internet);
                mIsOnline = internet;
                mOfflineText = findViewById(R.id.activity_shopping_list_offline_text);
                mOfflineText.setVisibility(internet ? View.GONE : View.VISIBLE);
                if (internet) {
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        });
        InternetConnectivityObserver.get().stop();
        InternetConnectivityObserver.get().start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");
    }

    public boolean isOnline() {
        return mIsOnline;
    }
}

package tn.duoes.esprit.cookmania.controllers.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.MainScreenPagerAdapter;
import tn.duoes.esprit.cookmania.controllers.fragments.AddRecipeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.CategoryRecipesFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.FeedFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.HomeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.SearchFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.ShoppingListFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.SuggestedFragment;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;
import tn.duoes.esprit.cookmania.views.RatingViewPager;

public class MainScreenActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        AddRecipeFragment.OnFragmentInteractionListener,
        ShoppingListFragment.OnFragmentInteractionListener,
        SuggestedFragment.OnFragmentInteractionListener,
        CategoryRecipesFragment.OnFragmentInteractionListener,
        FeedFragment.OnFragmentInteractionListener {

    private static final String TAG = MainScreenActivity.class.getSimpleName();

    private MaterialSearchView searchView;
    private int tabPosition = 0;
    public static RatingViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: HELLO");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.d(TAG, "onSuccess: "+token);
            }
        });

        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.name_logo);

        searchView = findViewById(R.id.searchview);

        TabLayout tabLayout = findViewById(R.id.mainscreen_tablayout);
        viewPager = findViewById(R.id.mainscreen_viewpager);

        viewPager.setAdapter(new MainScreenPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: "+tabPosition);
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        if(tabPosition == 1){
            item.setVisible(true);
        }else{
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationUtils.pagesStack.push(1);
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NavigationUtils.pagesStack.pop();
        Log.i(TAG, "onDestroy: ");
    }
}

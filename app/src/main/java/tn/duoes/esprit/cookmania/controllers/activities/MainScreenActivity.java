package tn.duoes.esprit.cookmania.controllers.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.MainScreenPagerAdapter;
import tn.duoes.esprit.cookmania.controllers.fragments.AddRecipeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.CategoryRecipesFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.HomeFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.SearchFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.ShoppingListFragment;
import tn.duoes.esprit.cookmania.controllers.fragments.SuggestedFragment;

public class MainScreenActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        AddRecipeFragment.OnFragmentInteractionListener,
        ShoppingListFragment.OnFragmentInteractionListener,
        SuggestedFragment.OnFragmentInteractionListener,
        CategoryRecipesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        TabLayout tabLayout = findViewById(R.id.mainscreen_tablayout);
        final ViewPager viewPager = findViewById(R.id.mainscreen_viewpager);

        viewPager.setAdapter(new MainScreenPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
}

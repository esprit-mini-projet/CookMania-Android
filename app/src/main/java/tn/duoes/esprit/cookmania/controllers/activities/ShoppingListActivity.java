package tn.duoes.esprit.cookmania.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ShoppingListViewAdapter;
import tn.duoes.esprit.cookmania.dao.ShoppingListDAO;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView mShoppingRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShoppingListViewAdapter mViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        mShoppingRecyclerView = findViewById(R.id.shooping_list_view);
        mViewAdapter = new ShoppingListViewAdapter(ShoppingListDAO.getShoppingItems());

        mShoppingRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mShoppingRecyclerView.setLayoutManager(mLayoutManager);
        mShoppingRecyclerView.setAdapter(mViewAdapter);
    }


}

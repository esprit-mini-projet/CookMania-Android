package tn.duoes.esprit.cookmania.controllers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ShoppingListViewAdapter;
import tn.duoes.esprit.cookmania.dao.ShoppingListDAO;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        ListView shoppingListView = findViewById(R.id.shooping_list_view);
        ShoppingListViewAdapter viewAdapter = new ShoppingListViewAdapter(this, ShoppingListDAO.getShoppingItems());
        shoppingListView.setAdapter(viewAdapter);
    }


}

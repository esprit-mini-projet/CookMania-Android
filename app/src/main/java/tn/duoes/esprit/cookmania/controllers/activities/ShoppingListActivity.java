package tn.duoes.esprit.cookmania.controllers.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ShoppingListViewAdapter;
import tn.duoes.esprit.cookmania.dao.ShoppingListDAO;
import tn.duoes.esprit.cookmania.helpers.ShoppingListRecyclerItemTouchHelper;

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
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mShoppingRecyclerView.setLayoutManager(mLayoutManager);
        mShoppingRecyclerView.setAdapter(mViewAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ShoppingListRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new ShoppingListRecyclerItemTouchHelper.ShoppingListRecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                final int deletedIndex = viewHolder.getAdapterPosition();
                final Object deletedItem = mViewAdapter.getMItems().get(deletedIndex);

                // remove the item from recycler view
                mViewAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar
                        .make(viewHolder.itemView, "removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore the deleted item
                        mViewAdapter.restoreItem(deletedItem, deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mShoppingRecyclerView);
    }


}

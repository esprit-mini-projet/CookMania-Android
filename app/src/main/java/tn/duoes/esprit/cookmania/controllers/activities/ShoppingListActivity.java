package tn.duoes.esprit.cookmania.controllers.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ShoppingListViewAdapter;
import tn.duoes.esprit.cookmania.dao.ShoppingListDAO;
import tn.duoes.esprit.cookmania.helpers.ShoppingListRecyclerItemTouchHelper;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView mShoppingRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShoppingListViewAdapter mViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        mShoppingRecyclerView = findViewById(R.id.shooping_list_view);
        mViewAdapter = new ShoppingListViewAdapter(getBaseContext());

        mShoppingRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mShoppingRecyclerView.setLayoutManager(mLayoutManager);
        mShoppingRecyclerView.setAdapter(mViewAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ShoppingListRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new ShoppingListRecyclerItemTouchHelper.ShoppingListRecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                int deletedIndex = viewHolder.getAdapterPosition();
                Object deletedItem = mViewAdapter.getMItems().get(deletedIndex);

                final Object toRestoreItem;
                final int toRestoreIndex;

                if ((deletedItem instanceof Ingredient) && ((ShoppingListItem)mViewAdapter.getMItems().get(((Ingredient) deletedItem).getShoppingListItemIndex())).getIngredients().size() == 1){
                    toRestoreItem = (mViewAdapter.getMItems().get(deletedIndex-1));
                    toRestoreIndex = deletedIndex - 1;
                    System.out.println(deletedItem);
                }else {
                    toRestoreIndex = deletedIndex;
                    toRestoreItem = deletedItem;
                }


                // remove the item from recycler view
                mViewAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(viewHolder.itemView, "removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewAdapter.restoreItem(toRestoreItem, toRestoreIndex);
                    }
                });
                snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        mViewAdapter.persist();
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mShoppingRecyclerView);
    }


}

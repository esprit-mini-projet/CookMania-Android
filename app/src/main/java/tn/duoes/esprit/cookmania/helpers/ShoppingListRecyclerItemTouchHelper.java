package tn.duoes.esprit.cookmania.helpers;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import tn.duoes.esprit.cookmania.adapters.ShoppingListViewAdapter;

public class ShoppingListRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ShoppingListRecyclerItemTouchHelperListener mListener;


    public ShoppingListRecyclerItemTouchHelper(int dragDirs, int swipeDirs, ShoppingListRecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.mListener = listener;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView;
            if(viewHolder instanceof ShoppingListViewAdapter.RecipeViewHolder){
                foregroundView = ((ShoppingListViewAdapter.RecipeViewHolder) viewHolder).foregroundView;
            }else{
                foregroundView = ((ShoppingListViewAdapter.IngredientViewHolder) viewHolder).foregroundView;
            }
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView;
        if (viewHolder instanceof ShoppingListViewAdapter.RecipeViewHolder){
            foregroundView = ((ShoppingListViewAdapter.RecipeViewHolder) viewHolder).foregroundView;
        }else{
            foregroundView = ((ShoppingListViewAdapter.IngredientViewHolder) viewHolder).foregroundView;
        }
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundView;
        if (viewHolder instanceof ShoppingListViewAdapter.RecipeViewHolder){
            foregroundView = ((ShoppingListViewAdapter.RecipeViewHolder) viewHolder).foregroundView;
        }else{
            foregroundView = ((ShoppingListViewAdapter.IngredientViewHolder) viewHolder).foregroundView;
        }
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView;
        if (viewHolder instanceof ShoppingListViewAdapter.RecipeViewHolder){
            foregroundView = ((ShoppingListViewAdapter.RecipeViewHolder) viewHolder).foregroundView;
        }else{
            foregroundView = ((ShoppingListViewAdapter.IngredientViewHolder) viewHolder).foregroundView;
        }
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    public interface ShoppingListRecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}

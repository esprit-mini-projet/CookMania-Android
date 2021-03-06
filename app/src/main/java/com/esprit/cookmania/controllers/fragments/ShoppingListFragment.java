package com.esprit.cookmania.controllers.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.cookmania.R;
import com.esprit.cookmania.adapters.ShoppingListViewAdapter;
import com.esprit.cookmania.controllers.activities.ShoppingListActivity;
import com.esprit.cookmania.helpers.RecyclerItemTouchHelper;
import com.esprit.cookmania.models.Ingredient;
import com.esprit.cookmania.models.ShoppingListItem;
import com.esprit.cookmania.views.corned_beef.BubbleCoachMark;
import com.esprit.cookmania.views.corned_beef.CoachMark;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements ShoppingListViewAdapter.ShopItemClickListener {
    private static final String TAG = ShoppingListFragment.class.getSimpleName();
    public static final String SEEN_RECIPE_HINT_KEY = "recipe_hint";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.sharedPreferences = getContext().getSharedPreferences(getContext().getString(R.string.prefs_name), Context.MODE_PRIVATE);
    }

    private RecyclerView mShoppingRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ShoppingListViewAdapter mViewAdapter;
    private TextView emptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        mShoppingRecyclerView = fragment.findViewById(R.id.shooping_list_view);
        mViewAdapter = new ShoppingListViewAdapter(getContext(), this);

        mShoppingRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mShoppingRecyclerView.setLayoutManager(mLayoutManager);
        mShoppingRecyclerView.setAdapter(mViewAdapter);

        emptyTextView = fragment.findViewById(R.id.shopping_empty_tv);
        checkForEmptyShoppingList();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                int deletedIndex = viewHolder.getAdapterPosition();
                Object deletedItem = mViewAdapter.getMItems().get(deletedIndex);

                final Object toRestoreItem;
                final int toRestoreIndex;

                if ((deletedItem instanceof Ingredient) && ((ShoppingListItem)mViewAdapter.getMItems().get(((Ingredient) deletedItem).getShoppingListItemIndex())).getIngredients().size() == 1){
                    toRestoreItem = (mViewAdapter.getMItems().get(deletedIndex-1));
                    toRestoreIndex = deletedIndex - 1;
                }else {
                    toRestoreIndex = deletedIndex;
                    toRestoreItem = deletedItem;
                }


                // remove the item from recycler view
                mViewAdapter.removeItem(viewHolder.getAdapterPosition());
                checkForEmptyShoppingList();

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Click UNDO to restore removed item", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> {
                    mViewAdapter.restoreItem(toRestoreItem, toRestoreIndex);
                    checkForEmptyShoppingList();
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

        mShoppingRecyclerView.post(() -> {
            if (!sharedPreferences.getBoolean(SEEN_RECIPE_HINT_KEY, false) && mViewAdapter.mItems != null && !mViewAdapter.mItems.isEmpty()) {
                sharedPreferences.edit().putBoolean(SEEN_RECIPE_HINT_KEY, true).apply();
                ImageView recipeArrowIV = mLayoutManager.findViewByPosition(0).findViewById(R.id.shopping_row_arrow);
                ImageView ingredientArrowIV = mLayoutManager.findViewByPosition(1).findViewById(R.id.shopping_ingredient_row_arrow);
                CoachMark recipeBubbleCoachMark = new BubbleCoachMark.BubbleCoachMarkBuilder(
                        getContext(), recipeArrowIV, "Slide to remove the entire recipe from shopping list!")
                        .setTargetOffset(0.25f)
                        .setShowBelowAnchor(true)
                        .setPadding(10)
                        .setOnDismissListener(() -> {
                            CoachMark ingredientBubbleCoachMark = new BubbleCoachMark.BubbleCoachMarkBuilder(
                                    getContext(), ingredientArrowIV, "Or slide to only remove specific ingredient!")
                                    .setTargetOffset(0.25f)
                                    .setShowBelowAnchor(true)
                                    .setPadding(10)
                                    .build();

                            ingredientBubbleCoachMark.show();
                        })
                        .build();

                recipeBubbleCoachMark.show();
            }
        });

        return fragment;
    }

    private void checkForEmptyShoppingList(){
        if(mViewAdapter.mItems.isEmpty()){
            emptyTextView.setVisibility(View.VISIBLE);
        }else{
            emptyTextView.setVisibility(View.GONE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onClick() {
        if (getActivity().getClass() == ShoppingListActivity.class) {
            ShoppingListActivity activity = (ShoppingListActivity) getActivity();
            return activity.isOnline();
        }
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

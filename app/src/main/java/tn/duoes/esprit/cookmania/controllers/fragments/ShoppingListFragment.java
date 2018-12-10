package tn.duoes.esprit.cookmania.controllers.fragments;

import android.content.Context;
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

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.adapters.ShoppingListViewAdapter;
import tn.duoes.esprit.cookmania.helpers.RecyclerItemTouchHelper;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView mShoppingRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShoppingListViewAdapter mViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        mShoppingRecyclerView = fragment.findViewById(R.id.shooping_list_view);
        mViewAdapter = new ShoppingListViewAdapter(getContext());

        mShoppingRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mShoppingRecyclerView.setLayoutManager(mLayoutManager);
        mShoppingRecyclerView.setAdapter(mViewAdapter);

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

        return fragment;
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

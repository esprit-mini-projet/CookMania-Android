package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.Ingredient;

public class RecipeDetailsIngredientsAdapter extends RecyclerView.Adapter<RecipeDetailsIngredientsAdapter.IngredientHolder> {

    private List<Ingredient> mIngredients;
    private Context mContext;
    private IngredientItemCallBack mCallBack;

    public RecipeDetailsIngredientsAdapter(Context context, List<Ingredient> ingredients, IngredientItemCallBack callBack) {
        mContext = context;
        mIngredients = ingredients;
        mCallBack = callBack;
    }



    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_ingredient, viewGroup, false);
        return new IngredientHolder(view, mCallBack);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder ingredientHolder, int i) {
        ingredientHolder.bind(mIngredients.get(i), i == mIngredients.size() - 1, mContext);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    static class IngredientHolder extends RecyclerView.ViewHolder{

        private ImageButton mAddButton;
        private ImageButton mDeleteButton;
        private TextView mQuantityTextView;
        private TextView mNameTextView;
        private IngredientItemCallBack mCallBack;

        public IngredientHolder(@NonNull View itemView, IngredientItemCallBack callBack) {
            super(itemView);
            mAddButton = itemView.findViewById(R.id.list_item_ingredient_add_button);
            mDeleteButton = itemView.findViewById(R.id.list_item_ingredient_delete_button);
            mQuantityTextView = itemView.findViewById(R.id.list_item_ingredient_quantity_text);
            mNameTextView = itemView.findViewById(R.id.list_item_ingredient_name_text);
            mCallBack = callBack;
        }

        public void bind(final Ingredient ingredient, boolean isLast, Context context) {
            mQuantityTextView.setText(String.format("%s%s", ingredient.getQuantity(), ingredient.getUnit()));
            mNameTextView.setText(ingredient.getName());
            setBackground(isLast, context);
            if (ingredient.isInShoppingList()) {
                mAddButton.setVisibility(View.GONE);
                mDeleteButton.setVisibility(View.VISIBLE);
            } else {
                mAddButton.setVisibility(View.VISIBLE);
                mDeleteButton.setVisibility(View.GONE);
            }
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddButton.setVisibility(View.GONE);
                    mDeleteButton.setVisibility(View.VISIBLE);
                    ingredient.setInShoppingList(true);
                    mCallBack.onAddIngredientButtonClicked(ingredient);
                }
            });
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setVisibility(View.GONE);
                    ingredient.setInShoppingList(false);
                    mCallBack.onDeleteIngredientButtonClicked(ingredient);
                }
            });
        }

        void setBackground(boolean isLast, Context context){
            if(isLast){
                itemView.setBackground(null);
            }else{
                itemView.setBackground(context.getDrawable(R.drawable.border_bottom_shape));
            }
        }
    }

    public interface IngredientItemCallBack {
        void onAddIngredientButtonClicked(Ingredient ingredient);

        void onDeleteIngredientButtonClicked(Ingredient ingredient);
    }
}

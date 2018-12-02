package tn.duoes.esprit.cookmania.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.dao.ShoppingListDAO;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.ListUtils;

public class ShoppingListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> mItems;

    public ShoppingListViewAdapter(Context context){
        super();
        this.mContext = context;
        updateDataSource();
    }

    private void updateDataSource(){
        List<ShoppingListItem> items = ShoppingListDAO.getInstance(mContext).getShoppingItems();
        if (items == null) items = new ArrayList<>();
        mItems = ListUtils.flattenList(new ArrayList<Object>(items), new ListUtils.IListUtils<Ingredient>() {
            @Override
            public List<Ingredient> nested(Object o, int index) {
                ShoppingListItem shoppingItem = (ShoppingListItem) o;
                List<Ingredient> ingredients = new ArrayList<>(shoppingItem.getIngredients());
                for (Ingredient ingredient : ingredients) {
                    ingredient.setShoppingListItemIndex(index);
                }
                return shoppingItem.getIngredients();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItems.get(position);
        if (item instanceof ShoppingListItem){
            return R.layout.shopping_list_row;
        }else {
            return R.layout.shopping_list_item_row;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false);
        switch (viewType){
            case R.layout.shopping_list_row:
                return new RecipeViewHolder(v);
            case R.layout.shopping_list_item_row:
                return new IngredientViewHolder(v);
            default:
                return new RecipeViewHolder(v);
        }
    }

    private ShoppingListItem shoppingListItem;

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof IngredientViewHolder){
            Ingredient ingredient = (Ingredient) mItems.get(position);
            IngredientViewHolder holder = (IngredientViewHolder) viewHolder;
            holder.nameTextView.setText(ingredient.getName());
            holder.unitTextView.setText(ingredient.getQuantity()+" "+ingredient.getUnit());
            holder.itemView.setTag(shoppingListItem);
        }else{
            shoppingListItem = (ShoppingListItem) mItems.get(position);
            RecipeViewHolder holder = (RecipeViewHolder) viewHolder;
            Glide.with(holder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+shoppingListItem.getRecipe().getImageURL()).into(holder.recipeImageView);
            holder.recipeTextView.setText(shoppingListItem.getRecipe().getName());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView recipeImageView;
        TextView recipeTextView;
        public View foregroundView, backgroundView;

        RecipeViewHolder(@NonNull View itemView){
            super(itemView);
            foregroundView = itemView.findViewById(R.id.shopping_list_foreground);
            backgroundView = itemView.findViewById(R.id.shopping_list_background);
            recipeImageView = foregroundView.findViewById(R.id.shopping_row_iv);
            recipeTextView = foregroundView.findViewById(R.id.shopping_row_tv);
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView unitTextView;
        public View foregroundView, backgroundView;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            foregroundView = itemView.findViewById(R.id.shopping_list_item_row_foreground);
            backgroundView = itemView.findViewById(R.id.shopping_list_item_row_background);
            nameTextView = foregroundView.findViewById(R.id.shopping_ingredient_row_name);
            unitTextView = foregroundView.findViewById(R.id.shopping_ingredient_row_unit);
        }
    }

    public void removeItem(int position){
        Object item = mItems.get(position);
        if (item instanceof ShoppingListItem){
            ShoppingListItem shoppingListItem = (ShoppingListItem) item;
            mItems.subList(position, position+shoppingListItem.getIngredients().size()+1).clear();
            notifyItemRangeRemoved(position, shoppingListItem.getIngredients().size()+1);
        }else{
            Ingredient ingredient = (Ingredient) item;
            if (((ShoppingListItem)mItems.get(ingredient.getShoppingListItemIndex())).getIngredients().size() == 1){
                removeItem(position-1);
                return;
            }
            mItems.remove(ingredient);
            ((ShoppingListItem)mItems.get(ingredient.getShoppingListItemIndex())).getIngredients().remove(ingredient);
            notifyItemRemoved(position);
        }
    }

    public void restoreItem(Object object, int position){
        if(object instanceof ShoppingListItem){
            ShoppingListItem shoppingListItem = (ShoppingListItem) object;
            mItems.add(position, object);
            mItems.addAll(position+1, shoppingListItem.getIngredients());
            notifyItemInserted(position);
        }else{
            Ingredient ingredient = (Ingredient) object;
            mItems.add(position, ingredient);
            ((ShoppingListItem) mItems.get(ingredient.getShoppingListItemIndex())).getIngredients().add(ingredient);
            notifyItemInserted(position);
        }
    }

    public void persist(){
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();
        for (Object o : mItems){
            if (o instanceof ShoppingListItem){
                shoppingListItems.add((ShoppingListItem) o);
            }
        }
        ShoppingListDAO.getInstance(mContext).persistShoppingListItems(shoppingListItems);
        updateDataSource();
    }

    public List<Object> getMItems(){
        return mItems;
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = mContext.getLayoutInflater();
        ViewHolder holder;
        ShoppingListItem item = mItems.get(position);
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shopping_list_row, null, true);
            holder.imageView = convertView.findViewById(R.id.shopping_row_iv);
            holder.textView = convertView.findViewById(R.id.shopping_row_tv);
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
        Picasso.get().load(Constants.UPLOAD_FOLDER_URL+"/"+item.getRecipe().getImageURL()).into(holder.imageView);
        holder.textView.setText(item.getRecipe().getName());

        return convertView;
    }*/
}

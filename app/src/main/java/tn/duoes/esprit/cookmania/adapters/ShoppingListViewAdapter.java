package tn.duoes.esprit.cookmania.adapters;

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
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;
import tn.duoes.esprit.cookmania.utils.Constants;
import tn.duoes.esprit.cookmania.utils.ListUtils;

public class ShoppingListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mItems;

    public ShoppingListViewAdapter(List<ShoppingListItem> items){
        super();
        mItems = ListUtils.flattenList(new ArrayList<Object>(items), new ListUtils.IListUtils<Ingredient>() {
            @Override
            public Object key(Object o) {
                return ((ShoppingListItem) o).getRecipe();
            }

            @Override
            public List<Ingredient> nested(Object o) {
                return ((ShoppingListItem) o).getIngredients();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItems.get(position);
        if (item instanceof Recipe){
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

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof IngredientViewHolder){
            Ingredient ingredient = (Ingredient) mItems.get(position);
            IngredientViewHolder holder = (IngredientViewHolder) viewHolder;
            holder.nameTextView.setText(ingredient.getName());
            holder.unitTextView.setText(ingredient.getQuantity()+" "+ingredient.getUnit());
        }else{
            Recipe recipe = (Recipe) mItems.get(position);
            RecipeViewHolder holder = (RecipeViewHolder) viewHolder;
            Glide.with(holder.itemView).load(Constants.UPLOAD_FOLDER_URL+"/"+recipe.getImageURL()).into(holder.recipeImageView);
            holder.recipeTextView.setText(recipe.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView recipeImageView;
        TextView recipeTextView;

        RecipeViewHolder(@NonNull View itemView){
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.shopping_row_iv);
            recipeTextView = itemView.findViewById(R.id.shopping_row_tv);
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView unitTextView;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.shopping_ingredient_row_name);
            unitTextView = itemView.findViewById(R.id.shopping_ingredient_row_unit);
        }
    }

    public void removeItem(int position){
        mItems.remove(position);
    }

    public void restoreItem(Object object, int position){
        mItems.add(position, object);
        notifyItemInserted(position);
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

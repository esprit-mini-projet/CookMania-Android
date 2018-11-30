package tn.duoes.esprit.cookmania.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;
import tn.duoes.esprit.cookmania.utils.Constants;

public class ShoppingListViewAdapter extends RecyclerView.Adapter<ShoppingListViewAdapter.ViewHolder> {

    private List<ShoppingListItem> mItems;

    public ShoppingListViewAdapter(List<ShoppingListItem> items){
        super();
        this.mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopping_list_row, viewGroup, false);
        return new ShoppingListViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ShoppingListItem item = mItems.get(position);

        Picasso.get().load(Constants.UPLOAD_FOLDER_URL+"/"+item.getRecipe().getImageURL()).into(viewHolder.imageView);
        viewHolder.textView.setText(item.getRecipe().getName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shopping_row_iv);
            textView = itemView.findViewById(R.id.shopping_row_tv);
        }
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

package tn.duoes.esprit.cookmania.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;
import tn.duoes.esprit.cookmania.utils.Constants;

public class ShoppingListViewAdapter extends ArrayAdapter{

    private class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    private Activity mContext;
    private List<ShoppingListItem> mItems;

    public ShoppingListViewAdapter(Activity context, List<ShoppingListItem> items){
        super(context, R.layout.shopping_list_row, items);
        this.mContext = context;
        this.mItems = items;
    }

    @Override
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
    }
}

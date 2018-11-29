package tn.duoes.esprit.cookmania.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;
import tn.duoes.esprit.cookmania.utils.Constants;

public class ShoppingListViewAdapter extends ArrayAdapter{

    private class ViewHolder{
        public NetworkImageView imageView;
        public TextView textView;
    }

    private Activity mContext;
    private List<ShoppingListItem> mItems;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public ShoppingListViewAdapter(Activity context, List<ShoppingListItem> items){
        super(context, R.layout.shopping_list_row, items);
        this.mContext = context;
        this.mItems = items;

        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
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
        holder.imageView.setImageUrl(Constants.UPLOAD_FOLDER_URL+"/"+item.getRecipe().getImageURL(), mImageLoader);
        holder.textView.setText(item.getRecipe().getName());

        return convertView;
    }
}

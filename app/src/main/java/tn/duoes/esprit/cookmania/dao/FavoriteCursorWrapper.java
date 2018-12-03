package tn.duoes.esprit.cookmania.dao;

import android.database.Cursor;
import android.database.CursorWrapper;

public class FavoriteCursorWrapper extends CursorWrapper {

    public FavoriteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public int getRecipeId(){
        return getInt(getColumnIndex(DataBaseSchema.FavoriteTable.Cols.RECIPE_ID));
    }

    public int getCount(){
        return getInt(getColumnIndex("nb"));
    }
}

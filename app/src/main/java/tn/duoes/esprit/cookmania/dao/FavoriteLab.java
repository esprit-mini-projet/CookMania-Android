package tn.duoes.esprit.cookmania.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLab {
    private static final String TAG = "FavoriteLab";

    private SQLiteDatabase mDatabase;

    private static FavoriteLab instance;

    public static FavoriteLab getInstance(Context context){
        if(instance == null){
            instance = new FavoriteLab(context);
        }
        return instance;
    }

    private FavoriteLab(Context context){
        mDatabase = new DataBaseHelper(context).getWritableDatabase();
    }

    private ContentValues getContentValues(int recipeId, String userId){
        ContentValues values = new ContentValues();
        values.put(DataBaseSchema.FavoriteTable.Cols.USER_ID, userId);
        values.put(DataBaseSchema.FavoriteTable.Cols.RECIPE_ID, recipeId);
        return values;
    }

    public long insert(int recipeId, String userId){
        ContentValues values = getContentValues(recipeId, userId);
        return mDatabase.insert(DataBaseSchema.FavoriteTable.NAME, null, values);
    }

    public void delete(int recipeId, String userId){
        mDatabase.delete(
                DataBaseSchema.FavoriteTable.NAME,
                DataBaseSchema.FavoriteTable.Cols.USER_ID + " = ? AND "
                        + DataBaseSchema.FavoriteTable.Cols.RECIPE_ID + " = " + recipeId,
                new String[]{userId}
        );
    }

    public FavoriteCursorWrapper getQuery(String[] columns, String selection, String[] selectionArgs){
        Cursor cursor = mDatabase.query(DataBaseSchema.FavoriteTable.NAME, columns, selection, selectionArgs, null, null, null);
        return new FavoriteCursorWrapper(cursor);
    }

    public List<Integer> getList(String userId){
        List<Integer> idList = new ArrayList<>();
        FavoriteCursorWrapper cursorWrapper = getQuery(
                null,
                DataBaseSchema.FavoriteTable.Cols.USER_ID + " = ?",
                new String[]{userId});

        try{
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast()){
                idList.add(cursorWrapper.getRecipeId());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return idList;
    }

    public boolean recipeExists(String userId, int recipeId){
        FavoriteCursorWrapper cursorWrapper = getQuery(
                new String[]{"COUNT(*) nb"},
                DataBaseSchema.FavoriteTable.Cols.USER_ID + " = ? AND "
                        + DataBaseSchema.FavoriteTable.Cols.RECIPE_ID + " = " + recipeId,
                new String[]{userId}
        );

        try{
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCount() > 0;
        } finally {
            cursorWrapper.close();
        }
    }
}

package tn.duoes.esprit.cookmania.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dataBase.db";
    public static final int VERSION = 1;

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DataBaseSchema.FavoriteTable.NAME
                    + "(id integer primary key autoincrement, "
                    + DataBaseSchema.FavoriteTable.Cols.USER_ID + ", "
                    + DataBaseSchema.FavoriteTable.Cols.RECIPE_ID +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

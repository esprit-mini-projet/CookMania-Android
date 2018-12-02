package tn.duoes.esprit.cookmania.dao;

import android.arch.persistence.room.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.helpers.database.DatabaseHelper;
import tn.duoes.esprit.cookmania.helpers.database.ShoppingListSerialization;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;

public final class ShoppingListDAO {

    private SQLiteDatabase db;
    private List<ShoppingListItem> mItems;

    private static ShoppingListDAO instance;

    private ShoppingListDAO(Context context){
        db = new DatabaseHelper(context).getWritableDatabase();


        mItems = new ArrayList<>();

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Water", 200, "ml"));
        ingredients.add(new Ingredient("Water", 200, "ml"));
        ingredients.add(new Ingredient("Water", 200, "ml"));
        ingredients.add(new Ingredient("Water", 200, "ml"));

        List<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(new Ingredient("Water", 200, "ml"));
        ingredients1.add(new Ingredient("Water", 200, "ml"));
        ingredients1.add(new Ingredient("Water", 200, "ml"));
        ingredients1.add(new Ingredient("Water", 200, "ml"));

        Recipe recipe = new Recipe(
                "Spaghetti",
                "Test Description",
                200,
                4,
                "1.jpg",
                20,
                "au_23435465",
                5
        );

        Recipe recipe1 = new Recipe(
                "Spaghetti",
                "Test Description",
                200,
                4,
                "2.jpg",
                20,
                "au_23435465",
                5
        );

        mItems.add(new ShoppingListItem(recipe, ingredients));
        mItems.add(new ShoppingListItem(recipe1, ingredients1));
        //persistShoppingListItems(mItems);
    }

    public static ShoppingListDAO getInstance(Context context){
        if (instance == null){
            instance = new ShoppingListDAO(context);
        }
        return instance;
    }

    private String userId = "f_1491707600961513";

    public List<ShoppingListItem> getShoppingItems(){
        String selectQuery = "SELECT * FROM "+ShoppingListSerialization.TABLE_NAME +" WHERE "+ShoppingListSerialization.USER_ID+" = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});
        if(!cursor.moveToFirst()) return null;
        return new ShoppingListSerialization(userId, cursor.getString(cursor.getColumnIndex(ShoppingListSerialization.SERIALIZATION))).getShoppingListItems();
    }

    public void persistShoppingListItems(List<ShoppingListItem> items){
        ShoppingListSerialization shoppingListSerialization = new ShoppingListSerialization();
        shoppingListSerialization.setUserId(userId);
        shoppingListSerialization.setShoppingItems(items);
        if(getShoppingItems() == null){
            ContentValues values = new ContentValues();
            values.put(ShoppingListSerialization.USER_ID, shoppingListSerialization.getUserId());
            values.put(ShoppingListSerialization.SERIALIZATION, shoppingListSerialization.getShoppingListSerialization());
            db.insert(ShoppingListSerialization.TABLE_NAME, null, values);
        }else{
            ContentValues values = new ContentValues();
            values.put(ShoppingListSerialization.SERIALIZATION, shoppingListSerialization.getShoppingListSerialization());

            db.update(ShoppingListSerialization.TABLE_NAME, values, ShoppingListSerialization.USER_ID+" = ?", new String[]{userId});
        }
        //mItems =
    }

}

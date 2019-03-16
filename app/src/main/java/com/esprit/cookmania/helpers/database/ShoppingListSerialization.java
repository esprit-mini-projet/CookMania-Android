package com.esprit.cookmania.helpers.database;

import com.esprit.cookmania.models.ShoppingListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ShoppingListSerialization {

    public static final String TABLE_NAME = "shopping_list";

    public static final String USER_ID = "user_id";
    private String userId;

    public static final String SERIALIZATION = "serialization";
    private String shoppingListSerialization;

    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+USER_ID+" TEXT PRIMARY KEY "+", "+SERIALIZATION+" TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    public ShoppingListSerialization() { }

    public ShoppingListSerialization(String userId, String shoppingListSerialization) {
        this.userId = userId;
        this.shoppingListSerialization = shoppingListSerialization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShoppingListSerialization() {
        return shoppingListSerialization;
    }

    public void setShoppingListSerialization(String shoppingListSerialization) {
        this.shoppingListSerialization = shoppingListSerialization;
    }

    public List<ShoppingListItem> getShoppingListItems(){
        Gson gson = new Gson();
        return gson.fromJson(shoppingListSerialization, new TypeToken<List<ShoppingListItem>>(){}.getType());
    }

    public void setShoppingItems(List<ShoppingListItem> shoppingItems){
        Gson gson = new Gson();
        shoppingListSerialization = gson.toJson(shoppingItems);
    }
}

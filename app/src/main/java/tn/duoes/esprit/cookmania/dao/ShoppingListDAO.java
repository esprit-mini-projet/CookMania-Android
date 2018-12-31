package tn.duoes.esprit.cookmania.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.helpers.database.DatabaseHelper;
import tn.duoes.esprit.cookmania.helpers.database.ShoppingListSerialization;
import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;

public final class ShoppingListDAO {

    private static final String TAG = ShoppingListDAO.class.getSimpleName();
    private SQLiteDatabase db;
    private String userId;
    //private List<ShoppingListItem> mItems;

    private static ShoppingListDAO instance;

    private ShoppingListDAO(Context context){
        db = new DatabaseHelper(context).getWritableDatabase();
        userId = context.getSharedPreferences(context.getString(R.string.prefs_name), Context.MODE_PRIVATE)
                .getString(context.getString(R.string.prefs_user_id), "");
        //deleteShoppingList();
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
        recipe.setId(1);

        Ingredient ingredient = new Ingredient(9, "TEST", 200, "ml");
        Ingredient ingredient2 = new Ingredient(10, "Water", 200, "ml");

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Water", 200, "ml"));
        ingredients.add(new Ingredient(2, "Water", 200, "ml"));
        ingredients.add(new Ingredient(3, "Water", 200, "ml"));
        ingredients.add(new Ingredient(4, "Water", 200, "ml"));

        List<Ingredient> ingredients1 = new ArrayList<>();
        ingredients1.add(new Ingredient(5, "Water", 200, "ml"));
        ingredients1.add(new Ingredient(6, "Water", 200, "ml"));
        ingredients1.add(new Ingredient(7, "Water", 200, "ml"));
        ingredients1.add(new Ingredient(8, "Water", 200, "ml"));

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
        recipe1.setId(2);

        /*addIngredient(recipe, ingredient);
        addIngredient(recipe, ingredient2);
        addRecipe(recipe1, ingredients);*/
    }

    public void addIngredient(Recipe recipe, Ingredient ingredient) {
        List<ShoppingListItem> shoppingListItems = getShoppingItems();
        if (shoppingListItems == null) {
            shoppingListItems = new ArrayList<>();
        }
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        ShoppingListItem shoppingListItem = new ShoppingListItem(recipe, ingredients);
        if (shoppingListItems.contains(shoppingListItem)) {
            shoppingListItem = shoppingListItems.get(shoppingListItems.lastIndexOf(shoppingListItem));
            if (!shoppingListItem.getIngredients().contains(ingredient)) {
                shoppingListItem.getIngredients().add(ingredient);
            }
        } else {
            shoppingListItems.add(shoppingListItem);
        }
        persistShoppingListItems(shoppingListItems);
    }

    public void addRecipe(Recipe recipe, List<Ingredient> ingredients) {
        List<ShoppingListItem> shoppingListItems = getShoppingItems();
        if (shoppingListItems == null) {
            shoppingListItems = new ArrayList<>();
        }
        ShoppingListItem shoppingListItem = new ShoppingListItem(recipe, ingredients);
        if (shoppingListItems.contains(shoppingListItem)) {
            shoppingListItem = shoppingListItems.get(shoppingListItems.lastIndexOf(shoppingListItem));
            for (Ingredient ingredient : ingredients) {
                if (!shoppingListItem.getIngredients().contains(ingredient)) {
                    shoppingListItem.getIngredients().add(ingredient);
                }
            }
        } else {
            shoppingListItems.add(shoppingListItem);
        }
        persistShoppingListItems(shoppingListItems);
    }

    public void removeIngredient(Recipe recipe, Ingredient ingredient) {
        List<ShoppingListItem> shoppingListItems = getShoppingItems();
        ShoppingListItem shoppingListItem = new ShoppingListItem(recipe);
        if (shoppingListItems == null || !shoppingListItems.contains(shoppingListItem)) {
            return;
        }
        int index = shoppingListItems.lastIndexOf(shoppingListItem);
        shoppingListItem = shoppingListItems.get(index);
        if (!shoppingListItem.getIngredients().contains(ingredient)) {
            return;
        }
        if (shoppingListItem.getIngredients().size() == 1) {
            removeRecipe(recipe);
            return;
        }
        shoppingListItem.getIngredients().remove(ingredient);
        shoppingListItems.remove(index);
        shoppingListItems.add(index, shoppingListItem);
        persistShoppingListItems(shoppingListItems);
    }

    public void removeRecipe(Recipe recipe) {
        List<ShoppingListItem> shoppingListItems = getShoppingItems();
        ShoppingListItem shoppingListItem = new ShoppingListItem(recipe);
        if (shoppingListItems == null || !shoppingListItems.contains(shoppingListItem)) {
            return;
        }
        shoppingListItems.remove(shoppingListItems.lastIndexOf(shoppingListItem));
        persistShoppingListItems(shoppingListItems);
    }

    public List<Ingredient> getRecipeIngredients(Recipe recipe) {
        List<ShoppingListItem> shoppingListItems = getShoppingItems();
        ShoppingListItem shoppingListItem = new ShoppingListItem(recipe);
        if (shoppingListItems == null || !shoppingListItems.contains(shoppingListItem)) {
            return null;
        }
        return shoppingListItems.get(shoppingListItems.lastIndexOf(shoppingListItem)).getIngredients();
    }

    public static ShoppingListDAO getInstance(Context context){
        if (instance == null){
            instance = new ShoppingListDAO(context);
        }
        return instance;
    }

    public List<ShoppingListItem> getShoppingItems(){
        String selectQuery = "SELECT * FROM "+ShoppingListSerialization.TABLE_NAME +" WHERE "+ShoppingListSerialization.USER_ID+" = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});
        if(!cursor.moveToFirst()) return null;
        return new ShoppingListSerialization(userId, cursor.getString(cursor.getColumnIndex(ShoppingListSerialization.SERIALIZATION))).getShoppingListItems();
    }

    public void persistShoppingListItems(List<ShoppingListItem> items){
        if (items.size() == 0){
            deleteShoppingList();
            return;
        }
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
    }

    private void deleteShoppingList(){
        db.delete(ShoppingListSerialization.TABLE_NAME, ShoppingListSerialization.USER_ID+" = ?", new String[]{userId});
    }

}
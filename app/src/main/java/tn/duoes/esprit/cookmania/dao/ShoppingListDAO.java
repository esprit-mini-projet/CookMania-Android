package tn.duoes.esprit.cookmania.dao;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;

public final class ShoppingListDAO {

    private List<ShoppingListItem> mItems;

    private static ShoppingListDAO instance;

    private ShoppingListDAO(){
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
    }

    public static ShoppingListDAO getInstance(){
        if (instance == null){
            instance = new ShoppingListDAO();
        }
        return instance;
    }

    public List<ShoppingListItem> getShoppingItems(){
        return mItems;
    }

    public void removeShoppingIngredient(ShoppingListItem item, Ingredient ingredient){
        System.out.println(mItems);
        mItems.get(mItems.indexOf(item)).getIngredients().remove(ingredient);
        System.out.println(mItems);
    }

    public void removeShoppingListItem(ShoppingListItem item){
        mItems.remove(item);
    }

    public void persistShoppingListItems(List<ShoppingListItem> items){
        mItems = items;
    }

}

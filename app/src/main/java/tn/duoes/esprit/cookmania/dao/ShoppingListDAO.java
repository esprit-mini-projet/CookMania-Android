package tn.duoes.esprit.cookmania.dao;

import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.models.Ingredient;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.ShoppingListItem;

public final class ShoppingListDAO {

    public static List<ShoppingListItem> getShoppingItems(){
        List<ShoppingListItem> items = new ArrayList<>();

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Water", 200, "ml"));
        ingredients.add(new Ingredient("Water", 200, "ml"));
        ingredients.add(new Ingredient("Water", 200, "ml"));
        ingredients.add(new Ingredient("Water", 200, "ml"));

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

        items.add(new ShoppingListItem(recipe, ingredients));

        return items;
    }

}

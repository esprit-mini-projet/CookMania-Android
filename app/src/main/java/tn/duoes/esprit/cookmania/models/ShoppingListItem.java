package tn.duoes.esprit.cookmania.models;

import java.util.List;

public class ShoppingListItem {

    private Recipe recipe;
    private List<Ingredient> ingredients;

    public ShoppingListItem(Recipe recipe, List<Ingredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }



    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "ShoppingListItem{" +
                "recipe=" + recipe +
                ", ingredients=" + ingredients +
                '}';
    }
}

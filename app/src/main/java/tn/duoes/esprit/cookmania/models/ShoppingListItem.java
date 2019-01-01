package tn.duoes.esprit.cookmania.models;

import java.util.List;

public class ShoppingListItem {

    private Recipe recipe;
    private List<Ingredient> ingredients;

    public ShoppingListItem(Recipe recipe, List<Ingredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public ShoppingListItem(Recipe recipe) {
        this.recipe = recipe;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListItem that = (ShoppingListItem) o;
        return recipe.getId() == that.recipe.getId();
    }

    @Override
    public int hashCode() {

        return recipe.getId();
    }
}
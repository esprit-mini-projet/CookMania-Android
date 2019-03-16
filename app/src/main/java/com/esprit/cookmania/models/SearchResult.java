package com.esprit.cookmania.models;

public class SearchResult {
    private Recipe recipe;
    private int height;
    private int width;

    public SearchResult(Recipe recipe, int height, int width) {
        this.recipe = recipe;
        this.height = height;
        this.width = width;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "recipe=" + recipe +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}

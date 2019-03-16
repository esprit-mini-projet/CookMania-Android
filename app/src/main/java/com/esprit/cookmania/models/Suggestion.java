package com.esprit.cookmania.models;

import java.util.List;

public class Suggestion {
    private String title;
    private List<Recipe> recipes;

    public Suggestion() {
    }

    public Suggestion(String title, List<Recipe> recipes) {
        this.title = title;
        this.recipes = recipes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "title='" + title + '\'' +
                ", recipes=" + recipes +
                '}';
    }
}

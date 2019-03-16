package com.esprit.cookmania.models;

public class FeedResult {
    private User user;
    private Recipe recipe;

    public FeedResult(User user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "FeedResult{" +
                "user=" + user +
                ", recipe=" + recipe +
                '}';
    }
}

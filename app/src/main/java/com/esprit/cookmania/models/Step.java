package com.esprit.cookmania.models;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Step {
    private int id;
    private int recipeId;
    private String description;
    @SerializedName("image_url")
    private String imageUrl;
    private int time;
    private List<Ingredient> ingredients;

    private File image;

    public Step(String description, String imageUrl, int time) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.time = time;
        ingredients = new ArrayList<>();
    }

    public Step(String description, int time, List<Ingredient> ingredients, File image) {
        this.description = description;
        this.time = time;
        this.ingredients = ingredients;
        this.image = image;
    }

    public Step(String description, String imageUrl, int time, List<Ingredient> ingredients) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.time = time;
        this.ingredients = ingredients;
    }

    public Step(int id, String description, String imageUrl, int time, List<Ingredient> ingredients) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.time = time;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", recipeId=" + recipeId +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", time=" + time +
                ", ingredients=" + ingredients +
                ", image=" + image +
                '}';
    }
}

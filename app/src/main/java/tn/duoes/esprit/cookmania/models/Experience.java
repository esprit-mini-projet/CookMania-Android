package tn.duoes.esprit.cookmania.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Experience {

    @SerializedName("recipe_id")
    private int recipeId;
    private float rating;
    private String comment;
    @SerializedName("image_url")
    private String imageUrl;
    private Date date;
    @SerializedName("user")
    private User user;

    private transient String recipeOwnerId;

    public Experience() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecipeOwnerId() {
        return recipeOwnerId;
    }

    public void setRecipeOwnerId(String recipeOwnerId) {
        this.recipeOwnerId = recipeOwnerId;
    }
}

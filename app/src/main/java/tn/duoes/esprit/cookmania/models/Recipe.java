package tn.duoes.esprit.cookmania.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private String description;
    private int calories;
    private int servings;

    @SerializedName("image_url")
    private String imageURL;

    private Date date;
    private int views;
    private int favorites;
    private int time;

    @SerializedName("user_id")
    private String userId;
    private float rating;

    public Recipe() {
    }

    public Recipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.calories = in.readInt();
        this.servings = in.readInt();
        this.imageURL = in.readString();
        this.date = (Date) in.readSerializable();
        this.views = in.readInt();
        this.favorites = in.readInt();
        this.time = in.readInt();
        this.userId = in.readString();
        this.rating = in.readFloat();
    }

    public Recipe(String name, String description, int calories, int servings, String imageURL, int time, String userId, float rating) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.servings = servings;
        this.imageURL = imageURL;
        this.time = time;
        this.userId = userId;
        this.rating = rating;
    }

    public Recipe(String name, String description, int calories, int servings, String imageURL, Date date, int views, int favorites, int time, String userId, float rating) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.servings = servings;
        this.imageURL = imageURL;
        this.date = date;
        this.views = views;
        this.favorites = favorites;
        this.time = time;
        this.userId = userId;
        this.rating = rating;
    }

    public Recipe(int id, String name, String description, int calories, int servings, String imageURL, Date date, int views, int favorites, int time, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.servings = servings;
        this.imageURL = imageURL;
        this.date = date;
        this.views = views;
        this.favorites = favorites;
        this.time = time;
        this.userId = userId;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", servings=" + servings +
                ", imageURL='" + imageURL + '\'' +
                ", date=" + date +
                ", views=" + views +
                ", favorites=" + favorites +
                ", time=" + time +
                ", userId='" + userId + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(calories);
        dest.writeInt(servings);
        dest.writeString(imageURL);
        dest.writeInt(views);
        dest.writeInt(favorites);
        dest.writeInt(time);
        dest.writeString(userId);
        dest.writeFloat(rating);
    }


    /*

    //Parcelable

    public Recipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.imageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imageURL);
    }*/
}

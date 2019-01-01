package tn.duoes.esprit.cookmania.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<String> labels;
    private List<Step> steps;

    private transient File image;
    private transient List<Ingredient> mIngredients;

    public Recipe() {
        steps = new ArrayList<>();
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
        this.steps = (ArrayList<Step>) in.readSerializable();
    }

    public Recipe(String name, String description, int calories, int servings, int time, String userId, File image, List<String> labels) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.servings = servings;
        this.time = time;
        this.userId = userId;
        this.image = image;
        this.labels = labels;
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
        steps = new ArrayList<>();
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
        steps = new ArrayList<>();
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
        steps = new ArrayList<>();
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
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
                ", steps=" + steps +
                ", labels= "+labels+
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

    public List<Ingredient> getIngredients(){
        if (mIngredients != null) return mIngredients;
        mIngredients = new ArrayList<>();
        for(Step step : steps){
            mIngredients.addAll(step.getIngredients());
        }
        return mIngredients;
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

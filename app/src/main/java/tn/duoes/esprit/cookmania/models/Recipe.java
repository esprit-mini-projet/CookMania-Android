package tn.duoes.esprit.cookmania.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private String imageURL;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Recipe(String name, String imageURL) {

        this.name = name;
        this.imageURL = imageURL;
    }

    public Recipe(int id, String name, String imageURL) {

        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
    }

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
    }
}

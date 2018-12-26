package tn.duoes.esprit.cookmania.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("id")
    private String mId;
    @SerializedName("username")
    private String mUserName;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("image_url")
    private String mImageUrl;
    @SerializedName("date")
    private Date mDate;
    @SerializedName("followers")
    private int mFollowers;
    @SerializedName("following")
    private int mFollowing;
    @SerializedName("uuid")
    private String mUuid;
    @SerializedName("token")
    private String mToken;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getFollowers() {
        return mFollowers;
    }

    public void setFollowers(int followers) {
        mFollowers = followers;
    }

    public int getFollowing() {
        return mFollowing;
    }

    public void setFollowing(int following) {
        mFollowing = following;
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId='" + mId + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mDate=" + mDate +
                ", mFollowers=" + mFollowers +
                ", mFollowing=" + mFollowing +
                "}\n";
    }
}

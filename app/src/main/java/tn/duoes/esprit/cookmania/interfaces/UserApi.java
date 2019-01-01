package tn.duoes.esprit.cookmania.interfaces;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import tn.duoes.esprit.cookmania.models.User;

public interface UserApi {

    @POST("social/check")
    Call<User> createFromSocialMedia(@Body User user);

    @GET("check_email/{email}")
    Call<JsonObject> checkEmail(@Path("email") String email);

    @POST("insert")
    Call<JsonObject> createFromEmail(@Body User user);

    @POST("signin")
    Call<User> signInWithEmail(@Body User user);

    @GET("{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("cover/{id}")
    Call<String> getUserCoverPhoto(@Path("id") String id);

    @Multipart
    @POST("update_photo")
    Call<String> updatePhoto(@Part MultipartBody.Part image,
                               @Part("user_id") RequestBody userId);

    @GET("following_list/{id}")
    Call<List<User>> getFollowing(@Path("id") String id);

    @GET("follower_list/{id}")
    Call<List<User>> getFollowers(@Path("id") String id);

    @GET("is_following/{id1}/{id2}")
    Call<Boolean> isFollowing(@Path("id1") String followerId, @Path("id2") String followedId);

    @POST("follow/{id1}/{id2}")
    Call<Void> follow(@Path("id1") String followerId, @Path("id2") String followedId);

    @DELETE("unfollow/{id1}/{id2}")
    Call<Void> unfollow(@Path("id1") String followerId, @Path("id2") String followedId);

    @PUT("update_cred/{id}/{email}/{username}/{password}")
    Call<Void> updateCredentials(@Path("id") String id,
                                 @Path("email") String email,
                                 @Path("username") String username,
                                 @Path("password") String password);

    @DELETE("delete/{id}")
    Call<Void> delete(@Path("id") String id);
}

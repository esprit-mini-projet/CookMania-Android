package tn.duoes.esprit.cookmania.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
}

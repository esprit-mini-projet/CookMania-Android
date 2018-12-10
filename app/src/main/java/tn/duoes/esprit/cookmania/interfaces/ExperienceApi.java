package tn.duoes.esprit.cookmania.interfaces;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import tn.duoes.esprit.cookmania.models.Experience;

public interface ExperienceApi {

    @POST("add")
    @Multipart
    Call<Void> addExperience(@Part("rating") RequestBody rating,
                                  @Part("comment") RequestBody comment,
                                  @Part("recipe_id") RequestBody recipeId,
                                  @Part("user_id") RequestBody userId,
                                  @Part MultipartBody.Part imageFile);

    @GET("fetch/{recipeID}/{userID}")
    Call<List<Experience>> getExperience(@Path("recipeID") int recipeId, @Path("userID") String userId);

    @GET("delete/{recipeID}/{userID}")
    Call<Void> deleteExperience(@Path("recipeID") int recipeId, @Path("userID") String userId);

    @GET("recipe/{recipeID}")
    Call<List<Experience>> getExperiencesForRecipe(@Path("recipeID") int recipeId);
}

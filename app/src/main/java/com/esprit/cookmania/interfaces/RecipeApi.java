package com.esprit.cookmania.interfaces;

import com.esprit.cookmania.models.FeedResult;
import com.esprit.cookmania.models.LabelCategory;
import com.esprit.cookmania.models.Recipe;
import com.esprit.cookmania.models.SearchResult;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RecipeApi {

    @GET("top")
    Call<List<Recipe>> getTopRatedRecipes();

    @GET("all/{label}")
    Call<List<Recipe>> getAllRecipiesByLabel(@Path("label") String label);

    @GET("label/Healthy")
    Call<List<Recipe>> getHealthyRecipes();

    @GET("label/Cheap")
    Call<List<Recipe>> getCheapRecipes();

    @Multipart
    @POST("add")
    Call<JsonElement> createRecipe(@Part MultipartBody.Part image,
                                   @Part("name") RequestBody name,
                                   @Part("description") RequestBody description,
                                   @Part("calories") RequestBody calories,
                                   @Part("servings") RequestBody servings,
                                   @Part("time") RequestBody time,
                                   @Part("user_id") RequestBody userId,
                                   @Part("labels") RequestBody labels);
  
    @GET("{id}")
    Call<Recipe> getRecipeById(@Path("id") String id);

    @FormUrlEncoded
    @POST("similar")
    Call<List<Recipe>> getSimilarRecipes(@Field("recipe_id") int recipeId, @Field("labels") String labels);

    @GET("user/{id}")
    Call<List<Recipe>> getRecipesByUser(@Path("id") String userId);

    @DELETE("{id}")
    Call<Void> delete(@Path("id") int id);


    @GET("labels")
    Call<List<LabelCategory>> getLabels();

    @POST("search")
    Call<List<SearchResult>> search(@Body HashMap map);

    @GET("feed/{user_id}")
    Call<List<FeedResult>> getFeed(@Path("user_id") String userId);

    @PUT("addViews/{id}")
    Call<Void> incrementViews(@Path("id") int recipeId);

    @PUT("add_favorites/{id}")
    Call<Void> incrementFavorites(@Path("id") int recipeId);

    @PUT("decrement_favorites/{id}")
    Call<Void> decrementFavorites(@Path("id") int recipeId);
}

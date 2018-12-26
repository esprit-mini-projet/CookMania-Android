package tn.duoes.esprit.cookmania.interfaces;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import tn.duoes.esprit.cookmania.models.LabelCategory;
import tn.duoes.esprit.cookmania.models.Recipe;

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
    Call<Integer> createRecipe(@Part MultipartBody.Part image,
                                    @Part("name") RequestBody name,
                                    @Part("description") RequestBody description,
                                    @Part("calories") RequestBody calories,
                                    @Part("servings") RequestBody servings,
                                    @Part("time") RequestBody time,
                                    @Part("user_id") RequestBody userId,
                                    @Part("labels") RequestBody labels);
  
    @GET("{id}")
    Call<Recipe> getRecipeById(@Path("id") String id);

    @GET("labels")
    Call<List<LabelCategory>> getLabels();
}

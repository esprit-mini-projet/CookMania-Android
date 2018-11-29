package tn.duoes.esprit.cookmania.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import tn.duoes.esprit.cookmania.models.Recipe;

public interface RecipeApi {

    @GET("top")
    Call<List<Recipe>> getTopRatedRecipes();

    @GET("label/Healthy")
    Call<List<Recipe>> getHealthyRecipes();

    @GET("label/Cheap")
    Call<List<Recipe>> getCheapRecipes();

}

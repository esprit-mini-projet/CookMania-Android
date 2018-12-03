package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tn.duoes.esprit.cookmania.interfaces.RecipeApi;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;

public final class RecipeService {

    public static final String TAG = "RecipeService";

    public interface RecipeServiceGetCallBack{
        void onResponse(List<Recipe> recipes);
        void onFailure();
    }

    public interface RecipeServiceInsertCallBack{
        void onResponse(int recipeId);
        void onFailure();
    }

    private static RecipeService instance;

    private RecipeApi mRecipeApi;

    public static RecipeService getInstance(){
        if(instance == null){
            instance = new RecipeService();
        }
        return instance;
    }

    private RecipeService(){
        Gson gson = new Gson().newBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPES_ROUTE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mRecipeApi = retrofit.create(RecipeApi.class);
    }

    public void getTopRatedRecipes(final RecipeServiceGetCallBack callBack){
        Call<List<Recipe>> call = mRecipeApi.getTopRatedRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, retrofit2.Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    callBack.onResponse(response.body());
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }

    public void getHealthyRecipes(final RecipeServiceGetCallBack callBack){
        Call<List<Recipe>> call = mRecipeApi.getHealthyRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, retrofit2.Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    callBack.onResponse(response.body());
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }

    public void getCheapRecipes(final RecipeServiceGetCallBack callBack){
        Call<List<Recipe>> call = mRecipeApi.getCheapRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, retrofit2.Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    callBack.onResponse(response.body());
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }
    
  public void getRecipeById(String id, final RecipeServiceGetCallBack callBack){
        Call<Recipe> call = mRecipeApi.getRecipeById(id);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, retrofit2.Response<Recipe> response) {
                if(response.isSuccessful()){
                    final Recipe recipe = response.body();
                    callBack.onResponse(new ArrayList<Recipe>(Arrays.asList(recipe)));
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }
  
  public void insertRecipe(Recipe recipe, final RecipeServiceInsertCallBack callBack){
        Call<Integer> call = mRecipeApi.createRecipe(recipe);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    callBack.onResponse(response.body());
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
  }
}

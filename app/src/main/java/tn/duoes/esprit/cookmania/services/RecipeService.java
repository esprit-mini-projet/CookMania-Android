package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import tn.duoes.esprit.cookmania.interfaces.RecipeApi;
import tn.duoes.esprit.cookmania.models.LabelCategory;
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

    public interface LabelGetCallBack{
        void onResponse(List<LabelCategory> categories);
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

    public void getAllRecipesByLabel(String label, final RecipeServiceGetCallBack callBack){
        Call<List<Recipe>> call = mRecipeApi.getAllRecipiesByLabel(label);
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

    public void addRecipe(Recipe recipe, final RecipeServiceInsertCallBack callBack){
        RequestBody requestImage = RequestBody.create(MediaType.parse("image/*"), recipe.getImage());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", recipe.getImage().getName(), requestImage);
        Gson gson = new Gson();

        Call<Integer> call = mRecipeApi.createRecipe(body,
                RequestBody.create(MediaType.parse("text/plain"),recipe.getName()),
                RequestBody.create(MediaType.parse("text/plain"), recipe.getDescription()),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(recipe.getCalories())),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(recipe.getServings())),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(recipe.getTime())),
                RequestBody.create(MediaType.parse("text/plain"), recipe.getUserId()),
                RequestBody.create(MediaType.parse("text/plain"), gson.toJson(recipe.getLabels())));

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

    public void getLabels(final LabelGetCallBack callBack){
        Call<List<LabelCategory>> call = mRecipeApi.getLabels();
        call.enqueue(new Callback<List<LabelCategory>>() {
            @Override
            public void onResponse(Call<List<LabelCategory>> call, Response<List<LabelCategory>> response) {
                callBack.onResponse(response.body());
                return;
            }

            @Override
            public void onFailure(Call<List<LabelCategory>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }
}

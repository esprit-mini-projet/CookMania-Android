package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tn.duoes.esprit.cookmania.interfaces.RecipeApi;
import tn.duoes.esprit.cookmania.models.FeedResult;
import tn.duoes.esprit.cookmania.models.LabelCategory;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.SearchResult;
import tn.duoes.esprit.cookmania.models.SearchWrapper;
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

    public interface RecipeServiceSimilarCallBack {
        void onGetSimilarResponse(List<Recipe> recipes);
    }

    public interface DeleteRecipeCallBack {
        void onResponse(boolean isSuccessful);
    }
    public interface LabelGetCallBack{
        void onResponse(List<LabelCategory> categories);
        void onFailure();
    }

    public interface FeedGetCallBack{
        void onResponse(List<FeedResult> feedResults);
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
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
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
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
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
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
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
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
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
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
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
        Log.d(TAG, "addRecipe: "+recipe);
        RequestBody requestImage = RequestBody.create(MediaType.parse("image/*"), recipe.getImage());
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", recipe.getImage().getName(), requestImage);
        Gson gson = new Gson();

        Call<JsonElement> call = mRecipeApi.createRecipe(body,
                RequestBody.create(MediaType.parse("text/plain"),recipe.getName()),
                RequestBody.create(MediaType.parse("text/plain"), recipe.getDescription()),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(recipe.getCalories())),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(recipe.getServings())),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(recipe.getTime())),
                RequestBody.create(MediaType.parse("text/plain"), recipe.getUserId()),
                RequestBody.create(MediaType.parse("text/plain"), gson.toJson(recipe.getLabels())));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    callBack.onResponse(response.body().getAsJsonObject().get("id").getAsInt());
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }

    public void getSimilarRecipes(final Recipe recipe, final RecipeServiceSimilarCallBack callBack) {
        Gson gson = new Gson().newBuilder().create();
        String labels = gson.toJson(recipe.getLabels());
        Call<List<Recipe>> call = mRecipeApi.getSimilarRecipes(recipe.getId(), labels);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    callBack.onGetSimilarResponse(response.body());
                    return;
                }
                callBack.onGetSimilarResponse(new ArrayList<Recipe>());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onGetSimilarResponse(new ArrayList<Recipe>());
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

    public void getRecipesByUser(final String userId, final RecipeServiceGetCallBack callBack) {
        Call<List<Recipe>> call = mRecipeApi.getRecipesByUser(userId);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    callBack.onResponse(response.body());
                    return;
                }
                callBack.onResponse(new ArrayList<Recipe>());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }
    
    public void search(SearchWrapper searchWrapper, RecipeServiceGetCallBack callBack){
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", searchWrapper.getSearchText());
        params.put("calories", searchWrapper.getCalories());
        params.put("minServings", searchWrapper.getServingsMin());
        params.put("maxServings", searchWrapper.getServingsMax());
        params.put("labels",  searchWrapper.getLabels());

        Call<List<SearchResult>> call = mRecipeApi.search(params);
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                List<Recipe> recipes = new ArrayList<>();
                for (SearchResult sr : response.body()){
                    recipes.add(sr.getRecipe());
                }
                callBack.onResponse(recipes);
            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {

            }
        });
    }

    public void delete(int recipeId, final DeleteRecipeCallBack callBack) {
        Call<Void> call = mRecipeApi.delete(recipeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callBack.onResponse(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onResponse(false);
            }
        });
    }
    
    public void getFeed(String userId, FeedGetCallBack callBack){
        Log.d(TAG, "getFeed: ");
        Call<List<FeedResult>> call = mRecipeApi.getFeed(userId);
        call.enqueue(new Callback<List<FeedResult>>() {
            @Override
            public void onResponse(Call<List<FeedResult>> call, Response<List<FeedResult>> response) {
                Log.d(TAG, "onResponse: "+response.body());
                callBack.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<FeedResult>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }

    public void incrementViews(int recipeId) {
        Log.i(TAG, "incrementViews: ");
        Call<Void> call = mRecipeApi.incrementViews(recipeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void incrementFavorites(int recipeId) {
        Log.i(TAG, "incrementFavorites: ");
        Call<Void> call = mRecipeApi.incrementFavorites(recipeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void decrementFavorites(int recipeId) {
        Log.i(TAG, "decrementFavorites: ");
        Call<Void> call = mRecipeApi.decrementFavorites(recipeId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}

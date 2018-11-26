package tn.duoes.esprit.cookmania.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.utils.Constants;

public final class RecipeService {

    public interface DataListener{
        public void onResponse(List<Recipe> recipes);
        public void onError(VolleyError error);
    }

    public static void getTopRatedRecipes(Context context, final DataListener listener){
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, Constants.RECIPES_ROUTE + "/top", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                List<Recipe> recipes = new Gson().fromJson(response, listType);
                listener.onResponse(recipes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void getHealthyRecipes(Context context, final DataListener listener){
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, Constants.RECIPES_ROUTE + "/label/Healthy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                List<Recipe> recipes = new Gson().fromJson(response, listType);
                listener.onResponse(recipes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void getCheapRecipes(Context context, final DataListener listener){
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, Constants.RECIPES_ROUTE + "/label/Cheap", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                List<Recipe> recipes = new Gson().fromJson(response, listType);
                listener.onResponse(recipes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}

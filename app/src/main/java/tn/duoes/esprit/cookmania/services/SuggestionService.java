package tn.duoes.esprit.cookmania.services;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.Suggestion;
import tn.duoes.esprit.cookmania.utils.Constants;

public final class SuggestionService {

    public interface DataListener{
        public void onResponse(Suggestion suggestion);
        public void onError(VolleyError error);
    }

    public static void getSuggestions(Context context, final DataListener listener){
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, Constants.RECIPES_ROUTE + "/suggestions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Suggestion suggestion = new Gson().fromJson(response, Suggestion.class);
                listener.onResponse(suggestion);
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

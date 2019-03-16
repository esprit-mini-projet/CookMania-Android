package com.esprit.cookmania.services;

import android.util.Log;

import com.esprit.cookmania.interfaces.SuggestionApi;
import com.esprit.cookmania.models.Suggestion;
import com.esprit.cookmania.utils.Constants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class SuggestionService {


    public static final String TAG = "SuggestionService";

    public interface SuggestionServiceCallback{
        void onResponse(Suggestion suggestion);
        void onFailure();
    }

    private static SuggestionService instance;

    private SuggestionApi mSuggestionApi;

    public static SuggestionService getInstance(){
        if(instance == null){
            instance = new SuggestionService();
        }
        return instance;
    }

    private SuggestionService(){
        Gson gson = new Gson().newBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPES_ROUTE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mSuggestionApi = retrofit.create(SuggestionApi.class);
    }

    public void getSuggestions(final SuggestionServiceCallback callBack) {
        Call<Suggestion> call = mSuggestionApi.getSuggestions();
        call.enqueue(new Callback<Suggestion>() {
            @Override
            public void onResponse(Call<Suggestion> call, retrofit2.Response<Suggestion> response) {
                if(response.isSuccessful()){
                    callBack.onResponse(response.body());
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<Suggestion> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }

}

package com.esprit.cookmania.interfaces;

import com.esprit.cookmania.models.Suggestion;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SuggestionApi {

    @GET("suggestions")
    Call<Suggestion> getSuggestions();
}

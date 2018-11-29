package tn.duoes.esprit.cookmania.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import tn.duoes.esprit.cookmania.models.Suggestion;

public interface SuggestionApi {

    @GET("suggestions")
    Call<Suggestion> getSuggestions();
}

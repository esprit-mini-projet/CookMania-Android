package tn.duoes.esprit.cookmania.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tn.duoes.esprit.cookmania.models.User;

public interface UserApi {

    @POST("social/check")
    Call<User> createFromSocialMedia(@Body User user);
}

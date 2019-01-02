package tn.duoes.esprit.cookmania.interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface StepApi {
    @Multipart
    @POST("add")
    Call<String> createStep(@Part MultipartBody.Part image,
                            @Part("description") RequestBody description,
                            @Part("time") RequestBody time,
                            @Part("recipe_id") RequestBody recipeId,
                            @Part("ingredients") RequestBody ingredients);
}

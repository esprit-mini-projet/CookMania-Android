package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tn.duoes.esprit.cookmania.interfaces.ExperienceApi;
import tn.duoes.esprit.cookmania.models.Experience;
import tn.duoes.esprit.cookmania.utils.Constants;

public class ExperienceService {

    private static final String TAG = "ExperienceService";

    private static ExperienceService instance;

    private ExperienceApi mExperienceApi;

    public static ExperienceService getInstance(){
        if(instance == null){
            instance = new ExperienceService();
        }
        return instance;
    }

    private ExperienceService(){
        Gson gson = new Gson().newBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.EXPERIENCES_ROUTE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mExperienceApi = retrofit.create(ExperienceApi.class);
    }

    public void addExperience(Experience experience, String path, final AddExperienceCallBack callBack){
        MultipartBody.Part imagePart = null;
        if(path == null){

        }else{
            File file = new File(path);
            imagePart = MultipartBody.Part.createFormData(
                    "image",
                    file.getName(),
                    RequestBody.create(MediaType.parse("image"), file));
        }
        Call<Void> call = mExperienceApi.addExperience(
                RequestBody.create(MediaType.parse("text/plain"), experience.getRating() + ""),
                RequestBody.create(MediaType.parse("text/plain"), experience.getComment()),
                RequestBody.create(MediaType.parse("text/plain"), experience.getRecipeId() + ""),
                RequestBody.create(MediaType.parse("text/plain"), experience.getUser().getId()),
                RequestBody.create(MediaType.parse("text/plain"), experience.getRecipeOwnerId()),
                imagePart
        );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callBack.onAddExperienceSuccess();
                }else{
                    callBack.onAddExperienceFailure();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onAddExperienceFailure();
            }
        });
    }

    public void getExperience(int recipeId, String userId, final GetExperienceCallBack callBack){
        Call<List<Experience>> call = mExperienceApi.getExperience(recipeId, userId);
        call.enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(Call<List<Experience>> call, Response<List<Experience>> response) {
                if(response.isSuccessful()){
                    Experience experience = response.body().size() > 0 ? response.body().get(0) : null;
                    callBack.onGetExperienceSuccess(experience);
                }else{
                    callBack.onGetExperienceFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Experience>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onGetExperienceFailure();
            }
        });
    }

    public void deleteExperience(int recipeId, String userId, final DeleteExperienceCallBack callBack){
        Call<Void> call = mExperienceApi.deleteExperience(recipeId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    callBack.onDeleteExperienceSuccess();
                }else{
                    callBack.onDeleteExperienceFailure();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onDeleteExperienceFailure();
            }
        });
    }

    public void getExperiencesForRecipe(int recipeId, final GetExperiencesCallBack callBack){
        Call<List<Experience>> call = mExperienceApi.getExperiencesForRecipe(recipeId);
        call.enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(Call<List<Experience>> call, Response<List<Experience>> response) {
                if(response.isSuccessful()){
                    callBack.onGetExperiencesSuccess(response.body());
                }else{
                    callBack.onGetExperiencesFailure();
                }
            }

            @Override
            public void onFailure(Call<List<Experience>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onGetExperiencesFailure();
            }
        });
    }

    public interface AddExperienceCallBack{
        void onAddExperienceSuccess();
        void onAddExperienceFailure();
    }
    public interface GetExperienceCallBack{
        void onGetExperienceSuccess(Experience experience);
        void onGetExperienceFailure();
    }
    public interface DeleteExperienceCallBack{
        void onDeleteExperienceSuccess();
        void onDeleteExperienceFailure();
    }
    public interface GetExperiencesCallBack{
        void onGetExperiencesSuccess(List<Experience> experiences);
        void onGetExperiencesFailure();
    }
}

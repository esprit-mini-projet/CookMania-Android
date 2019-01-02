package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tn.duoes.esprit.cookmania.interfaces.StepApi;
import tn.duoes.esprit.cookmania.models.Step;
import tn.duoes.esprit.cookmania.utils.Constants;

public class StepService {

    private static final String TAG = StepService.class.getSimpleName();

    public interface StepServiceInsertCallBack{
        void onResponse();
        void onFailure();
    }

    private static StepService instance;

    private StepApi mStepApi;

    public static StepService getInstance(){
        if(instance == null){
            instance = new StepService();
        }
        return instance;
    }

    private StepService(){
        Gson gson = new Gson().newBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.STEPS_ROUTE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mStepApi = retrofit.create(StepApi.class);
    }

    public void addStep(Step step, final StepServiceInsertCallBack callBack) {
        MultipartBody.Part body = null;
        if(step.getImage() != null){
            RequestBody requestImage = RequestBody.create(MediaType.parse("image/*"), step.getImage());
            body = MultipartBody.Part.createFormData("image", step.getImage().getName(), requestImage);
        }
        Gson gson = new Gson();

        Call<String> call = mStepApi.createStep(body,
                RequestBody.create(MediaType.parse("text/plain"), step.getDescription()),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(step.getTime())),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(step.getRecipeId())),
                RequestBody.create(MediaType.parse("text/plain"), gson.toJson(step.getIngredients())));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    callBack.onResponse();
                    return;
                }
                callBack.onFailure();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onFailure();
            }
        });
    }
}

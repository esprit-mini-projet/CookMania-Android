package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tn.duoes.esprit.cookmania.interfaces.UserApi;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.utils.Constants;

public class UserService {

    private static final String TAG = "UserService";

    private static UserService instance;

    private UserApi mUserApi;

    public static UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    private UserService(){
        Gson gson = new Gson().newBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.USERS_ROUTE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mUserApi = retrofit.create(UserApi.class);
    }

    public void createFromSocialMedia(User user, final CreateFromSocialMediaCallBack callBack){
        Call<User> call = mUserApi.createFromSocialMedia(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Error " + response.errorBody());
                    callBack.onCreateFromSocialMediaCompleted(null);
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                callBack.onCreateFromSocialMediaCompleted(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCreateFromSocialMediaCompleted(null);
            }
        });
    }

    public void checkEmail(String email, final CheckEmailCallBack callBack){
        Call<JsonObject> call = mUserApi.checkEmail(email);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: Error " + response.errorBody());
                    callBack.onCheckEmailCompleted(null);
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                Boolean exists = null;
                try{
                    exists = response.body().get("result").getAsBoolean();
                } catch (NullPointerException e){
                    Log.e(TAG, "onResponse: ", e);
                }
                callBack.onCheckEmailCompleted(exists);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCheckEmailCompleted(null);
            }
        });
    }

    public void createFromEmail(User user, final CreateFromEmailCallBack callBack){
        Call<JsonObject> call = mUserApi.createFromEmail(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "onResponse: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCreateFromEmailCompleted(null);
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                String id = null;
                try{
                    id = response.body().get("id").getAsString();
                } catch (NullPointerException e){
                    Log.e(TAG, "onResponse: ", e);
                }
                callBack.onCreateFromEmailCompleted(id);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCreateFromEmailCompleted(null);
            }
        });
    }


    public interface CreateFromSocialMediaCallBack{
        void onCreateFromSocialMediaCompleted(User user);
    }
    public interface CreateFromEmailCallBack{
        void onCreateFromEmailCompleted(String id);
    }
    public interface CheckEmailCallBack{
        void onCheckEmailCompleted(Boolean exists);
    }
}

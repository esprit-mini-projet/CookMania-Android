package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                callBack.onCompletion(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCompletion(null);
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
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                Boolean exists = null;
                try{
                    exists = response.body().get("result").getAsBoolean();
                } catch (NullPointerException e){
                    Log.e(TAG, "onResponse: ", e);
                }
                callBack.onCompletion(exists);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCompletion(null);
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
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                String id = null;
                try{
                    id = response.body().get("id").getAsString();
                } catch (NullPointerException e){
                    Log.e(TAG, "onResponse: ", e);
                }
                callBack.onCompletion(id);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCompletion(null);
            }
        });
    }

    public void signInWithEmail(User user, final SignInWithEmailCallBack callBack){
        Call<User> call = mUserApi.signInWithEmail(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "onResponse: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(null, response.code());
                    return;
                }
                Log.d(TAG, "onResponse: body: " + response.body());
                callBack.onCompletion(response.body(), 200);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCompletion(null, 500);
            }
        });
    }

    public void getUserById(String id, final CreateFromSocialMediaCallBack callBack){
        User user = new User();
        user.setEmail("kastali@gmail.om");
        user.setPassword("12345");
        signInWithEmail(user, new SignInWithEmailCallBack() {
            @Override
            public void onCompletion(User user, int statusCode) {
                callBack.onCompletion(user);
            }
        });
    }

    public interface CreateFromSocialMediaCallBack{
        void onCompletion(User user);
    }
    public interface CreateFromEmailCallBack{
        void onCompletion(String id);
    }
    public interface CheckEmailCallBack{
        void onCompletion(Boolean exists);
    }
    public interface SignInWithEmailCallBack{
        void onCompletion(User user, int statusCode);
    }
}

package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void createFromSocialMedia(User user, final UserServiceCallBack callBack){
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

    public interface UserServiceCallBack {
        /**
         * @param user
         * returns null if operation failed
         */
        void onCreateFromSocialMediaCompleted(User user);
    }
}

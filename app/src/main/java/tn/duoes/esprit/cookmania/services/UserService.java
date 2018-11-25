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

public class UserService {

    private static final String TAG = "UserService";

    private static final String Base_Url = "http://192.168.1.9:3000/users/";
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
                .baseUrl(Base_Url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mUserApi = retrofit.create(UserApi.class);
    }

    public void create(User user, final UserServiceCallBack callBack){
        Call<Object> call = mUserApi.create(user);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    String id = null;
                    try {
                        String body = new Gson().toJson(response.body());
                        Log.d(TAG, "onResponse: body: " + body);
                        JSONObject json = new JSONObject(body);
                        id = json.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callBack.onCreateCompleted(id);
                    return;
                }
                Log.d(TAG, "onResponse: Error " + response.errorBody());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCreateCompleted(null);
            }
        });
    }

    public interface UserServiceCallBack {
        /**
         * @param id
         * returns null if operation failed
         */
        void onCreateCompleted(String id);
    }
}

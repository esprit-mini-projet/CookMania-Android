package tn.duoes.esprit.cookmania.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                    Log.d(TAG, "onGetSimilarResponse: Error " + response.errorBody());
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "onGetSimilarResponse: body: " + response.body());
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
                    Log.d(TAG, "onGetSimilarResponse: Error " + response.errorBody());
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "onGetSimilarResponse: body: " + response.body());
                Boolean exists = null;
                try{
                    exists = response.body().get("result").getAsBoolean();
                } catch (NullPointerException e){
                    Log.e(TAG, "onGetSimilarResponse: ", e);
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

    public void createFromEmail(final User user, final CreateFromEmailCallBack callBack){
        Call<JsonObject> call = mUserApi.createFromEmail(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "onGetSimilarResponse: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "onGetSimilarResponse: body: " + response.body());
                String id = null;
                try{
                    id = response.body().get("id").getAsString();
                } catch (NullPointerException e){
                    Log.e(TAG, "onGetSimilarResponse: ", e);
                }
                callBack.onCompletion(id);

                //register device
                signInWithEmail(user, new SignInWithEmailCallBack() {
                    @Override
                    public void onCompletion(User user, int statusCode) {
                        //do nothing
                    }
                });
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
                        Log.d(TAG, "onGetSimilarResponse: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(null, response.code());
                    return;
                }
                Log.d(TAG, "onGetSimilarResponse: body: " + response.body());
                callBack.onCompletion(response.body(), 200);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCompletion(null, 500);
            }
        });
    }

    public void getUserById(String id, final GetUserByIdCallBack callBack){
        Call<User> call = mUserApi.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "getUserById: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "getUserById: body: " + response.body());
                callBack.onCompletion(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callBack.onCompletion(null);
            }
        });
    }

    public void getUserCoverPhoto(String id, final GetUserCoverPhotoCallBack callBack){
        Call<String> call = mUserApi.getUserCoverPhoto(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "getUserCoverPhoto: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "getUserCoverPhoto: body: " + response.body());
                callBack.onCompletion(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onGetUserCoverPhotoFailure: ", t);
                callBack.onCompletion(null);
            }
        });
    }

    public void updateUserPhoto(File photo, String userId, final UpdateUserPhotoCallBack callBack){
        RequestBody requestImage = RequestBody.create(MediaType.parse("image"), photo);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), requestImage);
        Call<String> call = mUserApi.updatePhoto(body,
                RequestBody.create(MediaType.parse("text/plain"), userId));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    callBack.onCompletion(response.body());
                    return;
                }
                callBack.onCompletion(null);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onUpdateUserPhotoFailure: ", t);
                callBack.onCompletion(null);
            }
        });
    }

    public void getFollowing(String id, final GetUsersCallBack callBack){
        Call<List<User>> call = mUserApi.getFollowing(id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "getFollowing: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onResponse(null);
                    return;
                }
                Log.d(TAG, "getFollowing: body: " + response.body());
                callBack.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "onGetFollowingFailure: ", t);
                callBack.onFailure();
            }
        });
    }

    public void getFollowers(String id, final GetUsersCallBack callBack){
        Call<List<User>> call = mUserApi.getFollowers(id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "getFollowers: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onResponse(null);
                    return;
                }
                Log.d(TAG, "getFollowers: body: " + response.body());
                callBack.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(TAG, "onGetFollowersFailure: ", t);
                callBack.onFailure();
            }
        });
    }

    public void isFollowing(String followerId, String followedId, final IsFollowingCallBack callBack){
        Call<Boolean> call = mUserApi.isFollowing(followerId, followedId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "isFollowing: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(null);
                    return;
                }
                Log.d(TAG, "isFollowing: body: " + response.body());
                callBack.onCompletion(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e(TAG, "onIsFollowingFailure: ", t);
                callBack.onCompletion(null);
            }
        });
    }

    public void follow(String followerId, String followedId, final FollowCallBack callBack){
        Call<Void> call = mUserApi.follow(followerId, followedId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "isFollowing: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(false);
                    return;
                }
                Log.d(TAG, "isFollowing: body: " + response.body());
                callBack.onCompletion(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onIsFollowingFailure: ", t);
                callBack.onCompletion(false);
            }
        });
    }

    public void unfollow(String followerId, String followedId, final FollowCallBack callBack){
        Call<Void> call = mUserApi.unfollow(followerId, followedId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    try {
                        Log.d(TAG, "isFollowing: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(false);
                    return;
                }
                Log.d(TAG, "isFollowing: body: " + response.body());
                callBack.onCompletion(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onIsFollowingFailure: ", t);
                callBack.onCompletion(false);
            }
        });
    }

    public void updateCredentials(User user, final UpdateCredCallBack callBack) {
        Call<Void> call = mUserApi.updateCredentials(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getPassword()
        );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    try {
                        Log.d(TAG, "updateCredentials: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(false);
                    return;
                }
                Log.d(TAG, "updateCredentials: body: " + response.body());
                callBack.onCompletion(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onUpdateCredentialsFailure: ", t);
                callBack.onCompletion(false);
            }
        });
    }

    public void delete(String id, final DeleteCallBack callBack) {
        Call<Void> call = mUserApi.delete(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    try {
                        Log.d(TAG, "delete: Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callBack.onCompletion(false);
                    return;
                }
                Log.d(TAG, "delete: body: " + response.body());
                callBack.onCompletion(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onDeleteFailure: ", t);
                callBack.onCompletion(false);
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
    public interface GetUserByIdCallBack{
        void onCompletion(User user);
    }
    public interface GetUserCoverPhotoCallBack{
        void onCompletion(String imageUrl);
    }
    public interface UpdateUserPhotoCallBack {
        void onCompletion(String imageUrl);
    }
    public interface GetUsersCallBack{
        void onResponse(List<User> users);
        void onFailure();
    }
    public interface IsFollowingCallBack{
        void onCompletion(Boolean isFollowing);
    }
    public interface FollowCallBack{
        void onCompletion(Boolean result);
    }
    public interface UpdateCredCallBack {
        void onCompletion(Boolean isSuccessful);
    }

    public interface DeleteCallBack {
        void onCompletion(Boolean result);
    }
}

package tn.duoes.esprit.cookmania.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.ProfileActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.models.Recipe;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.utils.Constants;

public class FireBaseNotificationService extends FirebaseMessagingService {
    private static final String TAG = FireBaseNotificationService.class.getSimpleName();
    public static final String EXPERIENCE_CHANNEL_ID = "experience_notif_id";
    public static final String EXPERIENCE_CHANNEL_NAME = "experience_notification";
    public static final String RECIPE_CHANNEL_ID = "recipe_notif_id";
    public static final String RECIPE_CHANNEL_NAME = "recipe_notification";
    public static final String FOLLOWER_CHANNEL_ID = "follower_notif_id";
    public static final String FOLLOWER_CHANNEL_NAME = "follower_notification";
    public static final int RECIPE_TYPE = 1;
    public static final int FOLLOWER_TYPE = 2;
    public static final int EXPERIENCE_TYPE = 3;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: ");
        final Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "onMessageReceived: notif type: " + data.get("notif_type"));
        switch (Integer.valueOf(data.get("notif_type"))) {
            case EXPERIENCE_TYPE:
                UserService.getInstance().getUserById(data.get("notif_user_id"), new UserService.GetUserByIdCallBack() {
                    @Override
                    public void onCompletion(User user) {
                        Glide.with(FireBaseNotificationService.this)
                                .asBitmap()
                                .load(user.getImageUrl())
                                .apply(RequestOptions.circleCropTransform().override(100, 100))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Intent intent = new Intent(FireBaseNotificationService.this, RecipeDetailsActivity.class);
                                        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, data.get("notif_id"));
                                        intent.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, false);

                                        sendNotification(EXPERIENCE_CHANNEL_ID,
                                                EXPERIENCE_CHANNEL_NAME,
                                                data.get("title"),
                                                data.get("body"),
                                                resource,
                                                intent);
                                    }
                                });
                    }
                });
                break;

            case RECIPE_TYPE:
                RecipeService.getInstance().getRecipeById(data.get("notif_id"), new RecipeService.RecipeServiceGetCallBack() {

                    @Override
                    public void onResponse(List<Recipe> recipes) {
                        Log.d(TAG, "onResponse: " + recipes.isEmpty());
                        if (recipes.isEmpty()) return;
                        Recipe recipe = recipes.get(0);
                        Glide.with(FireBaseNotificationService.this)
                                .asBitmap()
                                .load(Constants.UPLOAD_FOLDER_URL + "/" + recipe.getImageURL())
                                .apply(RequestOptions.circleCropTransform().override(100, 100))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Intent intent = new Intent(FireBaseNotificationService.this, RecipeDetailsActivity.class);
                                        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE_ID, data.get("notif_id"));
                                        intent.putExtra(RecipeDetailsActivity.EXTRA_SHOULD_FINISH, false);

                                        Log.d(TAG, "onResourceReady: ");
                                        sendNotification(RECIPE_CHANNEL_ID,
                                                RECIPE_CHANNEL_NAME,
                                                data.get("title"),
                                                data.get("body"),
                                                resource,
                                                intent);
                                    }
                                });
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                break;

            case FOLLOWER_TYPE:
                UserService.getInstance().getUserById(data.get("notif_id"), new UserService.GetUserByIdCallBack() {
                    @Override
                    public void onCompletion(User user) {
                        Glide.with(FireBaseNotificationService.this)
                                .asBitmap()
                                .load(user.getImageUrl())
                                .apply(RequestOptions.circleCropTransform().override(100, 100))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Intent intent = new Intent(FireBaseNotificationService.this, ProfileActivity.class);
                                        intent.putExtra(ProfileActivity.EXTRA_USER_ID, data.get("notif_id"));
                                        intent.putExtra(ProfileActivity.EXTRA_SHOULD_FINISH, false);

                                        sendNotification(FOLLOWER_CHANNEL_ID,
                                                FOLLOWER_CHANNEL_NAME,
                                                data.get("title"),
                                                data.get("body"),
                                                resource,
                                                intent);
                                    }
                                });
                    }
                });
                break;
        }
    }

    private void sendNotification(String channelId, String channelName, String title, String messageBody, Bitmap largeIcon, Intent actionIntent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 100, 200, 100})
                .setLargeIcon(largeIcon);

        if (actionIntent != null) {
            builder.setContentIntent(PendingIntent.getActivity(this, 0, actionIntent, 0));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d(TAG, "sendNotification: onMessageReceived >26");
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            Log.d(TAG, "sendNotification: onMessageReceived <26");
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), builder.build());
    }

    public void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: ");
    }
}
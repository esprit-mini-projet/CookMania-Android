package tn.duoes.esprit.cookmania.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tn.duoes.esprit.cookmania.R;
import tn.duoes.esprit.cookmania.controllers.activities.MainScreenActivity;
import tn.duoes.esprit.cookmania.controllers.activities.RecipeDetailsActivity;
import tn.duoes.esprit.cookmania.models.User;
import tn.duoes.esprit.cookmania.utils.NavigationUtils;

public class FireBaseNotificationService extends FirebaseMessagingService {
    private static final String TAG = FireBaseNotificationService.class.getSimpleName();
    public static final String EXPERIENCE_CHANNEL_ID = "experience_notif_id";
    public static final String EXPERIENCE_CHANNEL_NAME = "experience_notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: ");
        Map<String, String> data = remoteMessage.getData();
        switch (Integer.valueOf(data.get("notif_type"))){
            case 3:
                UserService.getInstance().getUserById(data.get("notif_user_id"), new UserService.CreateFromSocialMediaCallBack() {
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
                                    intent.putExtra(RecipeDetailsActivity.EXTRA_PARENT_ACTIVITY_CLASS, MainScreenActivity.class.getCanonicalName());

                                    sendNotification(EXPERIENCE_CHANNEL_ID,
                                        EXPERIENCE_CHANNEL_NAME,
                                        remoteMessage.getNotification().getTitle(),
                                        remoteMessage.getNotification().getBody(),
                                        resource,
                                        intent);
                                }
                            });
                    }
                });
            break;
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: "+s);
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

        if(actionIntent != null){
            builder.setContentIntent(PendingIntent.getActivity(this, 0, actionIntent, 0));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d(TAG, "sendNotification: onMessageReceived >26");
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }else{
            Log.d(TAG, "sendNotification: onMessageReceived <26");
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), builder.build());
    }

    public void handleIntent(Intent intent){
        Log.d(TAG, "handleIntent: ");
    }
}

package com.aariyan.loan_user.Notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.aariyan.loan_user.Home;
import com.aariyan.loan_user.MainActivity;
import com.aariyan.loan_user.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessaging extends FirebaseMessagingService {

    private String CHANNEL_ID = "com.aariyan.loan_user";
    private String CHANNEL_NAME = "Loan_User";

    private Context context;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //String refreshToken = FirebaseInstanceId.getInstance().getToken();

        if (firebaseUser != null) {
            updateToken(s);
        } else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TOKEN_SAVING",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //editor.putString("FCM_TOKEN",refreshToken);
            editor.putString("FCM_TOKEN",s);
            editor.commit();
        }
    }

    private void updateToken(String refreshToken) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");

        Token token = new Token(refreshToken);


        assert firebaseUser != null;
        databaseReference.child(firebaseUser.getUid()).setValue(token);


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {
//            sendNotification(remoteMessage);
//        }
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String sender = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String myself = remoteMessage.getData().get("sented");
        String videoUrl = remoteMessage.getData().get("videoUrl");
        String videoId = remoteMessage.getData().get("videoId");

//        RemoteViews remoteViews = new RemoteViews(getPackageName(), com.bluesky.aariyan_videomatching.R.drawable.main_background);
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();

        assert sender != null;
        //int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
        int j = 101;

        Intent intent = new Intent(this, Home.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();

        //bundle.putString("userid", user);
        // bundle.putString("sender", sender);
//        bundle.putString("videoUrl", videoUrl);
//        bundle.putString("videoId", videoId);
//        bundle.putString("userId", myself);
//        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //create notification channel only for API 26+ :
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importannce = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importannce);
            //channel.setDescription(contentText);

            //Now Register Channel with system :
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.setSmallIcon(Integer.parseInt(icon))
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(getBaseContext().getResources(),
//                        R.drawable.team))
                .setColor(ContextCompat.getColor(this, R.color.black))
                //.setContent(remoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                //.setCustomContentView(remoteViews)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent);

        // NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        //Now show the notification :
        NotificationManagerCompat showNotification = NotificationManagerCompat.from(this);
        showNotification.notify(i, builder.build());

        //manager.notify(i, builder.build());


    }
}

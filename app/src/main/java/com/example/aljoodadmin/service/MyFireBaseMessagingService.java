package com.example.aljoodadmin.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.aljoodadmin.R;
import com.example.aljoodadmin.view.Chat;
import com.example.aljoodadmin.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String title,message;
    Uri defuletSound;
    NotificationChannel notificationChannel;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    int i=1;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("title");
        message=remoteMessage.getData().get("text");
        Log.d("ttt","title= "+title);
        Log.d("ttt","title= "+message);
        defuletSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(),
                "default" )
                .setSmallIcon(R.drawable.logo_jood)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId("10001")
                .setContentText(remoteMessage.getData().get("text"));
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            creatChannelSound();
        }else{
            mBuilder
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.RED, 500, 5000)
                    .setAutoCancel(true);
            mNotificationManager.notify(1, mBuilder.build());

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void creatChannelSound() {
        try {

            notificationChannel = new
                    NotificationChannel( "10001" , "sound" , NotificationManager.IMPORTANCE_HIGH) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color.RED) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500}) ;
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(1, mBuilder.build());

        }catch (NullPointerException d){

        }
        Log.d("ttt","crearChannelSound");

    }
}

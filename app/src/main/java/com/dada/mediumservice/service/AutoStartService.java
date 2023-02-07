package com.dada.mediumservice.service;

import static com.dada.mediumservice.app.MyApp.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dada.mediumservice.R;
import com.dada.mediumservice.app.MyApp;

public class AutoStartService extends Service {

    private StartUpBootReceiver startUpBootReceiver;

    String appName;
    String packageName;

    @Override
    public void onCreate() {
        super.onCreate();

//        startForeground();


        //start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
        //end
      /*
       uncomment this lines

       Log.d("RECEIVER_SKJNS", "onCreate: app: "+ appName +" pack: "+packageName);

        Toast.makeText(this,"On create: app: "+ appName +" pack: "+packageName, Toast.LENGTH_SHORT).show();

        showNotifcation(this,"ABC","THIS IS DUMMY NOTIFICation");
*/


//        startForeground(9999,new Notification());

        /*startUpBootReceiver = new StartUpBootReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        registerReceiver(startUpBootReceiver, filter);*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //create floatingView
        //jai dada

        appName = intent.getStringExtra("APP_NAME");
        packageName = intent.getStringExtra("PACKAGE_NAME");

        Log.d("RECEIVER_SKJNS", "onStartCommand: app: " + appName + " pack: " + packageName);

        Toast.makeText(this, "onStartCommand: app: " + appName + " pack: " + packageName, Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        //Hide floating view
//        unregisterReceiver(startUpBootReceiver);

        Toast.makeText(this, "Auto Start: onDestroy called..... ", Toast.LENGTH_SHORT).show();


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("android.intent.action.PACKAGE_ADDED");
//        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, StartUpBootReceiver.class);
        this.sendBroadcast(broadcastIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void showNotifcation(Context context, String title, String body) {
        // Api Level < 26 o >=26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Api level >=26
//            final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    MyApp.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(context).notify(1, notification.build());

        } else {
            //less version of API 26
            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(/*notification id*/1, notificationBuilder.build());

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "background.service";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

}

package com.dada.mediumservice.service;

import static com.dada.mediumservice.app.MyApp.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dada.mediumservice.R;
import com.dada.mediumservice.app.MyApp;

public class StartUpBootReceiver  extends BroadcastReceiver {
   /* @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

        }
    }*/

    // https://stackoverflow.com/questions/45462666/notificationcompat-builder-deprecated-in-android-o


    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getData().getSchemeSpecificPart();
            // Perform operations when a new package is installed
            Toast.makeText(context, "Package Added: " + packageName, Toast.LENGTH_SHORT).show();

            Log.d("HAlo", "Package Added: " + packageName);
        }*/

        Log.d("NEW_APP_DETECTED", "Intent Action: " + intent.getAction());
        Uri data = intent.getData();
        Log.d("NEW_APP_DETECTED", " Intent DATA: " + data);


        //second added
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();

            showNotifcation(context,"New App Detected","Here is your new app: "+packageName);
            Toast.makeText(context, "Package Added: " + packageName, Toast.LENGTH_SHORT).show();
            Log.d("NEW_APP_DETECTED", "Here is your new app: "+packageName);
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("New App Installed")
                    .setContentText(packageName + " was installed.");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }

    private void showNotifcation(Context context, String title, String body) {
        //Este método muestra notificaciones compatibles con Android Api Level < 26 o >=26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Mostrar notificacion en Android Api level >=26
//            final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    MyApp.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(context).notify(1, notification.build());

        }else{
            //Mostrar notificación para Android Api Level Menor a 26
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



}

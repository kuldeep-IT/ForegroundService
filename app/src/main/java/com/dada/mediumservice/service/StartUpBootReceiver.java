package com.dada.mediumservice.service;

import static android.content.Context.WINDOW_SERVICE;
import static com.dada.mediumservice.app.MyApp.CHANNEL_ID;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dada.mediumservice.MainActivity;
import com.dada.mediumservice.R;
import com.dada.mediumservice.app.MyApp;

import org.w3c.dom.Text;

public class StartUpBootReceiver  extends BroadcastReceiver {
   /* @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

        }
    }*/

    // https://stackoverflow.com/questions/45462666/notificationcompat-builder-deprecated-in-android-o

    private View floatingView;
    private WindowManager windowManager;
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

            String appName = MyApp.getAppNameFromPkgName(context,packageName);

            showNotifcation(context,"New App Detected","Here is your new app: "+appName);

//            openWindow(context);

//            showAlertDialog(context);

            showFloatingWindow(context);

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
        // Api Level < 26 o >=26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Api level >=26
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

    private void openWindow(Context context){
        Intent i1 = new Intent(context, MainActivity.class);
//            intent.putExtra(MyDialog.NUMBER, number);
//            intent.putExtra(MyDialog.CONTACT, name);
        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//change below line in your code
        (context).startActivity(i1);
    }

    private void showAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.popup_dialog, null);
//        TextView button = dialogView.findViewById(R.id.okay_text);
        builder.setView(dialogView);
        final AlertDialog alert = builder.create();
        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alert.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setGravity(Gravity.TOP);
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                alert.dismiss();
            }
        });*/
    }

    private void showFloatingWindow(Context context){
        floatingView = LayoutInflater.from(context).inflate(R.layout.permission_floating_layout, null);

       TextView title = floatingView.findViewById(R.id.tvTitle);
       title.setText("Jai Dada");

        Button cancelButton = floatingView.findViewById(R.id.btnCancel);


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopSelf();
                if (floatingView != null) windowManager.removeView(floatingView);

            }
        });

    }




}

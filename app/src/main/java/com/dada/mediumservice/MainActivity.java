package com.dada.mediumservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dada.mediumservice.app.MyApp;
import com.dada.mediumservice.service.AutoStartService;
import com.dada.mediumservice.service.StartUpBootReceiver;

public class MainActivity extends AppCompatActivity {

    /*
     * https://medium.com/swlh/all-about-auto-start-foreground-service-in-android-8bba7569123e
     * */

    private StartUpBootReceiver startUpBootReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("IS_SERVICE_RUNNING", "onCreate: "+ MyApp.isServiceRunningInForeground(MainActivity.this, AutoStartService.class));
        /* MyApp.isServiceRunningInForeground(MainActivity.this, AutoStartService.class);*/


        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(mReceiver, filter);
    }

    public void startService(View v) {

        Intent serviceIntent = new Intent(this, AutoStartService.class);
        serviceIntent.putExtra("inputExtra", "Here is ForeGroundService");
        ContextCompat.startForegroundService(this, serviceIntent);
    }


    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, AutoStartService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        startUpBootReceiver = new StartUpBootReceiver();
        registerReceiver(startUpBootReceiver, filter);

    }

    //Extra added
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("New App Installed")
                        .setContentText(packageName + " was installed.");
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, builder.build());
            }
        }


    };

}
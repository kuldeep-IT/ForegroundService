package com.dada.mediumservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
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
    public static String[] requestedPermissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("IS_SERVICE_RUNNING", "onCreate: "+ MyApp.isServiceRunningInForeground(MainActivity.this, AutoStartService.class));
        /* MyApp.isServiceRunningInForegrou\nd(MainActivity.this, AutoStartService.class);*/

/*
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(mReceiver, filter);
        */


        AutoStartService autoStartService = new AutoStartService();
        Intent mServiceIntent = new Intent(this, autoStartService.getClass());
        if (!isMyServiceRunning(autoStartService.getClass())) {
//            startService(mServiceIntent);
            ContextCompat.startForegroundService(this, mServiceIntent);
            Log.d("AFTER_SERVICE_STATUS", ""+MyApp.isServiceRunningInForeground(this,autoStartService.getClass()));

        } else{
            ContextCompat.startForegroundService(this, mServiceIntent);
            Log.d("AFTER_SERVICE_STATUS_ELSE", ""+MyApp.isServiceRunningInForeground(this,autoStartService.getClass()));

        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }


    public void startService(View v) {

       /* Intent serviceIntent = new Intent(this, AutoStartService.class);
        serviceIntent.putExtra("inputExtra", "Here is ForeGroundService");
        ContextCompat.startForegroundService(this, serviceIntent);*/
    }


    public void stopService(View v) {
        /*Intent serviceIntent = new Intent(this, AutoStartService.class);
        stopService(serviceIntent);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

//      BroadCast receiver
        /*

        uncomment

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        startUpBootReceiver = new StartUpBootReceiver();
        registerReceiver(startUpBootReceiver, filter);*/

        //start foreground
       /* Intent serviceIntent = new Intent(this, AutoStartService.class);
        serviceIntent.putExtra("inputExtra", "Here is ForeGroundService");
        ContextCompat.startForegroundService(this, serviceIntent);*/

    }

    @Override
    protected void onDestroy() {
//        super.onDestroy();

        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("android.intent.action.PACKAGE_ADDED");
//        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, StartUpBootReceiver.class);
        sendBroadcast(broadcastIntent);
        super.onDestroy();

    }
}
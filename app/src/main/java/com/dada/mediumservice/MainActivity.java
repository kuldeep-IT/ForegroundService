package com.dada.mediumservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dada.mediumservice.app.MyApp;
import com.dada.mediumservice.service.AutoStartService;

public class MainActivity extends AppCompatActivity {

    /*
     * https://medium.com/swlh/all-about-auto-start-foreground-service-in-android-8bba7569123e
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("IS_SERVICE_RUNNING", "onCreate: "+ MyApp.isServiceRunningInForeground(MainActivity.this, AutoStartService.class));
        /* MyApp.isServiceRunningInForeground(MainActivity.this, AutoStartService.class);*/
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

}
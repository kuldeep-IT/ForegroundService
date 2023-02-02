package com.dada.mediumservice.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;

public class StartUpBootReceiver  extends BroadcastReceiver {
   /* @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("startuptest", "StartUpBootReceiver BOOT_COMPLETED");

        }
    }*/


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getData().getSchemeSpecificPart();
            // Perform operations when a new package is installed
            Toast.makeText(context, "Package Added: " + packageName, Toast.LENGTH_SHORT).show();

            Log.d("HAlo", "Package Added: " + packageName);
        }
    }

}

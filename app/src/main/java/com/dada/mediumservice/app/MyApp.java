package com.dada.mediumservice.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.dada.mediumservice.service.StartUpBootReceiver;

public class MyApp  extends Application {

    /*
    * https://medium.com/swlh/all-about-auto-start-foreground-service-in-android-8bba7569123e
    * */

    public static final String CHANNEL_ID = "autoStartServiceChannel";
    public static final String CHANNEL_NAME = "Auto Start Service Channel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        //      BroadCast receiver
 /*       IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        StartUpBootReceiver startUpBootReceiver = new StartUpBootReceiver();
        registerReceiver(startUpBootReceiver, filter);
*/


    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    //is Service is Running or Not
    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");

                return service.foreground;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }



    public static String getAppNameFromPkgName(Context context, String Packagename) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(Packagename, PackageManager.GET_META_DATA);
            String appName = (String) packageManager.getApplicationLabel(info);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

}

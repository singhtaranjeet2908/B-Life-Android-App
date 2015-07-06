package com.nyu.blife_app;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ramakrishnacmanyam on 4/26/15.
 */
public class GpsLocationReceiver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
//            Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
//                    Toast.LENGTH_SHORT).show();
//            Intent pushIntent = new Intent(context, HomeActivity.class);
//            context.startService(pushIntent);
//        }

        ContentResolver contentResolver = context.getContentResolver();

        // Find out what the settings say about which providers are enabled
        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

        if (mode == Settings.Secure.LOCATION_MODE_OFF) {
            // Location is turned OFF!
        } else {
            Log.v("Receiver","Location Added");
            Intent pushIntent = new Intent(context, HomeActivity.class);
            context.startService(pushIntent);
            // Location is turned ON!
        }


    }
}

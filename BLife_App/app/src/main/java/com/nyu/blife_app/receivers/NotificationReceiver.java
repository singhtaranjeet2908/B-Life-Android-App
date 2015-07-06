package com.nyu.blife_app.receivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import com.nyu.blife_app.ViewBloodRequestActivity;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents a customized Broadcast Receiver for Parse Push notifications that extends the ParsePushBroadcastReceiver.
 */

public class NotificationReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    private static final String PARSE_JSON_NOTIFICATION = "com.parse.Data";

    //override the onPushOpen() method to open "ViewBloodRequestActivity" instead of app's launcher activity when user taps on a Notification
    @Override
    protected void onPushOpen(Context context, Intent intent){

        //since we override onPushOpen() method, we need to track our app's push open event manually
        ParseAnalytics.trackAppOpenedInBackground(intent);

        String targetActivity = null;

        //get the value of key "target" from the JSON data received in Parse Push notification
        try {
            JSONObject pushData = getJSONDataFromIntent(intent);
            targetActivity = pushData.getString("target");
        }catch (JSONException e) {
            Log.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }

        //get the launcher activity class for the app from ParsePushBroadcastReceiver's getActivity() method
        Class<? extends Activity> cls = getActivity(context, intent);

        Intent activityIntent;
        if (targetActivity != null && !targetActivity.isEmpty()) {
            //send an explicit intent to open ViewBloodRequestActivity on tapping a push notification
            activityIntent = new Intent(context, ViewBloodRequestActivity.class);
            Log.d("notttt - ", "entering if");
        } else {
            //open the launcher activity of the app
            activityIntent = new Intent(context, cls);
            Log.d("notttt - ", "entering else");
        }

        if (Build.VERSION.SDK_INT >= 16) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(cls);
            stackBuilder.addNextIntent(activityIntent);
            stackBuilder.startActivities();
        } else {
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(activityIntent);
        }
    }


    private JSONObject getJSONDataFromIntent(Intent intent) {
        JSONObject jsonData = null;
        try {
            jsonData = new JSONObject(intent.getExtras().getString(PARSE_JSON_NOTIFICATION));
        } catch (JSONException e) {
            Log.e(TAG, "Unexpected JSONException when receiving push data: ", e);
        }
        return jsonData;
    }
}




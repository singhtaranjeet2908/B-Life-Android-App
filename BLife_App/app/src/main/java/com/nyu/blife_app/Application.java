package com.nyu.blife_app;

/**
 * Created by Taranjeet on 4/26/2015.
 */


        import android.util.Log;

        import com.parse.Parse;
        import com.parse.ParseException;
        import com.parse.ParseInstallation;
        import com.parse.ParsePush;
        import com.parse.ParseUser;
        import com.parse.PushService;

        import com.parse.ParsePush;
        import com.parse.SaveCallback;

public class Application extends android.app.Application {


    private static String TAG = "ParseApplication";

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Parse SDK.
        Parse.initialize(this, "6qUFzAHfl9bXRzzDlBegiXZx0Tw5dc29m3jXmnHt", "TS6OswWh6HwKQm2uCJxqfprlyrP2mfpkmwkx3Vg9");

        //Save the current Installation to Parse
        ParseInstallation.getCurrentInstallation().saveInBackground();

//        ParseUser user = ParseUser.getCurrentUser();
//        if (user != null){
//            Intent i = new Intent()
//
//        }

        // Specify an Activity to handle all pushes by default.
        //PushService.setDefaultPushCallback(this, MainActivity.class);

        //subscribe to a default particular channel ("broadcast" channel, here)
//        ParsePush.subscribeInBackground("", new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
//                } else {
//                    Log.e("com.parse.push", "failed to subscribe for push", e);
//                }
//            }
//        });
    }
}

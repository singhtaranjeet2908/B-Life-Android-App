package com.nyu.blife_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class RequestVerificationActivity extends ActionBarActivity {

    Button submitreq_veri;
    EditText submitreqveri_code;

    private String get_city,st_date,blood_group,loc;
    private String get_hospital;
    private String get_bloodGroup;
    private Date stDate;

    String currentUser;
    String message_code = "Request_Code",  user_content="Request_User";
    String message,full_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_verification);

        submitreq_veri = (Button) findViewById(R.id.verify_button);
        submitreqveri_code = (EditText) findViewById(R.id.verify_editText);

        //get the currently signed up user from Parse
        ParseUser signedUpUser = ParseUser.getCurrentUser();
        signedUpUser.fetchInBackground();
        currentUser = signedUpUser.getString("username");
        Log.d("Current User", currentUser);

        submitreq_veri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (intent.getStringExtra("Request_Code") != null && intent.getStringExtra("Request_User") != null) {
                    final String user_type = intent.getStringExtra("Type");
                    stDate = new Date(getIntent().getExtras().getLong("beforeDate", -1));

                    message = intent.getStringExtra(message_code);
                    full_details = intent.getStringExtra(user_content);

                String veri = submitreqveri_code.getText().toString();
                if (veri.equals(message)) {
                    String[] send_request_values = full_details.split(",");
                    String get_requestorName = send_request_values[0];
                    get_hospital = send_request_values[1];
                    get_city = send_request_values[2];
                    get_bloodGroup = send_request_values[3];
                    String get_message = send_request_values[4];
                    //Date get_requiredBefore = send_request_values[5];
                    // String Date bloodDate = get_requiredBefore.
                    String get_phoneNumber = send_request_values[5];


                    ParseQuery<ParseObject> query = ParseQuery.getQuery("BloodRequest");
                    query.whereEqualTo("requestorName", get_requestorName);
                    query.whereEqualTo("location", get_hospital);
                    query.whereEqualTo("city", get_city);
                    query.whereEqualTo("bloodGroup", get_bloodGroup);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            ParseObject p = list.get(0);
                            p.put("requestStatus", "verified");
                            p.saveInBackground();

                            /**
                             * whenever a new blood request is saved in the Parse database,
                             * send a "Targeted" Parse Push Notification to all the users(donors or receivers)
                             * belonging to the city selected in the blood request
                             */
                            sendNotification("accepted");

                            //send an implicit intent to call "ManageRequestsActivity"
                            Intent i = new Intent(RequestVerificationActivity.this, ManageRequestsScreen.class);
                            i.putExtra("Request Details", full_details);
                            i.putExtra("Verification Code", message);
                            i.putExtra("Status", "Request Posted");
                            startActivity(i);

                            finish();
                        }
                    });


//
//                    BloodRequest request = new BloodRequest();
//                    request.setrequestorName(get_requestorName);
//                    request.setLocation(get_hospital);
//                    request.setCity(get_city);
//                    request.setBloodGroup(get_bloodGroup);
//                    request.setMessage(get_message);
//                    request.setRequiredBefore(stDate);
//                    request.setCellNumber(get_phoneNumber);
//                    request.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//
//

//                            sendNotification();
//
//                            //send an implicit intent to call "ManageRequestsActivity"
//                            Intent i = new Intent(RequestVerificationActivity.this, ManageRequestsScreen.class);
//                            i.putExtra("Request Details", full_details);
//                            i.putExtra("Verification Code", message);
//                            i.putExtra("Status", "Request Posted");
//                            startActivity(i);
//
//                            finish();
//                        }
//                    });


//                    new SweetAlertDialog(RequestVerificationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//
//                            .setTitleText("Posted")
//                            .setContentText("Your Request has been Posted..")
//                            .setConfirmText("OK")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                   // sDialog.setConfirmText("Done!!");
//                                    sDialog.cancel();
//
//                                    //RequestVerificationActivity.this.finish();
//
//                                }
//                            }).show();
//                          //SweetAlertDialog sDialog = new SweetAlertDialog.;
//                    //sDialog.show();


                } else {
                    if (!veri.equals(message)) {
                        Toast.makeText(getApplicationContext(), "Wrong Code", Toast.LENGTH_SHORT).show();
                    }
//                    else
//                    {
                    //    back_Intent.putExtra("Code Empty" , "Awaiting confirmation");
//                        onBackPressed();
//                    }

                }
            }
                else{

                    blood_group=intent.getStringExtra("blood_Request");
                    String name = intent.getStringExtra("requestor_name");
                    loc =  intent.getStringExtra("location");
                    String city = intent.getStringExtra("city");
                    st_date =intent.getStringExtra("date");
                    String phone = intent.getStringExtra("phone");
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("BloodRequest");
                    query.whereEqualTo("requestorName", name);
                    query.whereEqualTo("location", loc);
                    query.whereEqualTo("city", city);
                    //query.whereEqualTo("requiredBefore", st_date);
                    query.whereEqualTo("cellNumber", phone);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            ParseObject p = list.get(0);

                            String veri = submitreqveri_code.getText().toString();
                            int k=p.getInt("verificationCode");
                            if (veri.equals(String.valueOf(p.getInt("verificationCode")))){
                                p.put("requestStatus", "verified");
                                p.saveInBackground();
                                sendNotification("awaiting");

                                //send an implicit intent to call "ManageRequestsActivity"
                                Intent i = new Intent(RequestVerificationActivity.this, ManageRequestsScreen.class);
                                i.putExtra("Request Details", full_details);
                                i.putExtra("Verification Code", message);
                                i.putExtra("Status", "Request Posted");
                                startActivity(i);

                                finish();
                            }

                        }
                    });
                }
            }
        });
    }


    //method to send a "Targeted" Parse Push Notification message created using a JSON Object
    private void sendNotification(String status)

    {
        JSONObject pushNotification;
        if(status.equals("accepted")) {
            pushNotification = getJSONDataMessage();
        }
        else{
            pushNotification = getJSONDataMessageA();

        }
        /**
         * Create our Installation query to get all the users who belong to the same city
         *as selected by the requester in the blood request.
         */
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("city", get_city);
        pushQuery.whereNotEqualTo("user", currentUser);


        // Send push notification to the query created above
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setData(pushNotification);
        push.sendInBackground();
    }

    //method to create a Parse Push notification message using a JSON Object
    private JSONObject getJSONDataMessage()
    {
        try {
            JSONObject JSONMessage = new JSONObject();
            DateFormat dateFormat = DateFormat.getDateInstance();
            String reqdBefore = dateFormat.format(this.stDate.getTime());
            String title = "\"" + get_bloodGroup + "\"" + " Blood " + "Required.";
            String alert = "At: " + get_hospital + ". Before: " + reqdBefore + ".";

            JSONMessage.put("title", title);
            JSONMessage.put("alert", alert);
            JSONMessage.put("target", "ViewBloodRequestActivity");

            return JSONMessage;
        }
        catch(JSONException e)
        {
            throw new RuntimeException("Something wrong with JSON Notification Message", e);
        }
    }
    private JSONObject getJSONDataMessageA()
    {
        try {
            JSONObject JSONMessage = new JSONObject();
            DateFormat dateFormat = DateFormat.getDateInstance();
  //          String reqdBefore = dateFormat.format(this.stDate.getTime());
            String title = "\"" + blood_group + "\"" + " Blood " + "Required.";
            String alert = "At: " + loc + ". Before: " + st_date + ".";

            JSONMessage.put("title", title);
            JSONMessage.put("alert", alert);
            JSONMessage.put("target", "ViewBloodRequestActivity");

            return JSONMessage;
        }
        catch(JSONException e)
        {
            throw new RuntimeException("Something wrong with JSON Notification Message", e);
        }
    }

    @Override
    public void onBackPressed() {
        Intent back_Intent = new Intent(RequestVerificationActivity.this, ManageRequestsScreen.class);
        startActivity(back_Intent);
        finish();
    }


}

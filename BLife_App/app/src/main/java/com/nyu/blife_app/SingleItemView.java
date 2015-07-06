package com.nyu.blife_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView nameTextView, cityTextView, locationTextView, dateTextView, bloodTextView;
    String name, city, location, cellNumber, blood, date;
    Button btn_contact_person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_view);
        btn_contact_person = (Button) findViewById(R.id.btncontact);
        //getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Intent i = getIntent();
        name = i.getStringExtra("name");
        city = i.getStringExtra("city");
        location = i.getStringExtra("location");
        cellNumber = i.getStringExtra("cellNumber");
        blood = i.getStringExtra("blood");

        Toast.makeText(this, "Phone of requester - " + cellNumber, Toast.LENGTH_SHORT).show();

        date = i.getStringExtra("requiredBefore");
        String[] onlyDate = date.split(" ");
        String mm = onlyDate[1];
        String dd = onlyDate[2];
        String yy = onlyDate[5];

        nameTextView = (TextView) findViewById(R.id.single_name);
        cityTextView = (TextView)findViewById(R.id.single_city);
        locationTextView = (TextView)findViewById(R.id.single_hos);
        dateTextView=(TextView)findViewById(R.id.single_date);
        bloodTextView = (TextView) findViewById(R.id.single_bg);

        // Load the text into the TextView
        nameTextView.setText(name);
        cityTextView.setText(city);
        locationTextView.setText(location);
        dateTextView.setText(dd+" "+mm+" "+yy);
        bloodTextView.setText(blood);

        btn_contact_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Button clicked..", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(SingleItemView.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Contact Receiver ?")
                        .setContentText("Your contact details will be shared with the Request Sender. You will be contacted at the " +
                                "discretion of the request sender. Do you want to proceed?")
                        .setCustomImage(R.mipmap.phone)
                        .setConfirmText(" Send SMS ")
                        .setCancelText("  Cancel  ")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sendSMSMessage(cellNumber);
//                                Intent call_intent = new Intent(Intent.ACTION_DIAL);
//                                call_intent.setData(Uri.parse("tel: " + cellNumber));
//                                startActivity(call_intent);
                                sDialog.cancel();
                                finish();

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    protected void sendSMSMessage(String phone)
    {
        Log.i("Send SMS", "in sms");

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.fetchInBackground();
        String phoneNo =  (currentUser.getNumber("phoneNumber")).toString();
        String curr_fname = currentUser.getString("firstName");
        String curr_lname = currentUser.getString("lastName");
        String curr_name = curr_fname + curr_lname;
        Toast.makeText(getBaseContext(), "user number - " + phoneNo, Toast.LENGTH_SHORT).show();


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, "A Donor has responded to your blood Request. You can contact "
                    + curr_name + " on - " + phoneNo  , null, null);
            Toast.makeText(getBaseContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),"SMS failed, please try again.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_item_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

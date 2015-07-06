package com.nyu.blife_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DonorsListScreenActivity extends ActionBarActivity {

    Button backbtn;


    // Declare Variables
    ListView listview;
    List<ParseUser> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    String blood, city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_list_screen);

        backbtn = (Button) findViewById(R.id.donorListBackbtn);
        listview = (ListView) findViewById(R.id.listview);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent backIntent = new Intent(DonorsListScreenActivity.this, SearchDonorsActivity.class);
                //startActivity(backIntent);
                finish();               // review it
            }
        });

        Toast.makeText(getBaseContext(), "Reached Successfully", Toast.LENGTH_SHORT).show();
        Intent i1 = getIntent();
        city = i1.getStringExtra("city");
        blood = i1.getStringExtra("blood");
        CustomAdapter adapter = new CustomAdapter(this, new ParseQueryAdapter.QueryFactory<ParseUser>() {

            // Log.i("output blood", blood[0]);
            @Override
            public ParseQuery<ParseUser> create() {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("userType", "Donor");
                query.whereEqualTo("bloodGroup", blood);
                query.whereEqualTo("city", city);

                try {
                    if(query.count() != 0)
                    {
                        Toast.makeText(DonorsListScreenActivity.this, "Donors found..",Toast.LENGTH_SHORT).show();
                        return query;
                    }
                    else
                    {
                        Toast.makeText(DonorsListScreenActivity.this,"there are no donors",Toast.LENGTH_LONG).show();

                        new SweetAlertDialog(DonorsListScreenActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(" No Donor Found ! ")
                                .setCustomImage(R.mipmap.phone)
                                .setContentText("No donor found based on search results...")
                                .setConfirmText("  OK  ")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        finish();
                                    }
                                })
                                .show();
                    }
                    //return query;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return query;
            }
        });

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String user_phone;
                TextView view1 = (TextView) view.findViewById(R.id.item_title);
                String donorname = view1.getText().toString();
                Toast.makeText(getBaseContext(), "Text view clicked. Donor Name. - " + donorname , Toast.LENGTH_SHORT).show();

                user_phone = CustomAdapter.get_number_info();
                Toast.makeText(getBaseContext(), "Donor Phone number - " + user_phone , Toast.LENGTH_SHORT).show();


                new SweetAlertDialog(DonorsListScreenActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Contact donor?")
                        .setCustomImage(R.mipmap.phone)
                        .setContentText("Your contact details will be shared with the Donor. You will be contacted at the " +
                                "discretion of the Donor. Do you want to proceed?")
                        .setConfirmText(" Send SMS ")
                        .setCancelText("  Cancel  ")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sendSMSMessage(user_phone);
                                sDialog.cancel();

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
            smsManager.sendTextMessage(phone, null, "You are requested for blood donation as you are registered as donor on BLife. " +
                    "You can contact on - " + phoneNo  , null, null);
            Toast.makeText(getBaseContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),"SMS failed, please try again.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
//        Intent back_Intent = new Intent(DonorsListScreenActivity.this, SearchDonorsActivity.class);
//        startActivity(back_Intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.fetchInBackground();
        String typeOfUser =  currentUser.getString("userType");
        Log.d("this type is", typeOfUser);
        if (typeOfUser.contentEquals("Donor")){
            getMenuInflater().inflate(R.menu.menu_home, menu);
            return true;
        }
        else {
            getMenuInflater().inflate(R.menu.sign_up, menu);
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i1 = new Intent(DonorsListScreenActivity.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(DonorsListScreenActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(DonorsListScreenActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }



}

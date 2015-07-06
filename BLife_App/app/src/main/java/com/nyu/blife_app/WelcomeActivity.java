package com.nyu.blife_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nyu.blife_app.models.BloodRequest;
import com.nyu.blife_app.models.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class WelcomeActivity extends Activity {

    Button btnSignup, btnSignin;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        check_network_status();

        /*calling the User class from models */
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(BloodRequest.class);
        //code for maintaining the sessions
         if (ParseUser.getCurrentUser() != null){
         startActivity(new Intent(this, HomeActivity.class));
         }


        btnSignup = (Button) findViewById(R.id.signUpButton);
        btnSignin = (Button) findViewById(R.id.signInButton);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                startActivity(i);

            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
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
        if (id == R.id.log_out) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void check_network_status() {
        if (isNetworkStatusAvialable(getApplicationContext())) {
        //do nothing if true
        } else {

            new SweetAlertDialog(WelcomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Network Problem!!")
                    .setContentText("Please connect to Internet!!")
                    .setConfirmText("    OK    ")
                    .setCancelText("  Cancel  ")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            finish();

                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }
    }
}

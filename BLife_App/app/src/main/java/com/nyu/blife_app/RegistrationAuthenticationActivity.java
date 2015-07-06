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

import com.nyu.blife_app.models.User;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegistrationAuthenticationActivity extends ActionBarActivity
{
    Button auth_submit;
    EditText veri_code;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_authentication);
        final String message,full_details;
        String message_code="Acceptor_Code";
        String user_content="Acceptor_User";
        final String donor_info;
        final Intent intent = getIntent();
        final String user_type=intent.getStringExtra("Type");
        if(user_type.equals("Donor"))
        {
            message_code="Donor_Code";
            user_content="Donor_User";
        }
        message = intent.getStringExtra(message_code);
        full_details = intent.getStringExtra(user_content);
        donor_info = intent.getStringExtra("donor_details");

        auth_submit = (Button) findViewById(R.id.auth_button);
        veri_code = (EditText) findViewById(R.id.auth_editText);

        auth_submit.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        if (intent.getStringExtra("Acceptor_Code") != null) {

                                String veri = veri_code.getText().toString();
                                Log.d("this verification code", veri);
                                if (veri.equals(message))
                                {
                                    Toast.makeText(getApplicationContext(), "Correct Code",Toast.LENGTH_LONG).show();
                                    if (user_type.equals("Donor"))
                                    {
                                        signup(full_details, donor_info, user_type);
                                    }
                                    else
                                    {
                                        signup(full_details, user_type);
                                    }
                                    //calling signup method
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Wrong Code",Toast.LENGTH_LONG).show();
                                }
                            }
                        else
                        {
                            String veri = veri_code.getText().toString();
                            if (veri.equals(intent.getStringExtra("Donor_Code")))
                            {
                                Toast.makeText(getApplicationContext(), "Correct Code",Toast.LENGTH_LONG).show();
                                update(donor_info);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Wrong Code",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

   /** copy paste this inside your send message method */
    /* defining signup method */

private void signup(String input_details, String input_donor, String input_type){

    //setup new parse user
    String[] parse_details=input_details.split(",");
    final String get_firstname = parse_details[0];
    final String get_lastname = parse_details[1];
    final String get_username = parse_details[2];
    final String get_password = parse_details[3];
    //final long get_phone = Integer.parseInt(parse_details[4]);
    final Long get_phone = Long.parseLong(parse_details[4]);
    final int get_zip = Integer.parseInt(parse_details[5]);
    final String get_city = parse_details[6];

    String member_type = input_type;
    String[] input_donor_values = input_donor.split(",");
    final String get_dob = input_donor_values[0];
    final String get_blood_group = input_donor_values[1];
    final String get_gender = input_donor_values[2];
    final String get_weight = input_donor_values[3];
    final String get_disease_value = input_donor_values[4];
    final String get_pregnancy = input_donor_values[5];


    ParseUser user = new ParseUser();
    user.setUsername(get_username);
    user.setPassword(get_password);
    user.put("firstName", get_firstname);
    user.put("lastName", get_lastname);
    user.put("phoneNumber",get_phone);
    user.put("zipCode", get_zip);
    user.put("city", get_city);
    user.put("dob", get_dob);
    user.put("bloodGroup", get_blood_group);
    user.put("gender", get_gender);
    user.put("weight", get_weight);
    user.put("disease", get_disease_value);
    user.put("pregnancy", get_pregnancy);
    user.put("userType", member_type);


    //call the parse signup method
    user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {

            if (e != null)
            {
                //show the error message
                Toast.makeText(RegistrationAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            else
            {
                //start an intent for homescreen activity
                Intent loggedin = new Intent(RegistrationAuthenticationActivity.this, HomeActivity.class);
                loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loggedin);
            }
        }
    });

        //create relationship between Parse Installation object and ParseUser object.
        // This allows us to send pushes to a very customized and dynamic segment of our user base.
        //i.e. This allows us to send "Targeted" Push notifications


        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //Store "city" data on the current Installation object
        installation.put("city", get_city);

        // Associate the device with a user by storing the current "username" on the current Parse Installation Object
        installation.put("user", get_username);
        installation.saveInBackground();
}


    //end of signup method
    /*start of sign up only for receivers*/

    private void signup(String input_details, String input_type){
        String[] parse_details=input_details.split(",");
        final String get_firstname = parse_details[0];
        final String get_lastname = parse_details[1];
        final String get_username = parse_details[2];
        final String get_password = parse_details[3];
        //final long get_phone = Integer.parseInt(parse_details[4]);
        final Long get_phone = Long.parseLong(parse_details[4]);
        final int get_zip = Integer.parseInt(parse_details[5]);
        final String get_city = parse_details[6];
        String member_type = input_type;

        Log.d("hello1", member_type);

        ParseUser user = new ParseUser();
        user.setUsername(get_username);
        user.setPassword(get_password);
        user.put("firstName",get_firstname);
        user.put("lastName",get_lastname);
        user.put("phoneNumber",get_phone);
        user.put("zipCode", get_zip);
        user.put("city", get_city);
        user.put("userType", member_type);

        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e) {

                if(e != null){

                    //show the error message
                    Toast.makeText(RegistrationAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
                else
                {
                    //get the currently signed up user from ParseUser
                    ParseUser signedUser = ParseUser.getCurrentUser();
                    signedUser.fetchInBackground();
                    currentUser = (signedUser.getString("username"));
                    Intent loggedin = new Intent(RegistrationAuthenticationActivity.this, HomeActivity.class);
                    loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loggedin);
                }
            }
        });

        //create relationship between Parse Installation object and ParseUser object.
        // This allows us to send pushes to a very customized and dynamic segment of our user base.
        //i.e. This allows us to send "Targeted" Push notifications


        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //Store "city" data on the current Installation object
        installation.put("city", get_city);

        // Associate the device with a user by storing the current "username" on the current Parse Installation Object
        installation.put("user", get_username);
        installation.saveInBackground();
    }

    private void update(String donor_register){
        String[] separate_details = donor_register.split(",");
        final String get_dob = separate_details[0]+" "+separate_details[1];
        final String get_blood_group = separate_details[2];
        final String get_gender = separate_details[3];
        final String get_weight = separate_details[4];
        final String get_disease_value = separate_details[5];
        final String get_pregnancy = separate_details[6];
        ParseUser user = ParseUser.getCurrentUser();
        user.put("dob",get_dob);
        user.put("bloodGroup",get_blood_group);
        user.put("gender", get_gender);
        user.put("weight",get_weight);
        user.put("disease",get_disease_value);
        user.put("pregnancy",get_pregnancy);
        user.put("userType", "Donor");

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null){
                    //save was successful
                    Toast.makeText(getBaseContext(),"Successfully registered as donor",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrationAuthenticationActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(getBaseContext(),"registration failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}


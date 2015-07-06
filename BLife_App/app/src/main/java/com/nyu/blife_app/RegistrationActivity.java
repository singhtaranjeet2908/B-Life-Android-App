package com.nyu.blife_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegistrationActivity extends ActionBarActivity {

    Button register;
    static EditText EditTextfname,EditTextlname,EditTextusrname,EditTextpassword,EditTextphone,EditTextZip;
    Spinner spinnerSelectCity;
    CheckBox checkBoxDonor;
    String phoneno,city_selected;
    Button registerbtn;
    EditText phone_number;
    Context registration=this;
    public final static String EXTRA_MESSAGE = "com.mycompany.Blife.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        registerbtn = (Button) findViewById(R.id.registerButton);
        EditTextfname = (EditText) findViewById(R.id.editFirstName);
        EditTextlname = (EditText) findViewById(R.id.editLastName);
        EditTextusrname = (EditText) findViewById(R.id.editUserName);
        EditTextpassword = (EditText) findViewById(R.id.editPassword);
        EditTextphone = (EditText) findViewById(R.id.editPhoneNumber);
        EditTextZip = (EditText) findViewById(R.id.editZipcode);
        spinnerSelectCity = (Spinner) findViewById(R.id.spinnerCity);
        checkBoxDonor = (CheckBox) findViewById(R.id.check_Donor);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String get_firstname = EditTextfname.getText().toString();
                final String get_lastname = EditTextlname.getText().toString();
                final String get_username = EditTextusrname.getText().toString();
                final String get_password = EditTextpassword.getText().toString();
                final String get_phone = EditTextphone.getText().toString();
                final String get_zip = EditTextZip.getText().toString();
                final String get_city = spinnerSelectCity.getSelectedItem().toString();


                final boolean fname_bool = validateFirstname(get_firstname);
                final boolean lname_bool = validateLastname(get_lastname);
                final boolean usrname_bool = validateUsername(get_username);
                final boolean pwd_bool = validatepassword(get_password);
                validatePhone(get_phone);
                validateZip(get_zip);
                validatespinnerCity(get_city);

                final String conc_string = get_firstname + "," + get_lastname + "," + get_username + "," +
                        get_password + "," + get_phone + "," + get_zip + "," + get_city;

                if(!get_city.equals("CITY"))
                {
                    if (fname_bool && lname_bool && usrname_bool && pwd_bool)
                    {
                        if (checkBoxDonor.isChecked()) {
                            Intent intent = new Intent(RegistrationActivity.this, DonorRegistrationActivity.class);
                            intent.putExtra("Phone_Number", get_phone);
                            intent.putExtra("User",conc_string);
                            startActivity(intent);

                        }
                        else {
                            //SMS code
                            if (!get_city.equals("CITY"))
                            {
                                if(isMobileAvailable(registration))
                                {
                                    sendSMSMessage(conc_string);
                                }
                                else{
                                    new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("SMS Failed..")
                                            .setContentText("Check for Cellular Connectivity.")
                                            .setConfirmText("   OK   ")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                }
                                            })
                                            .show();
                                }
                            }
                            else {
                                new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("City is empty!!")
                                        .setContentText("Please enter valid city!!!")
                                        .setConfirmText("   OK   ")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();

                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                }

                else
                {
                    new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Registration Failed!!")
                            .setContentText("Please check Details..")
                            .setConfirmText("   OK   ")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();

                                }
                            })
                            .show();
                }
            }

            private void validatespinnerCity(String get_city) {

                if(get_city.equals("CITY"))
                {
                    spinnerSelectCity.requestFocus();
                    Toast.makeText(getBaseContext(),"Select proper City",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    city_selected = get_city;
                }

            }

            private boolean validateFirstname(String get_firstname) {

                if (get_firstname.equals("")) {
                    EditTextfname.requestFocus();
                    EditTextfname.setError("Required Field!");
                    return false;
                }
                else if (!get_firstname.matches("[a-zA-Z ]+")) {
                    EditTextfname.requestFocus();
                    EditTextfname.setError("Only alphabets");
                    return false;
                }

                else {
                    return true;
                }
            }

            private boolean validateLastname(String get_lastname) {

                if (get_lastname.equals("")) {
                    EditTextlname.requestFocus();
                    EditTextlname.setError("Required Field!");
                    return false;
                }
                else if (!get_lastname.matches("[a-zA-Z ]+")) {
                    EditTextlname.requestFocus();
                    EditTextlname.setError("Only alphabets");
                    return false;
                }
                else {
                    return true;
                }
            }

            private boolean validateUsername(String get_username) {
                if (get_username.equals("")) {
                    EditTextusrname.requestFocus();
                    EditTextusrname.setError("Required Field!");
                    return false;
                }
                else if (!get_username.matches("^[a-z0-9_-]{3,15}$")) {
                    EditTextusrname.requestFocus();
                    EditTextusrname.setError("Enter valid username. /n Must have one number");
                    return false;
                }
                else {
                    return true;
                }
            }

            private boolean validatepassword(String get_password) {

                if (get_password.equals("")) {
                    EditTextpassword.requestFocus();
                    EditTextpassword.setError("Required Field!");
                    return false;
                }
                else if (!get_password.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")) {
                    EditTextpassword.requestFocus();
                    EditTextpassword.setError("Minimum 6 characters atleast one digit and one uppercase");
                    return false;
                }
                else if(get_password.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")){
                    //if password format is correct
                    return true;
                }
                else{
                    return true;
                }
            }

            private String validatePhone(String get_phone) {
                if (get_phone.equals("") || (!get_phone.matches("[0-9]{10}"))){
                    EditTextphone.requestFocus();
                    EditTextphone.setError("Enter valid phone number");
                }
                else
                {
                    phoneno = get_phone;
                }
                return phoneno;
            }

            private void validateZip(String get_zip) {

                if (get_zip.equals("") || (!get_zip.matches("\\d{5}"))) {
                    EditTextZip.requestFocus();
                    EditTextZip.setError("Enter valid zipcode.");
                }
            }
        });
    }

    protected void sendSMSMessage(String send_user)
    {
        Log.i("Send SMS", "");
        String phoneNo = EditTextphone.getText().toString();
        String my_name = generatePIN();
        String type_user ="Acceptor";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null,my_name , null, null);
            Toast.makeText(getBaseContext(), "SMS sent.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, RegistrationAuthenticationActivity.class);
            intent.putExtra("Acceptor_Code", my_name);
            intent.putExtra("Acceptor_User",send_user);
            intent.putExtra("Type",type_user);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            new SweetAlertDialog(RegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("SMS Failed..")
                    .setContentText("Check for Cellular Connectivity.")
                    .setConfirmText("   OK   ")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();

                        }
                    })
                    .show();
            e.printStackTrace();
        }
    }
    public String generatePIN()
    {
        int x=9000;
        int full=(int)((Math.random()*x)+1000);
        return String.valueOf(full);

    }


    @Override
    public void onBackPressed() {
        Intent back_Intent = new Intent(RegistrationActivity.this, WelcomeActivity.class);
        startActivity(back_Intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signInButton) {
            Intent i3 = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(i3);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static Boolean isMobileAvailable(Context appcontext) {
        TelephonyManager tel = (TelephonyManager) appcontext.getSystemService(Context.TELEPHONY_SERVICE);
        return ((tel.getNetworkOperator() != null && tel.getNetworkOperator().equals("")) ? false : true);
    }
}

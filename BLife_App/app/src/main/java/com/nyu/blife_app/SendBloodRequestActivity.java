package com.nyu.blife_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.blife_app.models.BloodRequest;
import com.nyu.blife_app.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SendBloodRequestActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {

    EditText etDonorname, etHospital, etMessage, etDonorphone;
    Spinner donorCity, donorBloodGroup;
    TextView tvRequireddate;
    Button donorRequestbtn;
    private String donorcity_selected,phoneno,bloodgroup_selected;
    private Date storeDate;
    Context sendbloodrequest=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_blood_request);
        Parse.initialize(this, "6qUFzAHfl9bXRzzDlBegiXZx0Tw5dc29m3jXmnHt", "TS6OswWh6HwKQm2uCJxqfprlyrP2mfpkmwkx3Vg9");
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(BloodRequest.class);
        donorCity = (Spinner) findViewById(R.id.spinnerCityBR);
        donorCity.setFocusable(true);
        donorCity.setFocusableInTouchMode(true);
        etDonorname = (EditText) findViewById(R.id.editName);
        etHospital = (EditText) findViewById(R.id.editHospital);
        etMessage = (EditText) findViewById(R.id.editoptMSG);
        etDonorphone = (EditText) findViewById(R.id.editPhoneNumberBR);
        donorBloodGroup = (Spinner) findViewById(R.id.spinnerBRBloodGroup);
        tvRequireddate = (TextView) findViewById(R.id.sendBRDate);
        donorRequestbtn = (Button) findViewById(R.id.sendBRButton);

        donorRequestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String get_name = etDonorname.getText().toString();
                final String get_hospital = etHospital.getText().toString();
                final String get_phone = etDonorphone.getText().toString();
                final String get_message = etMessage.getText().toString();
                final String get_donorcity = donorCity.getSelectedItem().toString();
                final String get_donorBloodGroup = donorBloodGroup.getSelectedItem().toString();

                final boolean name_bool = validateName(get_name);
                final boolean hospital_bool = validateHospital(get_hospital);
                final boolean phone_bool = validatedonorPhone(get_phone);
                validatedonorCity(get_donorcity);
                validateBloodGroup(get_donorBloodGroup);

                final String user_details = get_name + "," + get_hospital + "," + get_donorcity + "," +
                        get_donorBloodGroup + "," + get_message + "," + get_phone;


                if(storeDate != null) {
                    if (!get_donorcity.equals("CITY") && !get_donorBloodGroup.equals("BLOOD GROUP")) {
                        if (name_bool && hospital_bool && phone_bool) {
                            //Database posting of request and send verification code

                            if (isMobileAvailable(sendbloodrequest)) {
                                verifySMSMessage(user_details, storeDate);
                            } else {
                                new SweetAlertDialog(SendBloodRequestActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                        } else {
                            new SweetAlertDialog(SendBloodRequestActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Check Details.")
                                    .setContentText("Enter valid Details..")
                                    .setConfirmText("   OK   ")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();

                                        }
                                    })
                                    .show();
                        }

                    } else {
                        new SweetAlertDialog(SendBloodRequestActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("All fields mandatory")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                }).show();
                    }
                }
                else
                {
                    new SweetAlertDialog(SendBloodRequestActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Date empty..")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Toast.makeText(getBaseContext(), "All Fields mandatory.", Toast.LENGTH_SHORT).show();
                                    sDialog.cancel();
                                }
                            }).show();
                }
            }



            private String validateBloodGroup(String get_donorBloodGroup) {
                if(get_donorBloodGroup.equals("BLOOD GROUP"))
                {
                    donorBloodGroup.requestFocus();
                }
                else
                {
                    bloodgroup_selected = get_donorBloodGroup;
                }
                return bloodgroup_selected;
            }

            private void validatedonorCity(String get_donorcity) {
                if(get_donorcity.equals("CITY"))
                {
                    donorCity.requestFocus();
                }
                else
                {
                    donorcity_selected = get_donorcity;
                }
            }

            private boolean validatedonorPhone(String get_phone)
            {
                if ((get_phone.equals("")) && (!get_phone.matches("[0-9]{10}")))
                {
                    etDonorphone.requestFocus();
                    etDonorphone.setError("Enter valid phone number");
                    return false;
                }
                else
                {
                    return true;
                }
            }

            private boolean validateHospital(String get_hospital) {
                if (get_hospital.equals(""))
                {
                    etHospital.requestFocus();
                    etHospital.setError("Required Field! (Enter valid Hospital name)");
                    return false;
                }

                else {
                    return true;
                }
            }

            private boolean validateName(String get_name)
            {
                if (get_name.equals("") || !get_name.matches("[a-zA-Z ]+"))
                {
                    etDonorname.requestFocus();
                    etDonorname.setError("Required Field! (Valid Name)");
                    return false;
                }
                else
                {
                    return true;
                }
            }
        });
    }

    //method to display a Date Picker Dialog Fragment on click of 'Date Of Birth' TextView

    public void showDatePickerDialog(View view) {
        Log.d("DonorReg", "Entered showDialog");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = DatePickerDialogFragment.newInstance(SendBloodRequestActivity.this);

        newFragment.show(ft, "Select Date");
        Log.d("DonorReg", "Exited showDialog");
    }

    public static class DatePickerDialogFragment extends DialogFragment {

        protected DatePickerDialog.OnDateSetListener mDateSetListener;

        //Empty default constructor to prevent our app from crashing when the device is rotated.
        public DatePickerDialogFragment()
        {
            // nothing to see here, move along
        }

        public static DialogFragment newInstance(DatePickerDialog.OnDateSetListener callback)
        {
            DatePickerDialogFragment dFragment = new DatePickerDialogFragment();
            dFragment.mDateSetListener = callback;
            return dFragment;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            final Calendar cal = Calendar.getInstance();

            return new DatePickerDialog(getActivity(),mDateSetListener, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        final Calendar now = new GregorianCalendar();

        if (cal.before(now)){
            tvRequireddate.setError("cannot select a past date");
            tvRequireddate.setText("REQUIRED BEFORE");
        }
        else {
            DateFormat dateFormat = DateFormat.getDateInstance();
            tvRequireddate.setText(dateFormat.format(cal.getTime()));
            tvRequireddate.setError(null);
            storeDate = cal.getTime();
        }
    }

    public String generatePIN()
    {
        int x=9000;
        int full=(int)((Math.random()*x)+1000);
        return String.valueOf(full);
    }

    protected void verifySMSMessage(String send_user, Date stDate)
    {
        Log.i("Send SMS", "");
        String phoneNo = etDonorphone.getText().toString();
        String my_name = generatePIN();
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.fetchInBackground();
        String username = currentUser.getString("username");
        String type_user ="Donor";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, my_name, null, null);
            Toast.makeText(getBaseContext(), "    SMS sent.    ",Toast.LENGTH_LONG).show();
            String[] separateUserDetails = send_user.split(",");
            BloodRequest request = new BloodRequest();
            request.setrequestorName(separateUserDetails[0]);
            request.setLocation(separateUserDetails[1]);
            request.setCity(separateUserDetails[2]);
            request.setBloodGroup(separateUserDetails[3]);
            request.setMessage(separateUserDetails[4]);
            request.setRequiredBefore(stDate);
            request.setCellNumber(separateUserDetails[5]);
            request.setRequestStatus("Awaiting");
            request.setVerificationCode(Integer.parseInt(my_name));
            request.setUserName(username);

            request.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.d("database addition - ", "value added to database");
                }
            });

            Intent verify_intent = new Intent(this, RequestVerificationActivity.class);
            verify_intent.putExtra("Request_Code", my_name);
            verify_intent.putExtra("Request_User", send_user);
            verify_intent.putExtra("Type", type_user);
            verify_intent.putExtra("beforeDate", stDate.getTime());
            startActivity(verify_intent);
            finish();
        }
        catch (Exception e)
        {
            new SweetAlertDialog(SendBloodRequestActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                Intent i1 = new Intent(SendBloodRequestActivity.this, SettingsActivity.class);

                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();

                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(SendBloodRequestActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(SendBloodRequestActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);

                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();

                break;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        //Intent back_Intent = new Intent(SendBloodRequestActivity.this, HomeActivity.class);
        //startActivity(back_Intent);
        finish();
    }

    public static Boolean isMobileAvailable(Context appcontext) {
        TelephonyManager tel = (TelephonyManager) appcontext.getSystemService(Context.TELEPHONY_SERVICE);
        return ((tel.getNetworkOperator() != null && tel.getNetworkOperator().equals("")) ? false : true);
    }

}

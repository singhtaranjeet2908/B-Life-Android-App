package com.nyu.blife_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DonorRegistrationActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {

    TextView bDateTextView, txtviewgender, txtviewdisease, txtviewpregnant;
    Spinner spinnerselectBloodGroup;
    EditText edittextWeight;
    RadioGroup radiogrpGender, radiogrpDisease, radiogrpPregnant;
    RadioButton radiobtnGender, radiobtnDisease, radiobtnPregnant;
    Button registerbtnDonor;
    String weight,bloodgroup_selected;
    String conc_radio;
    Context donorregistration=this;
    public final static String EXTRA_MESSAGE = "com.mycompany.Blife.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);

        Intent intent = getIntent();
        final String get_phoneno = intent.getStringExtra("Phone_Number");
        final String get_reg_details=intent.getStringExtra("User");

        bDateTextView = (TextView)findViewById(R.id.txtDOB);
        bDateTextView.setFocusable(true);
        bDateTextView.setFocusableInTouchMode(true);
        registerbtnDonor = (Button) findViewById(R.id.registerButton_Donor);

        txtviewgender = (TextView)findViewById(R.id.txtGender);
        txtviewdisease = (TextView)findViewById(R.id.txtDisease);
        txtviewpregnant = (TextView)findViewById(R.id.txtPregnant);
        spinnerselectBloodGroup = (Spinner)findViewById(R.id.spinnerDonorBloodGroup);
        spinnerselectBloodGroup.setFocusable(true);
        spinnerselectBloodGroup.setFocusableInTouchMode(true);
        edittextWeight = (EditText) findViewById(R.id.editWeight);
        radiogrpGender = (RadioGroup) findViewById(R.id.radioGroupGender);
        radiogrpDisease = (RadioGroup) findViewById(R.id.radioGroupDisease);
        radiogrpPregnant = (RadioGroup) findViewById(R.id.radioGroupPregnant);


        registerbtnDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_date = bDateTextView.getText().toString();
                String get_weight = edittextWeight.getText().toString();
                String get_BloodGroup = spinnerselectBloodGroup.getSelectedItem().toString();


                String id_gender = addListenerRadioGroupGender();
                String id_disease = addListenerRadioGroupDisease();
                String id_pregnant = addListenerRadioGroupPregnant();
                conc_radio = get_date +","+get_BloodGroup + ","+id_gender + ","+get_weight+","+ id_disease+","+id_pregnant;
                Log.d("hello", conc_radio);

                validateWeight(get_weight);
                validateBloodGroup(get_BloodGroup);
                final Intent intent = getIntent();
                if (intent.getStringExtra("User")!= null){
                    if(!get_weight.isEmpty() && !get_BloodGroup.isEmpty())
                    {
                        if((!id_gender.equals("")) && ((!id_disease.equals("")) && (!id_disease.equals("YES"))) &&
                                ((!id_pregnant.equals("")) && (!id_pregnant.equals("YES"))))
                        {
                            if(isMobileAvailable(donorregistration)) {
                                sendSMSMessage(get_phoneno, get_reg_details, conc_radio);
                            }
                            else
                            {
                                new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                        else
                        {
                            new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Donor Registration Failed!!")
                                    .setContentText("All fields are required. A Donor cannot be pregnant or have a Disease.")
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
                    else
                    {
                        new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Donor Registration Failed!!")
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
                else {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.fetchInBackground();
                    Number phone = currentUser.getNumber("phoneNumber");

                    if(!get_weight.isEmpty() && !get_BloodGroup.isEmpty())
                    {
                        if((!id_gender.equals("")) && ((!id_disease.equals("")) && (!id_disease.equals("YES"))) &&
                                ((!id_pregnant.equals("")) && (!id_pregnant.equals("YES"))))
                        {
                            if(isMobileAvailable(donorregistration))
                            {
                                sendDonorSMSMessage( phone, conc_radio);
                            }
                            else
                            {
                                new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                        else
                        {
                            new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Donor Registration Failed!!")
                                    .setContentText("All fields are required. A Donor cannot be pregnant or have a Disease.")
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
                    else
                    {
                        new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Donor Registration Failed!!")
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
            }


            private String validateBloodGroup(String get_BloodGroup) {

                if(get_BloodGroup.equals("BLOOD GROUP"))
                {
                    spinnerselectBloodGroup.requestFocus();
                    Toast.makeText(getBaseContext(),"Select valid Blood Group entry.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    bloodgroup_selected = get_BloodGroup;
                }
                return bloodgroup_selected;
            }
            private String validateWeight(String get_weight) {

                if (get_weight.equals("") ||  Integer.parseInt(get_weight)<110 ){
                    edittextWeight.requestFocus();
                    edittextWeight.setError("Enter valid data.(Less than 110lbs)");
                    edittextWeight.setText("");
                }
                else
                {
                    weight = get_weight;
                }
                return weight;
            }
        });
    }

    private String addListenerRadioGroupPregnant() {
        int id_selected = radiogrpPregnant.getCheckedRadioButtonId();
        radiobtnPregnant = (RadioButton) findViewById(id_selected);
        String pregnancy = "";
        if(id_selected <= 0)
        {
            txtviewpregnant.setError("Select option..");
            return pregnancy;

        }
        else
        {
            pregnancy = radiobtnPregnant.getText().toString();
            txtviewpregnant.setError(null);
            return pregnancy;
        }
    }
    private String addListenerRadioGroupDisease() {
        int id_selected = radiogrpDisease.getCheckedRadioButtonId();
        String infect_disease="";
        radiobtnDisease = (RadioButton) findViewById(id_selected);
        if(id_selected <= 0)
        {
            txtviewdisease.setError("Select option..");
            return infect_disease;
        }
        else
        {
            infect_disease = radiobtnDisease.getText().toString();
            txtviewdisease.setError(null);
            return infect_disease;
        }
    }
    private String addListenerRadioGroupGender() {
        int id_selected = radiogrpGender.getCheckedRadioButtonId();
        radiobtnGender = (RadioButton) findViewById(id_selected);
        String genderName="";
        if(id_selected <= 0)
        {
            txtviewgender.setError("Select Gender..");
            return genderName;
        }
        else
        {

            genderName = radiobtnGender.getText().toString();
            Log.d("hello", genderName);
            txtviewgender.setError(null);
            return genderName;
        }

    }

    protected void sendSMSMessage(String phone,String send_details, String send_radio_values)
    {
        Log.i("Send SMS", "");
        String phoneNo = phone;
        String type_user ="Donor";
        String my_name = generatePIN();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null,my_name , null, null);
            Toast.makeText(getBaseContext(), "SMS sent.",Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(this, RegistrationAuthenticationActivity.class);
            intent1.putExtra("Donor_Code", my_name);
            intent1.putExtra("Donor_User",send_details);
            intent1.putExtra("Type",type_user);
            intent1.putExtra("Acceptor_Code","New");
            intent1.putExtra("donor_details", send_radio_values);

            startActivity(intent1);
        } catch (Exception e) {
            new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
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

    protected void sendDonorSMSMessage(Number phone, String send_radio_values)
    {
        Log.i("Send SMS", "");
        Number phoneNo = phone;
        String type_user ="Donor";
        String my_name = generatePIN();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(String.valueOf(phoneNo), null,my_name , null, null);
            Toast.makeText(getBaseContext(), "SMS sent.",Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(this, RegistrationAuthenticationActivity.class);
            intent1.putExtra("Donor_Code", my_name);
            intent1.putExtra("Type",type_user);
            intent1.putExtra("donor_details", send_radio_values);
            startActivity(intent1);
        } catch (Exception e) {
            new SweetAlertDialog(DonorRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
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

    //method to display a Date Picker Dialog Fragment on click of 'Date Of Birth' TextView

    public void showDatePickerDialog(View view) {
        Log.d("DonorReg", "Entered showDialog");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = DatePickerDialogFragment.newInstance(DonorRegistrationActivity.this);

        newFragment.show(ft, "Select Date");
        Log.d("DonorReg", "Exited showDialog");
    }

    /**
     * This inner class represents a reusable Date Picker hosted in a Dialog Fragment for B-Life app.
     * Code Reference : http://www.codinguser.com/2012/06/time-and-date-inputs-in-android/
     *
     */

    public static class DatePickerDialogFragment extends DialogFragment {

        protected DatePickerDialog.OnDateSetListener mDateSetListener;

        //Empty default constructor to prevent our app from crashing when the device is rotated.
        public DatePickerDialogFragment() {
            // nothing to see here, move along
        }

        public static DialogFragment newInstance(DatePickerDialog.OnDateSetListener callback){
            Log.d("DonorReg", "Entered newInstance");
            DatePickerDialogFragment dFragment = new DatePickerDialogFragment();
            dFragment.mDateSetListener = callback;
            Log.d("DonorReg", "Exited newInstance");
            return dFragment;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal = Calendar.getInstance();

            return new DatePickerDialog(getActivity(),mDateSetListener, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
    }


    //method to set the date selected from the Date Picker on the txtDOB TextView
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);


        final Calendar now = new GregorianCalendar();
        int age = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
                || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        if (cal.after(now)){
            bDateTextView.setError("cannot select a future date");
            bDateTextView.setText("DATE OF BIRTH");
        }
        else if (age < 17){
            bDateTextView.setError("age fault....less than 17 yrs..");
            bDateTextView.setText("DATE OF BIRTH");
        }
        else {
            DateFormat dateFormat = DateFormat.getDateInstance();
            bDateTextView.setText(dateFormat.format(cal.getTime()));
            bDateTextView.setError(null);
        }
    }


    @Override
    public void onBackPressed() {
        //Intent back_Intent = new Intent(DonorRegistrationActivity.this, RegistrationActivity.class);
        //startActivity(back_Intent);
        finish();
    }




    public static Boolean isMobileAvailable(Context appcontext) {
        TelephonyManager tel = (TelephonyManager) appcontext.getSystemService(Context.TELEPHONY_SERVICE);
        return ((tel.getNetworkOperator() != null && tel.getNetworkOperator().equals("")) ? false : true);
    }
}

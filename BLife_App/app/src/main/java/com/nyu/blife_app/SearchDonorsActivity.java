package com.nyu.blife_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseUser;


public class SearchDonorsActivity extends ActionBarActivity{

    Spinner spinnerBloodGroup, spinnerCity;
    EditText zipCode;
    Button btnSearchDonors;
    String bgroupvalidate_input, cityvalidate_input;
    Boolean Bgroup_bool, City_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donors);

        spinnerBloodGroup = (Spinner) findViewById(R.id.spinnerBloodGroup);
        spinnerBloodGroup.setFocusable(true);
        spinnerBloodGroup.setFocusableInTouchMode(true);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        zipCode = (EditText) findViewById(R.id.editTextZipCode);
        btnSearchDonors = (Button) findViewById(R.id.buttonSearchDonors);



        spinnerBloodGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bgroupvalidate_input = parent.getItemAtPosition(position).toString();
                Bgroup_bool = validate(bgroupvalidate_input);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityvalidate_input = parent.getItemAtPosition(position).toString();
                City_bool = validate(cityvalidate_input);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSearchDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String get_zip = zipCode.getText().toString();
                final String select_blood = spinnerBloodGroup.getSelectedItem().toString();
                final String select_city = spinnerCity.getSelectedItem().toString();
                if(!get_zip.equals(""))
                {
                    if(Bgroup_bool && City_bool) {
                        Toast.makeText(getBaseContext(), "Blood group - " + bgroupvalidate_input, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), "City - " + cityvalidate_input, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), "Zip - " + get_zip, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SearchDonorsActivity.this, DonorsListScreenActivity.class);
                        i.putExtra("blood", bgroupvalidate_input);
                        i.putExtra("city", cityvalidate_input);
                        i.putExtra("zip", get_zip);
                        startActivity(i);
                        //finish();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),"Select valid entry.",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(Bgroup_bool && City_bool) {
                        Toast.makeText(getBaseContext(), "Blood group - " + bgroupvalidate_input, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), "City - " + cityvalidate_input, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), "Zip is empty - " + get_zip, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SearchDonorsActivity.this, DonorsListScreenActivity.class);
                        i.putExtra("blood", bgroupvalidate_input);
                        i.putExtra("city", cityvalidate_input);
                        i.putExtra("zip", get_zip);
                        startActivity(i);
                        //finish();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),"Select valid entry.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
//        Intent back_Intent = new Intent(SearchDonorsActivity.this, HomeActivity.class);
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
                Intent i1 = new Intent(SearchDonorsActivity.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(SearchDonorsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(SearchDonorsActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }




    public Boolean validate(String get_inputDetail)
    {

        if(get_inputDetail.equals("BLOOD GROUP"))
        {
            spinnerBloodGroup.requestFocus();
            //Toast.makeText(getBaseContext(), "Select valid entry.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(get_inputDetail.equals("CITY"))
        {
            spinnerBloodGroup.requestFocus();
            //Toast.makeText(getBaseContext(),"Select valid entry.",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            Toast.makeText(getBaseContext(), "Selected entry - " + get_inputDetail,Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}

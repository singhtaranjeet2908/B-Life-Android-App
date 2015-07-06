package com.nyu.blife_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;


public class HealthTipsActivity extends ActionBarActivity {
    ListView ListView_before_donation,ListView_during_donation,ListView_after_donation;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);
        ListView_before_donation =(ListView)findViewById(R.id.health_tips_lview);
        ListView_during_donation=(ListView)findViewById(R.id.health_tips_lview1);
        ListView_after_donation=(ListView)findViewById(R.id.health_tips_lview2);
        final String[] before_donation=new String[]{
        "Maintain a healthy iron level in your diet by eating iron rich foods, such as red meat, " +
                "fish, poultry, beans, spinach, iron-fortified cereals and raisins.",
         "Get a good night's sleep.",
         "Drink an extra 16 oz. of water or nonalcoholic fluids before the donation.",
         "Eat a healthy meal before your donation. Avoid fatty foods, such as hamburgers," +
         " fries or ice cream before donating.",
        "If you are a platelet donor, remember that your system must be free of aspirin for " +
                "two days prior to donation."
        };

        final String[] during_donation=new String[]{
        "Wear clothing with sleeves that can be raised above the elbow.",
         "Let the person taking your blood know if you have a preferred arm and show " +
                 "them any good veins that have been used successfully in the past to draw blood.",
        "Relax, listen to music, talk to other donors or read during the donation process.",
         "Take the time to enjoy a snack and a drink in the refreshments area immediately" +
                 " after donating."
        };

        final String[] after_donation=new String[]{
        "Drink an extra four (8 ounce) glasses of liquids and avoid alcohol over the next 24 hours.",
         "Remove the wrap bandage  within the next hour.",
        "Keep the strip bandage on for the next several hours.",
        "To avoid a skin rash, clean the area around the strip bandage with soap and water.",
        "Do not do any heavy lifting or vigorous exercise for the rest of the day.",
         "If the needle site starts to bleed, apply pressure to it and raise your arm straight up" +
                 " for about 5-10 minutes or until bleeding stops.",
         "If you experience dizziness or lightheadedness after donation, stop what you are doing " +
                 "and sit down or lie down until you feel better. Avoid performing any activity " +
                 "where fainting may lead to injury for at least 24 hours."
        };
        Integer image_id=0;
        customlistadapter adapter=new customlistadapter(this,image_id,before_donation);
        ListView_before_donation.setAdapter(adapter);
        customlistadapter adapter1=new customlistadapter(this,image_id,during_donation);
        ListView_during_donation.setAdapter(adapter1);
        customlistadapter adapter2=new customlistadapter(this,image_id,after_donation);
        ListView_after_donation.setAdapter(adapter2);
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
    public void onBackPressed() {

        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i1 = new Intent(HealthTipsActivity.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(HealthTipsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(HealthTipsActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

}

package com.nyu.blife_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FAQsScreen extends ActionBarActivity {

    ELA_FAQ listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs_screen);
        //for setting the logo as home button

        expListView=(ExpandableListView) findViewById(R.id.expandableListView);
        prepareListData();
        //listAdapter=new ExpandableListView_FAQ(this,listDataHeader,listDataChild);
        listAdapter=new ELA_FAQ(this,listDataHeader,listDataChild);
        expListView.setAdapter(listAdapter);
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

    private void prepareListData(){

        listDataHeader=new ArrayList<String>();
        listDataChild=new HashMap<String,List<String>>();
        listDataHeader.add("What is B-life??");
        List<String> wib=new ArrayList<String>();
        wib.add("B-life is an app connecting donors and receivers and provides a communication platform and reduces the " +
                "gap of communication between them");


        listDataHeader.add("Who can donate blood?" );
        List<String> wib9=new ArrayList<String>();
        wib9.add("In most states, donors must be age 17 or older. Some states allow donation by " +
                "16-year-olds with a signed parental consent form. Donors must weigh at least 110 " +
                "pounds and be in good health");

        listDataHeader.add("Is it safe to give blood?");
        List<String> wib10=new ArrayList<>();
        wib10.add("Donating blood is a safe process. Each donor’s blood is collected through a new," +
                " sterile needle that is used once and then discarded.");

        listDataHeader.add("How many gallons of blood I can donate?? ");
        List<String> wib1=new ArrayList<String>();
        wib1.add("46.5 gallons amount of blood you could donate if you begin at age 17 " +
                "and donate every 56 days until you reach 79 years old");

        listDataHeader.add("How much time does it take to donate blood once??");
        List<String> wib2=new ArrayList<String>();
        wib2.add("The actual donation of" +
                " a pint of whole blood unit takes eight to 10 minutes. However, the time varies " +
                "slightly with each person depending on several factors " +
                ". It’s about an hour of your " +
                "time.");

        listDataHeader.add("Types of red blood cells??");
        List<String> wib3=new ArrayList<String>();
        wib3.add("Four main red blood cell types: A, B, AB and O. Each can be positive or negative" +
                " for the Rh factor. AB is the universal recipient, O negative is the universal " +
                "donor of red blood cells.");

        listDataHeader.add("How often can I donate blood?");
        List<String> wib4=new ArrayList<String>();
        wib4.add("You must wait at least eight weeks (56 days) between donations of whole blood " +
                "and 16 weeks (112 days) between double red cell donations.");


        listDataHeader.add("Can I get HIV by donating blood?");
        List<String> wib5=new ArrayList<String>();
        wib5.add("No. Sterile procedures and disposable equipment are used during the process");

        listDataHeader.add("How often can I donate platelets?");
        List<String> wib6=new ArrayList<>();
        wib6.add("Every 7 days up to 24 apheresis donations can be made in a year. " +
                "Some apheresis donations can generate two or three adult-sized platelet " +
                "transfusion doses from one donation.");

        listDataHeader.add("Will it hurt when you insert the needle?");
        List<String> wib7=new ArrayList<String>();
        wib7.add("Only for a moment. Pinch the fleshy, soft underside of your arm."+
                        "That pinch is similar to what you will feel when the needle is inserted.");

        listDataHeader.add("How long will it take to replenish the pint of blood I donate?");
        List<String> wib8=new ArrayList<String>();
        wib8.add("The plasma from your donation is replaced within about 24 hours. Red cells need"+
               " about four to six weeks for complete replacement. That’s why at least eight weeks"+
                        " are required between whole blood donations.");

        listDataHeader.add("What happens if I donate blood and realize afterward that I shouldn’t" +
                " have because I may have been exposed to HIV or another disease?");
        List<String> wib11=new ArrayList<>();
        wib11.add("If you give blood but decide later that your blood may not be safe to transfuse," +
                " you should call the hospital emergency telephone number you were given at" +
                " the time of your donation. This call should be made as" +
                " soon as possible.");

        listDataHeader.add("Can I donate blood to myself");
        List<String> wib12=new ArrayList<>();
        wib12.add("An autologous donation is when you donate blood for yourself before having" +
                " surgery or a planned medical procedure. Autologous donations require a physician" +
                " prescription. Contact your doctor first to find out if you should donate blood" +
                " for yourself.");

        listDataHeader.add("Are blood substitutes available?");
        List<String> wib13=new ArrayList<>();
        wib13.add("No, there are currently no substitutes for blood. However, research is" +
                " continually being done to identify new alternatives to blood transfusion." +
                " The Red Cross actively follows blood substitute research and works closely " +
                "with other organizations that develop new transfusion alternatives.");

        listDataChild.put(listDataHeader.get(0), wib);
        listDataChild.put(listDataHeader.get(1),wib1);
        listDataChild.put(listDataHeader.get(2),wib2);
        listDataChild.put(listDataHeader.get(3),wib3);
        listDataChild.put(listDataHeader.get(4),wib4);
        listDataChild.put(listDataHeader.get(5),wib5);
        listDataChild.put(listDataHeader.get(6),wib6);
        listDataChild.put(listDataHeader.get(7),wib7);
        listDataChild.put(listDataHeader.get(8),wib8);
        listDataChild.put(listDataHeader.get(9),wib9);
        listDataChild.put(listDataHeader.get(10),wib10);
        listDataChild.put(listDataHeader.get(11),wib11);
        listDataChild.put(listDataHeader.get(12),wib12);
        listDataChild.put(listDataHeader.get(13),wib13);

    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i1 = new Intent(FAQsScreen.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(FAQsScreen.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(FAQsScreen.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }


}

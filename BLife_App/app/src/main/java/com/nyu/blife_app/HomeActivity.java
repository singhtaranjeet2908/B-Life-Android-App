package com.nyu.blife_app;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class HomeActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<Integer> listDataHeaderImages;
    GPStracker home_gpstracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        expListView=(ExpandableListView) findViewById(R.id.expandableListView2);
        prepareListData();
        listAdapter=new ExpandableListAdapter(this,listDataHeader,listDataChild, listDataHeaderImages);
        expListView.setAdapter(listAdapter);
        home_gpstracker=new GPStracker(HomeActivity.this);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));

               //highlight the selected list group using selector from list_group_highlighter.xml
                parent.setItemChecked(index, true);

                if(listDataHeader.get(groupPosition).equals("SEARCH BLOOD DONORS") && isInternetOn()){
                    Intent searchDonorIntent = new Intent(getApplicationContext(), SearchDonorsActivity.class);
                    startActivity(searchDonorIntent);
                }

                if(listDataHeader.get(groupPosition).equals("HELPLINE NUMBERS")){
                    Intent helplineIntent = new Intent(getApplicationContext(), HelplineNumbersScreen.class);
                    startActivity(helplineIntent);
                }

                if(listDataHeader.get(groupPosition).equals("TIPS FOR DONORS")){
                    Intent tipsIntent = new Intent(getApplicationContext(), HealthTipsActivity.class);
                    startActivity(tipsIntent);
                }


                if(listDataHeader.get(groupPosition).equals("POST-DONATION SNACKING !") && isInternetOn() && check_gps()){
                    Intent snackIntent = new Intent(getApplicationContext(), Search_Hotel.class);
                    startActivity(snackIntent);
                }

                if(listDataHeader.get(groupPosition).equals("FAQs")){
                    Intent faqIntent = new Intent(getApplicationContext(), FAQsScreen.class);
                    startActivity(faqIntent);
                }

                if(listDataHeader.get(groupPosition).equals("ABOUT B-LIFE")){
                    Intent aboutIntent = new Intent(getApplicationContext(), AboutBLifeActivity.class);
                    startActivity(aboutIntent);
                }
                return false;
            }
        });

        // ListView Child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView
                        .getPackedPositionForChild(groupPosition, childPosition));
                //highlight the selected list child using selector from list_item_highlighter.xml
                parent.setItemChecked(index, true);

                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("SEND A BLOOD REQUEST") && isInternetOn()){
                    Intent sendBRIntent = new Intent(getApplicationContext(), SendBloodRequestActivity.class);
                    startActivity(sendBRIntent);
                }

                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("VIEW BLOOD REQUESTS") && isInternetOn()){
                    Intent viewBRIntent = new Intent(getApplicationContext(), ViewBloodRequestActivity.class);
                    startActivity(viewBRIntent);
                }

                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("MANAGE BLOOD REQUESTS") && isInternetOn()){
                    Intent manageReqIntent = new Intent(getApplicationContext(), ManageRequestsScreen.class);
                    startActivity(manageReqIntent);
                }

                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("SEARCH NEARBY HOSPITALS")){
                    if(isInternetOn()) {
                           if(check_gps()) {
                               Intent searchHospitalIntent = new Intent(getApplicationContext(),
                                       SearchHospitalsScreen.class);
                               startActivity(searchHospitalIntent);
                           }
                    }
                }

                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("SEARCH NEARBY BLOOD BANKS")){
                    if(isInternetOn()) {
                        if(check_gps()) {
                            Intent searchBanksIntent = new Intent(getApplicationContext(), SearchBloodBankActivity.class);
                            startActivity(searchBanksIntent);
                        }
                    }
                }
                return false;
            }
        });
    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("NO INTERNET CONNECTION!")
                        .setContentText("Please enable your WIFI or Mobile Data connection.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

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
                Intent i1 = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(HomeActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private boolean check_gps(){
           boolean k=true;

            if (home_gpstracker.canGetLocation()) {
                k= true;
            }
            else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                home_gpstracker.showSettingsAlert();
                k=false;
            }
            return k;
    }

    @Override
    public void onRestart()
    {
    super.onRestart();
    HomeActivity.this.recreate();
    }
    //prepare data for lists of group and child titles as well as lists of group and child images
    private void prepareListData(){

        //for titles
        listDataHeader=new ArrayList<String>();
        listDataChild=new HashMap<String,List<String>>();
        listDataHeader.add("SEARCH BLOOD DONORS");
        List<String> home_list = new ArrayList<String>();


        listDataHeader.add("BLOOD REQUEST");
        List<String> home_list1=new ArrayList<String>();
        home_list1.add("SEND A BLOOD REQUEST");
        home_list1.add("VIEW BLOOD REQUESTS");
        home_list1.add("MANAGE BLOOD REQUESTS");

        listDataHeader.add("SEARCH DONATION CAMPS");
        List<String> home_list2 =new ArrayList<String>();
        home_list2.add("SEARCH NEARBY HOSPITALS");
        home_list2.add("SEARCH NEARBY BLOOD BANKS");

        listDataHeader.add("HELPLINE NUMBERS");
        List<String> home_list3 =new ArrayList<String>();

        listDataHeader.add("TIPS FOR DONORS");
        List<String> home_list4 =new ArrayList<String>();

        listDataHeader.add("POST-DONATION SNACKING !");
        List<String> home_list5 =new ArrayList<String>();

        listDataHeader.add("FAQs");
        List<String> home_list6 = new ArrayList<String>();

        listDataHeader.add("ABOUT B-LIFE");
        List<String> home_list7 =new ArrayList<String>();


        //arrayList for images/icons for expandable list view "groups"
        listDataHeaderImages = new ArrayList<Integer>();

        listDataHeaderImages.add(R.drawable.globetransparent);
        listDataHeaderImages.add(R.drawable.usertransparent);
        listDataHeaderImages.add(R.drawable.plus);
        listDataHeaderImages.add(R.drawable.phone1);
        listDataHeaderImages.add(R.drawable.tips);
        listDataHeaderImages.add(R.drawable.orange_juice);
        listDataHeaderImages.add(R.drawable.faq);
        listDataHeaderImages.add(R.drawable.rightgray);

        listDataChild.put(listDataHeader.get(0), home_list);
        listDataChild.put(listDataHeader.get(1), home_list1);
        listDataChild.put(listDataHeader.get(2), home_list2);
        listDataChild.put(listDataHeader.get(3), home_list3);
        listDataChild.put(listDataHeader.get(4), home_list4);
        listDataChild.put(listDataHeader.get(5), home_list5);
        listDataChild.put(listDataHeader.get(6), home_list6);
        listDataChild.put(listDataHeader.get(7), home_list7);
    }
}

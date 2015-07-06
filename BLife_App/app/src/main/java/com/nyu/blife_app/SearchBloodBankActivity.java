package com.nyu.blife_app;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchBloodBankActivity extends ActionBarActivity {

    private ListView shs_lv;
    private String provider;

    private double lat,lng;
    private GPStracker gps;


            class Bloodbank {
                final String name;
                final String url;
                //List<String> data_location=new ArrayList<String>();
                final String data_location;
                final String in_num;
                public Bloodbank(String name, String url, String data_location,String num) {
                    this.name = name;
                    this.url = url;
                    this.data_location=data_location;
                    this.in_num=num;
                }

                @Override
                public String toString() {
                    return name;
                }


                public String getLocation(){
                    return data_location;
                }
                public String get_number(){
                    return in_num;
                }
                public String get_url(){
                    return url;
                }

            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blood_bank_screen);
        shs_lv=(ListView)findViewById(R.id.listview_sbb);

        gps=new GPStracker(SearchBloodBankActivity.this);
                    if(gps.canGetLocation()){

                        lat = gps.getLatitude();
                        lng = gps.getLongitude();

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lng,
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();

                    }

                    AsyncTask<Void, Void, List<Bloodbank>> hospitals = new AsyncTask<Void, Void, List<Bloodbank>>() {
                        @Override
                        protected List<Bloodbank> doInBackground(Void... params) {
                            Yelp get_blood_bank = Yelp.getYelp(SearchBloodBankActivity.this);
                            //String hospitals = get_hospitals.search("hospitals", 34.0204989,-118.4117325);
                            Log.v("Location details", String.valueOf(lat) + " " + String.valueOf(lng));
                            String blood_bank = get_blood_bank.search("blood banks", lat, lng);
                            try {
                                return processJson(blood_bank);
                            } catch (JSONException e) {
                                Log.v("Full Errors", "Over here");
                                return Collections.<Bloodbank>emptyList();
                                //return hospitals;
                            }


                        }

                        @Override
                        protected void onPostExecute(List<Bloodbank> result) {
    //                mSearchResultsText.setText(result);


                            ArrayAdapter<Bloodbank> new_adapter = new ArrayAdapter<Bloodbank>(SearchBloodBankActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    result);

                            shs_lv.setAdapter(new_adapter);
                            shs_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                    Bloodbank biz = (Bloodbank) shs_lv.getItemAtPosition(position);
                                    Log.v("List View", biz.toString());
                                    Toast.makeText(getApplicationContext(), biz.toString(), Toast.LENGTH_LONG).show();

                                    String re1=".*?";	// Non-greedy match on filler
                                    //String re2="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 1
                                    String re2="([+-]?\\d*\\.\\d{2,})(?![-+0-9\\.])";
                                    String re3=".*?";	// Non-greedy match on filler
                                    String re4="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";
//                        String re5=".*?";
//                        String re6="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";// Float 2
                                    String float1="";
                                    String float2="";
                                    Pattern p = Pattern.compile(re1+re2+re3+re4,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                                    Matcher m = p.matcher(biz.getLocation());
                                    if (m.find())
                                    {
                                        float1=m.group(1);
                                        float2=m.group(2);
                                        Log.v("Code", float1 + " " + float2);
                                        // System.out.print("("+float1.toString()+")"+"("+float2.toString()+")"+"\n");
                                    }
                                    String display_address="";
                                    try {
                                        JSONObject get_address=new JSONObject(biz.getLocation());
                                        display_address=get_address.optString("display_address");
//                                display_address=display_address.replaceAll("[//[//]//\"]","");
                                        display_address=display_address.replaceAll("[\\[|\\]|//\"]","");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    String uri = String.format(Locale.ENGLISH,
                                            "http://maps.google.com/maps?daddr=%f,%f",
                                            Float.valueOf(float1), Float.valueOf(float2));
//                            Intent intent44 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                            intent44.setClassName("com.google.android.apps.maps",
//                                    "com.google.android.maps.MapsActivity");
//                            startActivity(intent44);
                                    Intent to_selected=new Intent(getApplicationContext(),SelectedHospitalDetails.class);
                                    to_selected.putExtra("lat",float1);
                                    to_selected.putExtra("long",float2);
                                    to_selected.putExtra("address",display_address);
                                    to_selected.putExtra("phone",biz.get_number());
                                    to_selected.putExtra("url",biz.get_url());
                                    to_selected.putExtra("name",biz.toString());
                                    startActivity(to_selected);

                                }
                            });

                            //setProgressBarIndeterminateVisibility(false);
                        }
                    }.execute();
                }


//   //@Override
//     void onListItemClick(ListView shs_lv, View view, int position, long id) {
//        Business biz = (Business)shs_lv.getItemAtPosition(position);
//        Log.v("List View",biz.toString());
//        Toast.makeText(getApplicationContext(),biz.toString(),Toast.LENGTH_LONG).show();
//       // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(biz.url)));
//    };

            List<Bloodbank> processJson (String jsonStuff)throws JSONException {
                JSONObject json = new JSONObject(jsonStuff);
                JSONArray blood_bank_json = json.getJSONArray("businesses");
                ArrayList<Bloodbank> BloodBankNames = new ArrayList<Bloodbank>(blood_bank_json.length());
                List<String> json_location=new ArrayList<String>();
                for (int i = 0; i < blood_bank_json.length(); i++) {

                    JSONObject hospital_returned = blood_bank_json.getJSONObject(i);
                    BloodBankNames.add(new Bloodbank(hospital_returned.optString("name"),
                            hospital_returned.optString("mobile_url"),
                            hospital_returned.optString("location"),
                            hospital_returned.optString("phone")));
                    Log.v("Inside Json232",hospital_returned.optString("location"));


                }
                Integer image_id=R.drawable.icon_caller;
                return BloodBankNames;

                //return TextUtils.join("\n", hospitalNames);
            }



    @Override
    public void onBackPressed() {
        //Intent back_Intent = new Intent(SearchBloodBankActivity.this, HomeActivity.class);
        //startActivity(back_Intent);
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
                Intent i1 = new Intent(SearchBloodBankActivity.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(SearchBloodBankActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(SearchBloodBankActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    public void onLocationChanged(Location location) {
        lat = (float) (location.getLatitude());
        lng = (float) (location.getLongitude());
        Log.v("My Location",String.valueOf(lat)+" "+String.valueOf(lng));
            }
        }







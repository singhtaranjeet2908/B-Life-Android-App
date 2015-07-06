package com.nyu.blife_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ViewBloodRequestActivity extends ActionBarActivity {


    Spinner searchCity, searchBG;
    Button searchreq;
    FloatingActionButton fab;
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    String vbr_searchbg , vbr_searchcity;
    String bgroupvalidate_input, cityvalidate_input;
    String group_bool, City_bool;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_request);

        searchCity = (Spinner) findViewById(R.id.searchCity);
        searchBG = (Spinner) findViewById(R.id.searchBG);
        searchBG.setFocusable(true);
        searchreq = (Button) findViewById(R.id.btnSearchReq);

        listview = (ListView) findViewById(R.id.listview_view_BR);

        vbr_searchcity = searchCity.getSelectedItem().toString();
        vbr_searchbg = searchBG.getSelectedItem().toString();



        adapter = new ArrayAdapter<String>(ViewBloodRequestActivity.this,R.layout.listview_item);



        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BloodRequest");


        try {
            ob = query.find();
            if(ob == null)
            {
                Toast.makeText(ViewBloodRequestActivity.this, "No Result available based on search parameters.", Toast.LENGTH_SHORT).show();
            }
            else {
                for (ParseObject BloodRequest : ob) {
                    String onlyDate = BloodRequest.get("requiredBefore").toString();
                    String[] dateForSplit = onlyDate.split(" ");
                    String mm = dateForSplit[1];
                    String dd = dateForSplit[2];
                    String yy = dateForSplit[5];
                    String entireDate = dd + " " + mm + " " + yy;
                    adapter.add(BloodRequest.get("bloodGroup") + " Blood Group Required before " + entireDate + " in " + BloodRequest.get("city"));

                }

                int count = adapter.getCount();
                if(count == 0)
                {
                    Toast.makeText(ViewBloodRequestActivity.this, " 0 results found based on search parameters.",Toast.LENGTH_SHORT).show();
                }
                else if(count == 1)
                {
                    Toast.makeText(ViewBloodRequestActivity.this, count + " result found based on search parameters.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ViewBloodRequestActivity.this, count + " results found based on search parameters.",Toast.LENGTH_SHORT).show();
                }

                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ViewBloodRequestActivity.this, SingleItemView.class);
                        i.putExtra("name", ob.get(position).getString("requestorName"));
                        i.putExtra("city", ob.get(position).getString("city"));
                        i.putExtra("location", ob.get(position).getString("location"));
                        i.putExtra("cellNumber", ob.get(position).getString("cellNumber"));
                        i.putExtra("requiredBefore", ob.get(position).getDate("requiredBefore").toString());
                        i.putExtra("blood", ob.get(position).getString("bloodGroup"));
                        startActivity(i);
                    }
                });
            }
        } catch (com.parse.ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        searchreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vbr_searchcity = searchCity.getSelectedItem().toString();
                vbr_searchbg = searchBG.getSelectedItem().toString();


                if (vbr_searchcity.equals("CITY") && vbr_searchbg.equals("BLOOD GROUP")) {
                    Toast.makeText(getBaseContext(), "No field selected. General Results found.", Toast.LENGTH_SHORT).show();
                } else if (!vbr_searchbg.equals("BLOOD GROUP") && vbr_searchcity.equals("CITY")) {
                    Toast.makeText(getBaseContext(), "Results sorted by - " + vbr_searchbg, Toast.LENGTH_SHORT).show();
                    new RemoteDataTask().execute();
                } else if (vbr_searchbg.equals("BLOOD GROUP") && !vbr_searchcity.equals("CITY")) {
                    Toast.makeText(getBaseContext(), "Results sorted by - " + vbr_searchcity, Toast.LENGTH_SHORT).show();
                    new RemoteDataTask().execute();
                } else {
                    Toast.makeText(getBaseContext(), "Results sorted by - " + vbr_searchbg + " and " + vbr_searchcity, Toast.LENGTH_SHORT).show();
                    new RemoteDataTask().execute();
                }
            }
        });
    }




    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewBloodRequestActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Fetching Result based on Search Parameters..");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String request_blood_group = searchBG.getSelectedItem().toString();
            String request_city = searchCity.getSelectedItem().toString();
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("BloodRequest");

            if (request_blood_group.equals("BLOOD GROUP") && request_city.equals("CITY"))
            {
                try {
                    ob = query.find();
                } catch (com.parse.ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            else if(!request_blood_group.equals("BLOOD GROUP") && request_city.equals("CITY"))
            {
                query.whereEqualTo("bloodGroup", request_blood_group);
                //query.whereEqualTo("city", request_city);
                try {
                    ob = query.find();
                } catch (com.parse.ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            else if (request_blood_group.equals("BLOOD GROUP") && !request_city.equals("CITY"))
            {
                //query.whereEqualTo("bloodGroup", request_blood_group);
                query.whereEqualTo("city", request_city);
                try {
                    ob = query.find();
                } catch (com.parse.ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            else
            {
                query.whereEqualTo("bloodGroup", request_blood_group);
                query.whereEqualTo("city", request_city);
                try {
                    ob = query.find();
                } catch (com.parse.ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
          return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (ob == null) {
                mProgressDialog.dismiss();
                Toast.makeText(ViewBloodRequestActivity.this, "No Result available based on search parameters.", Toast.LENGTH_SHORT).show();
            }

            else {
                listview = (ListView) findViewById(R.id.listview_view_BR);

                adapter = new ArrayAdapter<String>(ViewBloodRequestActivity.this, R.layout.listview_item, R.id.text);

                for (ParseObject BloodRequest : ob) {
                    String onlyDate = BloodRequest.get("requiredBefore").toString();
                    String[] dateForSplit = onlyDate.split(" ");
                    String mm = dateForSplit[1];
                    String dd = dateForSplit[2];
                    String yy = dateForSplit[5];
                    String entireDate = dd + " " + mm + " " + yy;
                    adapter.add(BloodRequest.get("bloodGroup") + " Blood Group Required before " + entireDate + " in " + BloodRequest.get("city"));
                }

                int count = adapter.getCount();
                if(count == 0)
                {
                    Toast.makeText(ViewBloodRequestActivity.this, " 0 results found based on search parameters.",Toast.LENGTH_SHORT).show();
                }
                else if(count == 1)
                {
                    Toast.makeText(ViewBloodRequestActivity.this, count + " result found based on search parameters.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ViewBloodRequestActivity.this, count + " results found based on search parameters.",Toast.LENGTH_SHORT).show();
                }

                listview.setAdapter(adapter);
                mProgressDialog.dismiss();
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(ViewBloodRequestActivity.this, SingleItemView.class);
                        i.putExtra("name", ob.get(position).getString("requestorName"));
                        i.putExtra("city", ob.get(position).getString("city"));
                        i.putExtra("location", ob.get(position).getString("location"));
                        i.putExtra("cellNumber", ob.get(position).getString("cellNumber"));
                        i.putExtra("requiredBefore", ob.get(position).getDate("requiredBefore").toString());
                        i.putExtra("blood", ob.get(position).getString("bloodGroup"));

                        startActivity(i);
                    }
                });
            }
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
    public void onBackPressed() {
        //Intent back_Intent = new Intent(ViewBloodRequestActivity.this, HomeActivity.class);
        //startActivity(back_Intent);
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
                Intent i1 = new Intent(ViewBloodRequestActivity.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(ViewBloodRequestActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(ViewBloodRequestActivity.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

}

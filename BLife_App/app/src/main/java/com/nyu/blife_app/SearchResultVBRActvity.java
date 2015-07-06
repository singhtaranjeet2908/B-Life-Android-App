package com.nyu.blife_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class SearchResultVBRActvity extends ActionBarActivity {

    private RecyclerView card_ViewBR;
    private card_viewBR_adapter card_viewBRAdapter;
    FloatingActionButton fab;
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result_vbr_actvity);
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
        Intent intent = getIntent();
        String request_city = intent.getStringExtra("city");
        String request_blood_group = intent.getStringExtra("blood");
        // fab = (FloatingActionButton) findViewById(R.id.fab1);
//        card_ViewBR = (RecyclerView)findViewById(R.id.recyclerViewBR);
//        fab.attachToRecyclerView(card_ViewBR);
//        card_ViewBR.setHasFixedSize(true);
//        LinearLayoutManager viewBRlayoutManager = new LinearLayoutManager(getApplication());
//        card_ViewBR.setLayoutManager(viewBRlayoutManager);

//        FetchDonorListData itemsData[] = { new FetchDonorListData("Phone",R.mipmap.phone),
//                new FetchDonorListData("Phone",R.mipmap.phone1),
//                new FetchDonorListData("Phone",R.mipmap.plus),
//                new FetchDonorListData("Phone",R.mipmap.right)};
//
//        card_viewBRAdapter = new card_viewBR_adapter(this, itemsData);
//        card_ViewBR.setAdapter(card_viewBRAdapter);
//        card_ViewBR.setItemAnimator(new DefaultItemAnimator());

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent fab_intent = new Intent(SearchResultVBRActvity.this, SendBloodRequestActivity.class);
//                startActivity(fab_intent);
//                finish();
//            }
//        });



    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SearchResultVBRActvity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "Country" in Parse.com
            Intent i2 = getIntent();
            String request_blood_group = i2.getStringExtra("blood");
            String request_city = i2.getStringExtra("city");
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "BloodRequest");
            query.whereEqualTo("bloodGroup", request_blood_group);
            query.whereEqualTo("city", request_city);
            //query.orderByDescending("_created_at");
            try {
                ob = query.find();
            } catch (com.parse.ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(SearchResultVBRActvity.this,
                    R.layout.listview_item);
            // Retrieve object "name" from Parse.com database
            for (ParseObject BloodRequest : ob) {
                adapter.add("Required"+" "+ BloodRequest.get("bloodGroup")+" before"+ BloodRequest.get("requiredBefore")+" at "+ BloodRequest.get("city")+ " by "+ BloodRequest.get("requestorName"));

            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i = new Intent(SearchResultVBRActvity.this,
                            SingleItemView.class);
                    // Pass data "name" followed by the position
                    i.putExtra("name", ob.get(position).getString("requestorName")
                            .toString());
                    i.putExtra("city", ob.get(position).getString("city")
                            .toString());
                    i.putExtra("location", ob.get(position).getString("location")
                            .toString());
                    i.putExtra("cellNumber", ob.get(position).getString("cellNumber")
                            .toString());
                    i.putExtra("blood", ob.get(position).getString("bloodGroup")
                            .toString());
                    // Open SingleItemView.java Activity
                    startActivity(i);
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        Intent back_Intent = new Intent(SearchResultVBRActvity.this, ViewBloodRequestActivity.class);
        startActivity(back_Intent);
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
                Intent i1 = new Intent(this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "you selected settings", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(SearchResultVBRActvity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"you selected sign up button", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}

package com.nyu.blife_app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectedHospitalDetails extends ActionBarActivity {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_hospital_details);
        Intent full=getIntent();
        String loc_lat=full.getStringExtra("lat");
        String loc_long=full.getStringExtra("long");
        String loc_address=full.getStringExtra("address");
        final String loc_phone=full.getStringExtra("phone");
        String loc_name=full.getStringExtra("name");
        String loc_url=full.getStringExtra("url");
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        GPStracker selcted_my=new GPStracker(this);
        LatLng location_cord=new LatLng(Double.parseDouble(loc_lat),Double.parseDouble(loc_long));
        TextView set_address=(TextView)findViewById(R.id.address);
        TextView set_phone=(TextView)findViewById(R.id.number_call);
        LatLng my_latlng=new LatLng(selcted_my.getLatitude(),selcted_my.getLongitude());


        if(googleMap!=null){
            Marker location=googleMap.addMarker(new MarkerOptions().position(location_cord).title(loc_name));
            Marker my_location=googleMap.addMarker(new MarkerOptions().position(my_latlng).title("My Location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            set_address.setText(loc_address);
            set_phone.setText("Phone No: +1 "+loc_phone);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location_cord, 15));


        }

        set_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + loc_phone));
                startActivity(intent);
            }
        });
//        try {
//            // Loading map
//            initilizeMap();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

//    /**
//     * function to load map. If map is not created it will create it for you
//     * */
//    private void initilizeMap() {
//        if (googleMap == null) {
//            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//                    R.id.map)).getMap();
//
//            // check if map is created successfully or not
//            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initilizeMap();
//    }


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
                Intent i1 = new Intent(SelectedHospitalDetails.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(SelectedHospitalDetails.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(SelectedHospitalDetails.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

}

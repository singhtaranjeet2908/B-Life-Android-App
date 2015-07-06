package com.nyu.blife_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;


public class HelplineNumbersScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline_numbers_screen);

        ListView helpline_lview=(ListView)findViewById(R.id.listView);
        final String[] helpline_name = new String[] {
                "US Suicide ",
                "NDMDA Depression Hotline ",
                "Suicide Prevention Services Crisis ",
                "Suicide Prevention Services Depression",
                "AAA Crisis Pregnancy Center",
                 "Child Abuse",
                "Crisis Help Line",
                "Domestic & Teen Dating Violence",
                "Parental Stress",
                "Runaway Hotline",
                "Sexual Assault Hotline ",
                "Suicide & Depression Hotline",
                "National Child Abuse Hotline	",
                "National Domestic Violence Hotline",
                "National Domestic Violence (TDD)",
                "National Youth Crisis Hotline"

        };
        final String[] number_helpline=new String[]{
                "1-800-784-2433",
                "800-826-3632",
                "800-784-2433",
                "630-482-9696",
                "800-560-0717",
                "800-792-5200",
                "800-233-4357",
                "800-992-2600",
                "800-632-8188",
                "800-231-6946",
                "800-223-5001",
                "800-999-9999",
                "800-422-4453",
                "800-799-7223",
                "800-787-3224",
                "800-448-4663"
                    };
        Integer image_id=R.drawable.icon_caller;
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, helpline_name);
        customlistadapter adapter=new customlistadapter(this,image_id,helpline_name);
        helpline_lview.setAdapter(adapter);
        helpline_lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = helpline_name[position];
                String itemNumber=number_helpline[position];

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Calling "+ itemValue, Toast.LENGTH_LONG)
                        .show();
                Uri call_number=Uri.parse("tel:" + itemNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL,call_number);
                startActivity(intent);

            }

        });
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
                Intent i1 = new Intent(HelplineNumbersScreen.this, SettingsActivity.class);
                startActivity(i1);
                Toast.makeText(getBaseContext(), "Opening Settings...", Toast.LENGTH_LONG).show();
                break;

            case R.id.log_out:
                ParseUser.logOut();

                Intent intent = new Intent(HelplineNumbersScreen.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getBaseContext(),"Logging Out...", Toast.LENGTH_LONG).show();
                break;

            case R.id.signUpButton:
                Intent i2 = new Intent(HelplineNumbersScreen.this, DonorRegistrationActivity.class);
                startActivity(i2);
                Toast.makeText(getBaseContext(),"Opening Sign Up Form...", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

}

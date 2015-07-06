package com.nyu.blife_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;


public class EditProfileActivity extends Fragment{

    EditText username,firstname,lastname,mobile,city,zipcode,dob,bloodgroup,weight;
    TextView weight_listing, bloodgroup_zipcode,dob_textview;
    String typeOfUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater full=getActivity().getLayoutInflater();
        View rootView = full.inflate(R.layout.activity_edit_profile, container, false);




        username=(EditText)rootView.findViewById(R.id.editText1);
        firstname=(EditText)rootView.findViewById(R.id.editText2);
        lastname=(EditText)rootView.findViewById(R.id.editText3);
        mobile=(EditText)rootView.findViewById(R.id.editText4);
        city=(EditText)rootView.findViewById(R.id.editText);
        zipcode=(EditText)rootView.findViewById(R.id.editText6);
        dob=(EditText)rootView.findViewById(R.id.editText8);
        bloodgroup=(EditText)rootView.findViewById(R.id.editText9);
        weight=(EditText)rootView.findViewById(R.id.enter_weight);
        final ParseUser current_user = ParseUser.getCurrentUser();
        current_user.fetchInBackground();

        typeOfUser =  current_user.getString("userType");
        weight_listing=(TextView)rootView.findViewById(R.id.textView_weight);
        bloodgroup_zipcode=(TextView)rootView.findViewById(R.id.textView9);
        dob_textview=(TextView)rootView.findViewById(R.id.textView8);
        weight_listing.setText("");
        bloodgroup_zipcode.setText("");
        dob_textview.setText("");
        weight.setVisibility(rootView.INVISIBLE);
        bloodgroup.setVisibility(rootView.INVISIBLE);
        dob.setVisibility(rootView.INVISIBLE);


        if(typeOfUser.contentEquals("Donor")){
            weight.setVisibility(rootView.VISIBLE);
            bloodgroup.setVisibility(rootView.VISIBLE);
            dob.setVisibility(rootView.VISIBLE);

            weight.setText(current_user.getString("weight"));
            bloodgroup.setText(current_user.getString("bloodGroup"));

            dob.setText(current_user.getString("dob"));

            weight_listing.setText("Weight:");
            bloodgroup_zipcode.setText("Blood Group:");
            dob_textview.setText("DOB:");


        }
        username.setText(current_user.getString("username"));
        firstname.setText(current_user.getString("firstName"));
        lastname.setText(current_user.getString("lastName"));
        mobile.setText(String.valueOf(current_user.getNumber("phoneNumber")));
        city.setText(current_user.getString("city"));
        zipcode.setText(String.valueOf(current_user.getNumber("zipCode")));



        Button update_details=(Button)rootView.findViewById(R.id.button_update);
        update_details.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if(validatePhone(mobile.getText().toString()) && validateZip(zipcode.getText().toString())) {
                    if (typeOfUser.contentEquals("Donor")) {
                        current_user.put("weight", weight.getText().toString());
                        current_user.put("bloodGroup", bloodgroup.getText().toString());
                        current_user.put("dob", dob.getText().toString());


                    }
                    current_user.put("username", username.getText().toString());
                    current_user.put("firstName", firstname.getText().toString());
                    current_user.put("lastName", lastname.getText().toString());
                    current_user.put("phoneNumber", Integer.parseInt(mobile.getText().toString()));
                    current_user.put("city", city.getText().toString());
                    current_user.put("zipCode", Integer.parseInt(zipcode.getText().toString()));


                    current_user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //save was successful
                                Log.v("Edit Page", " Editing done successfully");
                                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                getActivity().finish();

                            } else {
                                Toast.makeText(getActivity(), "Update Failed", Toast.LENGTH_SHORT).show();

                                Log.v("Edit Page", " Editing failed");
                            }
                        }
                    });


                }
            }
        });





        return rootView;





    }

    private Boolean validatePhone(String get_phone) {


        if (get_phone.equals("") || !get_phone.matches("[0-9]{10}")) {
        Toast.makeText(getActivity(),"Enter Phone number properly",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }



    }
    private boolean validateZip(String get_zip) {

        if (get_zip.equals("") || !get_zip.matches("\\d{5}")) {
        Toast.makeText(getActivity()," Enter Zipcode Properly",Toast.LENGTH_SHORT).show();
        return false;
        }
        else{
        return true;
        }

    }




}
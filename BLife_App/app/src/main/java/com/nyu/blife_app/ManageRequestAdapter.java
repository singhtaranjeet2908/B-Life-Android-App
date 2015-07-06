package com.nyu.blife_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * Created by Yeshwant on 10/05/2015.
 */

public class ManageRequestAdapter extends ParseQueryAdapter<ParseObject>{
    static String request_status, blood_request, requestor_name, location, date, phone, requestor_city;

    Context mcontext;
    public ManageRequestAdapter(Context context, QueryFactory<ParseObject> queryFactory){
        super(context, queryFactory);
        mcontext = context;
    }
    @Override
    public View getItemView(ParseObject BloodRequest, View v, ViewGroup parent){
        if (v == null) {
            v = View.inflate(getContext(), R.layout.mrequest_cardview_item, null);
        }
        super.getItemView(BloodRequest, v, parent);
        final View k=v;

        request_status= BloodRequest.getString("requestStatus");
        blood_request = BloodRequest.getString("bloodGroup");
        requestor_name = BloodRequest.getString("requestorName");
       location = BloodRequest.getString("location");
        date = BloodRequest.getDate("requiredBefore").toString();
       phone =  BloodRequest.getString("cellNumber");
        Button delete=(Button)v.findViewById(R.id.btnDeleteReq);

       requestor_city =  BloodRequest.getString("city");
        v.setClickable(true);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("status_request - ", request_status);
                if (request_status.equals("Awaiting")) {
                    Intent i1 = new Intent(v.getContext(), RequestVerificationActivity.class);
                    i1.putExtra("request_status",request_status);
                    i1.putExtra("blood_Request",blood_request);
                    i1.putExtra("requestor_name",requestor_name);
                    i1.putExtra("location",location);
                    i1.putExtra("date",date);
                    i1.putExtra("phone", phone);
                    i1.putExtra("city", requestor_city);
                    v.getContext().startActivity(i1);
                    ((ManageRequestsScreen)mcontext).finish();
                } else {
                    Toast.makeText(v.getContext(), "Request is already Posted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("BloodRequest");
                query.whereEqualTo("requestorName", requestor_name);
                query.whereEqualTo("location", location);
                query.whereEqualTo("city", requestor_city);
                query.whereEqualTo("cellNumber", phone);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        ParseObject delete_entry=parseObjects.get(0);
                        try {
                            delete_entry.delete();
//                            Intent jik=new Intent(mcontext,ManageRequestsScreen.class);
//                            mcontext.startActivity(jik);
//                            ((ManageRequestsScreen)mcontext).finish();
                            Toast.makeText(mcontext,"Successfully deleted :"+requestor_name,Toast.LENGTH_SHORT).show();
                            ((ManageRequestsScreen)mcontext).recreate();




                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                });



            }
        });



        // Add the name view

        TextView bgTextView = (TextView) v.findViewById(R.id.reqname);
        bgTextView.setText((BloodRequest.getString("bloodGroup")) + "  Blood Group Required");

       TextView titleTextView = (TextView) v.findViewById(R.id.textViewName);
        titleTextView.setText(BloodRequest.getString("requestorName"));

        // Add the city view
       TextView cityView = (TextView)v.findViewById(R.id.textViewCity);
        cityView.setText(BloodRequest.getString("city"));

        // Add the hospital location
        TextView hosLocation = (TextView)v.findViewById(R.id.textViewLoc);
        hosLocation.setText(BloodRequest.getString("location"));

        //Add the Date
        String beforeDate = BloodRequest.getDate("requiredBefore").toString();
        String[] separatedDate = beforeDate.split(" ");
        TextView requiredBefore = (TextView)v.findViewById(R.id.textViewDate);
        requiredBefore.setText(separatedDate[1]+" "+separatedDate[2]+" "+separatedDate[5]);

        //Add the Contact info
        TextView phone = (TextView)v.findViewById(R.id.textViewphone);
        phone.setText(BloodRequest.getString("cellNumber"));

        // Add the request stats
        TextView status = (TextView)v.findViewById(R.id.textViewstatus);
        status.setText(BloodRequest.getString("requestStatus"));
        return v;
    }
    public static String get_request_info()
    {
        return request_status;
    }

}
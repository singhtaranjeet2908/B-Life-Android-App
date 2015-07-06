package com.nyu.blife_app;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Yeshwant on 25/04/2015.
 */
public class CustomAdapter extends ParseQueryAdapter<ParseUser> {


    static String getDetail;
    String username, phonenumber;


    public CustomAdapter(Context context, QueryFactory<ParseUser> queryFactory) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri


        super(context, queryFactory);


//                new ParseQueryAdapter.QueryFactory<ParseUser>() {
//
//            //String[] blood1 = blood;
//            public ParseQuery<ParseUser> create() {
//                ParseQuery query = ParseUser.getQuery();
//                //query.whereEqualTo("userType","Donor");
//                query.whereEqualTo("bloodGroup", blood);
//                //query.whereEqualTo("zipCode", zip);
//
//                Log.i("query output", query.toString());
//                return query;
//
//            }
//
//        });
    }
    // Customize the layout by overriding getItemView

    @Override
    public View getItemView(ParseUser user, View v, ViewGroup parent) {
        // build your views
        // Add the title view
//        ListView titleTextView = (ListView) v.findViewById(R.id.listview);
//       titleTextView.setAdapter();
        if (v == null) {
            v = View.inflate(getContext(), R.layout.recyclerview_items_layout, null);
        }

        super.getItemView(user, v, parent);

        //super.getItemView(user, v, parent);


        username = user.getString("username");
        phonenumber = user.getNumber("phoneNumber").toString();
        TextView view1 = (TextView)v.findViewById(R.id.item_title);
        TextView errorMsg = (TextView)v.findViewById(R.id.item_error);
        Log.d("this is", user.getString("firstName") + " " + user.getString("lastName"));
        if ((!(user.getString("firstName")).isEmpty()) && (!(user.getString("lastName").isEmpty())))
        {

            Toast.makeText(getContext(),"list of donors",Toast.LENGTH_LONG).show();
            view1.setText(user.getString("firstName") + " " + user.getString("lastName"));
            getDetail = phonenumber;
        }
        else
        {
            Toast.makeText(getContext(),"there are no donors",Toast.LENGTH_LONG).show();
        }
        ImageView imgIcon = (ImageView) v.findViewById(R.id.item_icon);
        Picasso.with(imgIcon.getContext()).load(R.mipmap.phone).into(imgIcon);
        return v;
    }

    public static String get_number_info()
    {
        return getDetail;
    }
}

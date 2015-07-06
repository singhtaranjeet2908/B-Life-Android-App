package com.nyu.blife_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ramakrishnacmanyam on 4/19/15.
 */
public class SearchHosListAdapter extends ArrayAdapter<String>  {


        String[] help_name;
        Integer image_id;
        Context context;
        ArrayList<String> list_data;
        public SearchHosListAdapter(Activity context,Integer image_id, ArrayList<String> text){
            super(context, R.layout.list_row_helpline, text);
            // TODO Auto-generated constructor stub
            this.list_data= text;
            this.image_id = image_id;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View single_row = inflater.inflate(R.layout.list_row_helpline, null,
                    true);
            TextView textView = (TextView) single_row.findViewById(R.id.tv_helpline);
            ImageView imageView = (ImageView) single_row.findViewById(R.id.iv_helpline);
            textView.setText(help_name[position]);
            imageView.setImageResource(image_id);
            return single_row;
        }
    }



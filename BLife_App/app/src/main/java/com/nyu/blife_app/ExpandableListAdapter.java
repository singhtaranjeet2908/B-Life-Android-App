package com.nyu.blife_app;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;



public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    // group images
    private List<Integer> _listDataHeaderImages;

    // child image data in format of group images, child images
    //private HashMap<Integer, List<Integer>> _listDataChildImages;



    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData,
                                 List<Integer> listGroupImages) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._listDataHeaderImages = listGroupImages;
        //this._listDataChildImages = listChildImages;
}

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);


        /*//set background color for child items of "BLOOD REQUEST" & "SEARCH DONATION CAMPS" list group items
        if(groupPosition == 1 && childPosition == 0
            || groupPosition == 1 && childPosition == 1
            || groupPosition == 1 && childPosition == 2
            || groupPosition == 2 && childPosition == 0
            || groupPosition == 2 && childPosition == 1) {
            txtListChild.setText(childText);
            txtListChild.setBackgroundResource(R.color.home_activity_button);
        }
        else{
            txtListChild.setText(childText);
            txtListChild.setBackgroundResource(R.color.home_activity_button);
        }*/

        /*if(this._listDataHeader.get(groupPosition).equals("BLOOD REQUEST")
                && childText.equals("SEND BLOOD REQUEST")||childText.equals("VIEW BLOOD REQUEST")
                ||childText.equals("MANAGE BLOOD REQUESTS")){
            txtListChild.setBackgroundColor(_context.getResources().getColor(R.color.home_activity_button));
        }*/

        /*if(this._listDataHeader.get(groupPosition).equals("SEARCH DONATION CAMPS")
                && childText.equals("SEARCH HOSPITALS")||
                childText.equals("SEARCH BLOOD BANKS")){
            txtListChild.setBackgroundColor(_context.getResources().getColor(R.color.home_activity_button));
        }*/


        /*if(childText.equals("SEND BLOOD REQUEST")||childText.equals("VIEW BLOOD REQUEST")
                ||childText.equals("MANAGE BLOOD REQUESTS")||childText.equals("SEARCH HOSPITALS")||
                childText.equals("SEARCH BLOOD BANKS")){
            txtListChild.setBackgroundColor(_context.getResources().getColor(R.color.home_activity_button));
        }*/

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
        public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.groupImageView);
        int imageId = this._listDataHeaderImages.get(groupPosition);
        imageView.setImageResource(imageId);


       /* //set background color for "BLOOD REQUEST" & "SEARCH DONATION CAMPS" list group items, which are not buttons
        if(groupPosition == 1 || groupPosition == 2) {

                lblListHeader.setBackgroundResource(R.color.home_activity_header);
            }
        else{
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            lblListHeader.setBackgroundResource(R.color.home_activity_button);
            }*/



            /*if(gr) {
                lblListHeader.setBackgroundColor(_context.getResources().getColor(R.color.home_activity_header));
            }*/

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

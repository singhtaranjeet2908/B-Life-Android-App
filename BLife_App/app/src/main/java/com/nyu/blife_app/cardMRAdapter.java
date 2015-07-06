package com.nyu.blife_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Pranav Gunner on 4/23/2015.
 */
public class cardMRAdapter extends RecyclerView.Adapter<cardMRAdapter.ViewHolder> {

    private static FetchDonorListData[] itemsData;
    private static Context context;

    public cardMRAdapter(Context context, FetchDonorListData[] itemsData)
    {
        this.context = context;
        this.itemsData = itemsData;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mrequest_cardview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(cardMRAdapter.ViewHolder viewHolder, int position) {

        viewHolder.reqname.setText("Blood Group Required");
        viewHolder.deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Text view clicked. Donor Name.", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Delete Request")
                        .setContentText("Action can't be Reverted!")
                        .setConfirmText("Delete Request")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // Code for deleting request
                                sDialog.setTitleText("Deleted!")
                                        .setContentText("Request Deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView reqname;
        public TextView textViewName;
        public TextView textViewCity;
        public TextView textViewLoc;
        public TextView textViewDate;
        public TextView textViewphone;
        public TextView textViewstatus;
        public TextView textViewId;
        public Button deleteRequest;
        public CardView card;


        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);


           // card = (CardView) itemLayoutView.findViewById(R.id.card);
            reqname = (TextView) itemLayoutView.findViewById(R.id.reqname);
            textViewName = (TextView) itemLayoutView.findViewById(R.id.textViewName);
            textViewCity = (TextView) itemLayoutView.findViewById(R.id.textViewCity);
            textViewLoc = (TextView) itemLayoutView.findViewById(R.id.textViewLoc);
            textViewDate = (TextView) itemLayoutView.findViewById(R.id.textViewDate);
            textViewphone = (TextView) itemLayoutView.findViewById(R.id.textViewphone);
            textViewstatus = (TextView) itemLayoutView.findViewById(R.id.textViewstatus);
//            deleteRequest = (Button) itemLayoutView.findViewById(R.id.btnDeleteReq);

            //final String phone_number = textViewphone.getText().toString();
            itemLayoutView.setClickable(true);
            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Context context = itemLayoutView.getContext();
                    Intent i = new Intent(v.getContext(), RequestVerificationActivity.class);
                            //i.putExtra("Phone Number", viewHolder.textViewDate.getText().toString());
                            v.getContext().startActivity(i);
                }
            });
        }
    }
}

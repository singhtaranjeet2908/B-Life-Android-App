package com.nyu.blife_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Pranav Gunner on 4/26/2015.
 */
public class card_viewBR_adapter extends RecyclerView.Adapter<card_viewBR_adapter.ViewHolder> {


    private static FetchDonorListData[] itemsData;
    private static Context context;

    public card_viewBR_adapter(Context context, FetchDonorListData[] itemsData)
    //public card_viewBR_adapter(Context context)
    {
        this.context = context;
        this.itemsData = itemsData;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_blood_requests, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String phoneNo = holder.vbr_textViewphone.getText().toString();
        holder.contactSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Text view clicked. Donor Name.", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Contact Sender?")
                        .setCustomImage(R.mipmap.phone)
                        .setConfirmText("Call Sender")
                        .setCancelText("Send SMS")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent call_intent = new Intent(Intent.ACTION_DIAL);
                                call_intent.setData(Uri.parse("tel:" + phoneNo));
                                context.startActivity(call_intent);
                                sDialog.cancel();

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phoneNo, null,"Sender of this message is " +
                                        "Interested in Blood Donation on the request you posted!" , null, null);
                                //Toast.makeText(v.getContext(), "SMS sent.",Toast.LENGTH_LONG).show();
                                sDialog.cancel();
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

        public TextView vbr_reqdetails;
        public TextView vbr_textViewName;
        public TextView vbr_textViewCity;
        public TextView vbr_textViewLoc;
        public TextView vbr_textViewDate;
        public TextView vbr_textViewphone;
        public Button contactSender;
        public CardView card;


        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);


           // card = (CardView) itemLayoutView.findViewById(R.id.card);
            vbr_reqdetails = (TextView) itemLayoutView.findViewById(R.id.reqname);
            vbr_textViewName = (TextView) itemLayoutView.findViewById(R.id.textViewNamevbr);
            vbr_textViewCity = (TextView) itemLayoutView.findViewById(R.id.textViewCityvbr);
            vbr_textViewLoc = (TextView) itemLayoutView.findViewById(R.id.textViewLocvbr);
            vbr_textViewDate = (TextView) itemLayoutView.findViewById(R.id.textViewDatevbr);
            vbr_textViewphone = (TextView) itemLayoutView.findViewById(R.id.textViewphonevbr);
            contactSender = (Button) itemLayoutView.findViewById(R.id.btnContactreqSender);

            itemLayoutView.setClickable(true);

            //final String phone_number = textViewphone.getText().toString();

//                itemLayoutView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Context context = itemLayoutView.getContext();
//                        Intent i = new Intent(v.getContext(), RequestVerificationActivity.class);
//                        //i.putExtra("Phone Number", viewHolder.textViewDate.getText().toString());
//                        v.getContext().startActivity(i);
//                    }
//                });
        }
    }
}



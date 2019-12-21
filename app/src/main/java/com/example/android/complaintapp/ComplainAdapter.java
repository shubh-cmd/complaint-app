package com.example.android.complaintapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ComplainViewHolder>{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *

     * @param options
     */
    private Context mCtx;
    private List<Complain> complainList;


    public ComplainAdapter(Context mCtx,List<Complain> complainList) {
        this.mCtx = mCtx;
        this.complainList = complainList;
    }



    @NonNull
    @Override
    public ComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.complain_row,parent,false);



        return new ComplainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplainViewHolder holder, int position) {
        final Complain complain = complainList.get(position);
        holder.setTimestamp(complain.getTimestamp());
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx,DetailsActivity.class);
                intent.putExtra("mName",complain.getName());
                intent.putExtra("mMobileNumber",complain.getMobile_num());
                intent.putExtra("mRoomNumber",complain.getRoom_num());
                intent.putExtra("mComplainDetail",complain.getComplain_text());
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complainList.size();
    }

    class ComplainViewHolder extends RecyclerView.ViewHolder{

        TextView mTimestamp;
        View mView;
        TextView mName,mMobileNumber,mRoomNumber,mComplainDetail;
        RelativeLayout relative;

        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            mView =itemView;

            mName = mView.findViewById(R.id.name_View);
            mMobileNumber = mView.findViewById(R.id.mobile_no_View);
            mRoomNumber = mView.findViewById(R.id.room_no_View);
            mComplainDetail = mView.findViewById(R.id.complain_detail_View);
            relative = mView.findViewById(R.id.relative);



        }



        public void setTimestamp(Long post_time){

            mTimestamp = mView.findViewById(R.id.date_view);
            SimpleDateFormat sfd = new SimpleDateFormat("EEE, MMM d, yyyy          'at' hh:mm aaa");

            mTimestamp.setText(sfd.format(new Date(post_time)));

        }
    }
}

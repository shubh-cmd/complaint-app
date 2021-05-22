package com.example.android.complaintapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ComplainViewHolder>{



    private Context mCtx;
    private List<Complain> complainList;
    private DatabaseReference mDatabase;


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
    public void onBindViewHolder(@NonNull final ComplainViewHolder holder, final int position) {

        final Complain complain = complainList.get(position);


        holder.setTimestamp(complain.getTimestamp());

        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = holder.setKey(complain.getKey());

                mDatabase = FirebaseDatabase.getInstance().getReference().child("personal complaint");
                mDatabase.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mCtx,"deleted ",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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
        ImageView mDeleteBtn;
        View mView;
        TextView mName,mMobileNumber,mRoomNumber,mComplainDetail;
        RelativeLayout relative;
        String key;

        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            mView =itemView;

            mName = mView.findViewById(R.id.name_View);
            mMobileNumber = mView.findViewById(R.id.mobile_no_View);
            mRoomNumber = mView.findViewById(R.id.room_no_View);
            mComplainDetail = mView.findViewById(R.id.complain_detail_View);
            relative = mView.findViewById(R.id.relative);
            mDeleteBtn = mView.findViewById(R.id.deleteBtn);






        }



        public void setTimestamp(Long post_time){

            mTimestamp = mView.findViewById(R.id.date_view);
            SimpleDateFormat sfd = new SimpleDateFormat("EEE, MMM d, yyyy    hh:mm aaa");

            mTimestamp.setText(sfd.format(new Date(post_time)));

        }

        public String setKey(String key) {
            this.key = key;
            return this.key;
        }
    }
}

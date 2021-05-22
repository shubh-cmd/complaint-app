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

public class OtherComplainAdapter extends RecyclerView.Adapter<OtherComplainAdapter.OtherComplainViewHolder>{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link } for configuration options.
     *

     * @param options
     */
    private Context mCtx;
    private DatabaseReference mOtherDatabase;
    private List<OtherComplain> othercomplainList;


    public OtherComplainAdapter(Context mCtx,List<OtherComplain> othercomplainList) {
        this.mCtx = mCtx;
        this.othercomplainList = othercomplainList;
    }



    @NonNull
    @Override
    public OtherComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.other_complain_row,parent,false);



        return new OtherComplainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OtherComplainViewHolder holder, int position) {
        final OtherComplain othercomplain = othercomplainList.get(position);
        holder.setTimestamp(othercomplain.getTimestamp());
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = holder.setKey(othercomplain.getKey());
                Log.v("SHUBHAMKUMAR","hi "+key);
                mOtherDatabase = FirebaseDatabase.getInstance().getReference().child("other complaint");
                mOtherDatabase.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mCtx,"deleted ",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx,OtherdetailsActivity.class);
                intent.putExtra("image",othercomplain.getImage());
                intent.putExtra("complain",othercomplain.getComplainDes());
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return othercomplainList.size();
    }

    class OtherComplainViewHolder extends RecyclerView.ViewHolder{

        TextView mTimestamp;
        View mView;
        TextView mOtherComplainDetail;
        ImageView mImage;
        RelativeLayout relativeLayout;
        String key;
        ImageView mDeleteBtn;



        public OtherComplainViewHolder(@NonNull View itemView) {
            super(itemView);
            mView =itemView;

            mOtherComplainDetail = mView.findViewById(R.id.complain_text_View);
            mImage = mView.findViewById(R.id.image_view);
            relativeLayout = mView.findViewById(R.id.relative_text_view);
            mDeleteBtn = mView.findViewById(R.id.other_deleteBtn);




        }



        public void setTimestamp(Long post_time){

            mTimestamp = mView.findViewById(R.id.date_text_view);
            SimpleDateFormat sfd = new SimpleDateFormat("EEE, MMM d, yyyy    hh:mm aaa");

            mTimestamp.setText(sfd.format(new Date(post_time)));

        }

        public String setKey(String key) {
            this.key = key;
            return this.key;
        }
    }
}
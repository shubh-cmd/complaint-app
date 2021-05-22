package com.example.android.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OthercomplaintActivity extends AppCompatActivity {

    private RecyclerView mOther_complaint_list;
    private String mBhawanName;





    private OtherComplainAdapter adapter;
    private List<OtherComplain> othercomplainList;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othercomplaint);


        mOther_complaint_list =  findViewById(R.id.other_complain_list);
        mOther_complaint_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mOther_complaint_list.setLayoutManager(linearLayoutManager);
        othercomplainList = new ArrayList<>();
        adapter = new OtherComplainAdapter(this, othercomplainList);
        mOther_complaint_list.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        String uid =mCurrentUser.getUid();
        Log.v("USERID","Hi "+uid);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabase= FirebaseDatabase.getInstance().getReference().child("other complaint");
        //mDatabase.addListenerForSingleValueEvent(valueEventListener);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mBhawanName = String.valueOf(dataSnapshot.child("bhawanName").getValue());
                Log.v("SHUBHAM","hi "+mBhawanName);
                Query query = mDatabase
                        .orderByChild("BhawanName")
                        .equalTo(mBhawanName);
                query.addListenerForSingleValueEvent(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.v("OtherComplain","hi "+mBhawanName);


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            othercomplainList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OtherComplain othercomplain = snapshot.getValue(OtherComplain.class);
                    othercomplainList.add(othercomplain);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


}

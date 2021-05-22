package com.example.android.complaintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView mName,mMobileNumber,mRoomNumber,mComplainDetail;


    String name,mob_no,room_no,complain_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mName = findViewById(R.id.name_View);
        mMobileNumber = findViewById(R.id.mobile_no_View);
        mRoomNumber = findViewById(R.id.room_no_View);
        mComplainDetail =findViewById(R.id.complain_detail_View);

        name = getIntent().getStringExtra("mName");
        mob_no =getIntent().getStringExtra("mMobileNumber");
        room_no =getIntent().getStringExtra("mRoomNumber");
        complain_text =getIntent().getStringExtra("mComplainDetail");
        mName.setText(name);
        mMobileNumber.setText(mob_no);
        mRoomNumber.setText(room_no);
        mComplainDetail.setText(complain_text);


    }
}

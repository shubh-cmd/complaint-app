package com.example.android.complaintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class PersonalActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mRoomNum;
    private EditText mBhawanName;
    private EditText mMobileNum;
    private EditText mComplainText;
    private Button mSubmitBtn;
    private ProgressDialog mProgress;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        mNameField= findViewById(R.id.name_field);
        mRoomNum= findViewById(R.id.room_no_field);
        mBhawanName= findViewById(R.id.bhawan_name);
        mMobileNum= findViewById(R.id.mobile_no);
        mComplainText= findViewById(R.id.complain_text);
        mSubmitBtn =findViewById(R.id.submit_Btn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("personal complaint");
        mProgress = new ProgressDialog(this);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPostingComplain();
            }
        });
    }

    private void startPostingComplain() {


          String nameField =mNameField.getText().toString().trim();
          String roomNum =mRoomNum.getText().toString().trim();
          String BhawanName =mBhawanName.getText().toString().toLowerCase().trim();
          String MobileNum =" "+mMobileNum.getText().toString().trim();
          String ComplainText =mComplainText.getText().toString().trim();

          if(TextUtils.isEmpty(nameField)) {
              Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
              return;
          }

        if(TextUtils.isEmpty(roomNum)) {
            Toast.makeText(this," enter room number",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(BhawanName)) {
            Toast.makeText(this," enter bhawan name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(MobileNum)) {
            Toast.makeText(this," enter mobile number",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(ComplainText)) {
            Toast.makeText(this,"enter name complain",Toast.LENGTH_LONG).show();
            return;
        }

              mProgress.setMessage("submitting complain");
              mProgress.show();

              DatabaseReference personalPost = mDatabase.push();

              Map map = new HashMap();
              map.put("timestamp", ServerValue.TIMESTAMP);

              personalPost.updateChildren(map);
              personalPost.child("name").setValue(nameField);
              personalPost.child("room_num").setValue(roomNum);
              personalPost.child("bhawan_name").setValue(BhawanName);
              personalPost.child("mobile_num").setValue(MobileNum);
              personalPost.child("complain_text").setValue(ComplainText);
              personalPost.child("key").setValue(personalPost.getKey());

              mProgress.dismiss();
              Toast.makeText(PersonalActivity.this,"complain submitted",Toast.LENGTH_LONG).show();


    }
}

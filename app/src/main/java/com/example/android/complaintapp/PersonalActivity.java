package com.example.android.complaintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class PersonalActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mRoomNum;
    private Spinner mBhawanSpinner;
    private String mBhawanName;
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
        mBhawanSpinner= findViewById(R.id.bhawan_spinner);

        setupSpinner();
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

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mBhawanSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mBhawanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.bhawan_0))) {
                        mBhawanName = "aazad";
                    } else if (selection.equals(getString(R.string.bhawan_1))) {
                        mBhawanName = "cautley";
                    }else if (selection.equals(getString(R.string.bhawan_2))) {
                        mBhawanName = "ganga";
                    }else if (selection.equals(getString(R.string.bhawan_3))) {
                        mBhawanName = "govind";
                    }else if (selection.equals(getString(R.string.bhawan_4))) {
                        mBhawanName = "kasturba";
                    }else if (selection.equals(getString(R.string.bhawan_5))) {
                        mBhawanName = "rajendra";
                    }else if (selection.equals(getString(R.string.bhawan_6))) {
                        mBhawanName = "radhakrishnan";
                    }else if (selection.equals(getString(R.string.bhawan_7))) {
                        mBhawanName = "rajiv";
                    }else {
                        mBhawanName = "sarojini";
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBhawanName = null; // Unknown
            }
        });
    }

    private void startPostingComplain() {


          String nameField =mNameField.getText().toString().trim();
          String roomNum =mRoomNum.getText().toString().trim();
          String BhawanName =mBhawanName;
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

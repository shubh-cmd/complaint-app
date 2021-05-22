package com.example.android.complaintapp;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener{


    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private Spinner mBhawanSpinner;
    private String mBhawanName;

    private TextView textViewSignin;
    private TextView mStudentPage;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;





    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //initializing views
        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        mBhawanSpinner = findViewById(R.id.spinner_bhawan);

        setupSpinner();
        textViewSignin = findViewById(R.id.textViewSignin);
        mStudentPage = findViewById(R.id.student_view);

        buttonSignup =  findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);


        String text = "Already Registered? Signin here";
        String studentText = "Student? Click here";

        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                progressDialog.show();
                startActivity(new Intent(MainActivity1.this, ProfileActivity.class));
            }
        };

        ss.setSpan(clickableSpan,20,26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignin.setText(ss);
        textViewSignin.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString sss = new SpannableString(studentText);
        ClickableSpan clickableSpanStudent = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                progressDialog.show();
                startActivity(new Intent(MainActivity1.this, MainActivity2.class));
            }
        };

        sss.setSpan(clickableSpanStudent,9,14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStudentPage.setText(sss);
        mStudentPage.setMovementMethod(LinkMovementMethod.getInstance());

        //attaching listener to button
        buttonSignup.setOnClickListener(this);


        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }




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

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        /*if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, ProfileActivity.class));
        }*/

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    }
    // [END on_start_check_user]



    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        final String bhawanName = mBhawanName;

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(bhawanName)){
            Toast.makeText(this,"Please Select Bhawan",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            String user_id = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            current_user_db.child("bhawanName").setValue(bhawanName);
                            current_user_db.child("image").setValue("default");
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(MainActivity1.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity1.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }



 }



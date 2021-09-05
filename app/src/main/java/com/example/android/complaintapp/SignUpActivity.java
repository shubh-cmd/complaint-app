package com.example.android.complaintapp;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{


    //defining view objects
    private static final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private long pressedTime;
    private EditText mEmailTextView;
    private EditText mPasswordTextView;
    private Button mButtonForSignUp;
    private Spinner mBhawanSpinner;
    private String mBhawanName;

    private TextView mSignInTextView;
    private TextView mStudentPage;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        //initializing firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //initializing views
        mEmailTextView =  findViewById(R.id.editTextEmail);
        mPasswordTextView =  findViewById(R.id.editTextPassword);
        mBhawanSpinner = findViewById(R.id.spinner_bhawan);

        setupSpinner();
        mSignInTextView = findViewById(R.id.textViewSignin);
        mStudentPage = findViewById(R.id.student_view);

        mButtonForSignUp =  findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);


        String text = "Already Registered? Signin here";
        String studentText = "Student? Click here";

        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                progressDialog.show();
                startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
            }
        };

        ss.setSpan(clickableSpan,20,26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSignInTextView.setText(ss);
        mSignInTextView.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString sss = new SpannableString(studentText);
        ClickableSpan clickableSpanStudent = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                progressDialog.show();
                startActivity(new Intent(SignUpActivity.this, AddComlaintActivity.class));
            }
        };

        sss.setSpan(clickableSpanStudent,9,14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mStudentPage.setText(sss);
        mStudentPage.setMovementMethod(LinkMovementMethod.getInstance());

        //attaching listener to button
        mButtonForSignUp.setOnClickListener(this);


        //if getCurrentUser does not returns null
        if(mFirebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

    }

    /**
     * Setup the dropdown spinner that allows the user to select the Bhawan Name in which he or she reside.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array added in values resoursce file
        // the spinner will use the default layout, but i can also create my own layout for spinner
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

        if(view == mButtonForSignUp){
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
        // if the user already signed in just launch the activity
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }
    // [END on_start_check_user]



    private void registerUser(){

        //getting email and password from edit texts
        String email = mEmailTextView.getText().toString().trim();
        if(!isValidEmail(email)){
            mEmailTextView.setError("Please Enter Valid Email");
            return;
        }
        String password  = mPasswordTextView.getText().toString().trim();
        if(password.length() < 8){
            mPasswordTextView.setError("Password must be aleast 8 characters");
            return;
        }
        //number
        if(!password.matches("(.*[0-9].*)"))
        {
            mPasswordTextView.setError("Password must contain atleast one number");
            return;
        }

        //upper case
        if(!password.matches("(.*[A-Z].*)")){
            mPasswordTextView.setError("Password must contain atleast one Uppercase letter");
            return;
        }
        //symbol
        if(!password.matches("^(?=.*[_.()$&@]).*$")){
            mPasswordTextView.setError("Password must contain atleast one special character");
            return;
        }
        final String bhawanName = mBhawanName;

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            //Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            mEmailTextView.setError("Please Enter Email ");
            return;
        }

        if(TextUtils.isEmpty(password)){
            mPasswordTextView.setError("Please Enter Email ");
            return;
        }

        if(TextUtils.isEmpty(bhawanName)){
            mEmailTextView.setError("Please Enter Email ");
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Registration successful!", Toast.LENGTH_LONG).show();
                            sendEmailVerification();
                            String user_id = mFirebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            current_user_db.child("bhawanName").setValue(bhawanName);
                            current_user_db.child("image").setValue("default");
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String str = e.toString();
                String[] errorMessage = str.split(":");
                Toast.makeText(SignUpActivity.this,errorMessage[1],Toast.LENGTH_LONG).show();
            }
        });

    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public void sendEmailVerification() {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(SignUpActivity.class.getSimpleName(), "Email sent.");
                        }
                    }
                });
        // [END send_email_verification]
    }


}



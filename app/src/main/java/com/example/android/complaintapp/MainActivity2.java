package com.example.android.complaintapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private RadioButton mPersonal;
    private RadioButton mOther;
    private Button mNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mPersonal = findViewById(R.id.radioButton1);
        mOther = findViewById(R.id.radioButton2);
        mNext =findViewById(R.id.nextBtn);


        mPersonal.setOnClickListener(this);
        mOther.setOnClickListener(this);
        mNext.setOnClickListener(this);


    }



    public void onClick(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked) {
                    Log.v("MainActivity1", "personal" + checked);
                    mNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity2.this,PersonalActivity.class));
                        }
                    });


                }
                    break;
            case R.id.radioButton2:
                if (checked)
                    Log.v("MainActivity1","other"+checked);
                mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity2.this,OtherActivity.class));
                    }
                });
                break;
        }

    }
}

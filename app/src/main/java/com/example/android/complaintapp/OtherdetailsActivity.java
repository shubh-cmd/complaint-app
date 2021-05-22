package com.example.android.complaintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OtherdetailsActivity extends AppCompatActivity {

    private TextView mComplainDescription;
    private ImageView mImageView;

    String image,complainDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherdetails);

        mComplainDescription =  findViewById(R.id.complain_text_View);
        mImageView = findViewById(R.id.image_view);

        image = getIntent().getStringExtra("image");
        Log.v("SACHIN","hi "+image);
        complainDescription = getIntent().getStringExtra("complain");
        mComplainDescription.setText(complainDescription);
        Picasso.get().load(image).into(mImageView);

    }
}

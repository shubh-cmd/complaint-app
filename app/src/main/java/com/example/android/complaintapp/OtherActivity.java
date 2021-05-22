package com.example.android.complaintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.viewmodel.RequestCodes;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OtherActivity extends AppCompatActivity {

    private ImageButton mGalleryBtn;
    private ImageButton mCameraBtn;
    private Spinner mBhawanSpinner;
    private String mBhawanName;
    private EditText mComplainDes;
    private Button mSubmitBtn;
    private int CAMERA_PERMISSION_CODE = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private static final int RC_PHOTO_PICKER = 2;
    private static final int CAMERA_REQUEST_CODE= 1;

    private Uri imageUri;
    private Uri downloadUri;
    private Task<Uri> downloadUrl;
    private Uri mImageUri =null;
    private int upload_count=0;

    private ProgressDialog mProgress;

    ArrayList<Uri> imageList = new ArrayList<Uri>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        mGalleryBtn =findViewById(R.id.imageBtn);
        mCameraBtn =findViewById(R.id.cameraBtn);
        mBhawanSpinner = findViewById(R.id.bhawan_spinner);

        setupSpinner();
        mComplainDes =findViewById(R.id.complainDes);
        mSubmitBtn =findViewById(R.id.submitBtn);

        mStorage = FirebaseStorage.getInstance().getReference().child("photos");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("other complaint");

        mProgress=new ProgressDialog(this);

        mGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
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

    private void startPosting(){

        String bhawanName= mBhawanName;
        String complainDes= mComplainDes.getText().toString().trim();

        if(TextUtils.isEmpty(bhawanName)) {
            Toast.makeText(this," enter bhawan name",Toast.LENGTH_LONG).show();
            return;
        }


        if(TextUtils.isEmpty(complainDes)) {
            Toast.makeText(this," enter complain",Toast.LENGTH_LONG).show();
            return;
        }

        if(!TextUtils.isEmpty(bhawanName) && !TextUtils.isEmpty(complainDes) && mImageUri==null){
            mProgress.setMessage("submitting complain");
            mProgress.show();


            DatabaseReference otherPost = mDatabase.push();
            Map map = new HashMap();
            map.put("timestamp", ServerValue.TIMESTAMP);

            otherPost.updateChildren(map);
            otherPost.child("BhawanName").setValue(bhawanName);
            otherPost.child("ComplainDes").setValue(complainDes);
            otherPost.child("key").setValue(otherPost.getKey());

            mProgress.dismiss();
            Toast.makeText(OtherActivity.this, "complain submitted", Toast.LENGTH_LONG).show();

        }


         if(!TextUtils.isEmpty(bhawanName) && !TextUtils.isEmpty(complainDes) && mImageUri!=null) {
            mProgress.setMessage("submitting complain");
            mProgress.show();


            DatabaseReference otherPost = mDatabase.push();
            Map map = new HashMap();
            map.put("timestamp", ServerValue.TIMESTAMP);

            otherPost.updateChildren(map);
            otherPost.child("BhawanName").setValue(bhawanName);
            otherPost.child("ComplainDes").setValue(complainDes);
            otherPost.child("image").setValue(downloadUri.toString());
            otherPost.child("key").setValue(otherPost.getKey());

            mProgress.dismiss();
            Toast.makeText(OtherActivity.this, "complain submitted", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==RC_PHOTO_PICKER && resultCode == RESULT_OK){
           if(data.getClipData()!=null){
               mProgress.setMessage("selecting image");
               mProgress.show();
               int countClipData = data.getClipData().getItemCount();
               int currentImageSelect=0;

               while(currentImageSelect<countClipData){
                   imageUri =data.getClipData().getItemAt(currentImageSelect).getUri();
                   imageList.add(imageUri);
                   currentImageSelect = currentImageSelect+1;
               }

               for(upload_count=0;upload_count<imageList.size();upload_count++){
                   mImageUri =imageList.get(upload_count);
                   final StorageReference imageName=mStorage.child(mImageUri.getLastPathSegment());
                   imageName.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   downloadUri = uri;
                               }
                           });
                           mProgress.dismiss();
                           Toast.makeText(OtherActivity.this,"image selected",Toast.LENGTH_LONG).show();

                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           String message = e.toString();
                           Toast.makeText(OtherActivity.this,message,Toast.LENGTH_LONG).show();
                       }
                   });
               }

           }if(data.getClipData()==null){
                mProgress.setMessage("selecting image");
                mProgress.show();

               mImageUri = data.getData();



                // Get a reference to store file at chat_photos/<FILENAME>
                 final StorageReference photoRef = mStorage.child(mImageUri.getLastPathSegment());
                 photoRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                 downloadUri = uri;
                            }
                        });
                        mProgress.dismiss();
                        Toast.makeText(OtherActivity.this,"image selected",Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(OtherActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                });
            }


        }

        if(requestCode ==CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            mProgress.setMessage("selecting image");
            mProgress.show();
            mImageUri=data.getData();
            final StorageReference filePath =mStorage.child("photos").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUri = uri;
                        }
                    });

                    mProgress.dismiss();
                    Toast.makeText(OtherActivity.this,"image selected",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(OtherActivity.this,message,Toast.LENGTH_LONG).show();
                }
            });
        }

    }




}
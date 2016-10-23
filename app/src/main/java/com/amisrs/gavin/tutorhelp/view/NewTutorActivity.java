package com.amisrs.gavin.tutorhelp.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.TutorQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.other.DbBitmapUtility;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewTutorActivity extends AppCompatActivity {
    private static final String TAG = "NewTutorActivity";

    Button createButton;
    TextInputEditText zid;
    TextInputEditText fname;
    TextInputEditText lname;
    TextInputEditText email;
    TextInputLayout til_zid;
    TextInputLayout til_fName;
    TextInputLayout til_lName;
    TextInputLayout til_email;

    ImageButton captureButton;
    ImageView profilePic;

    String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 100;
    String imgPath = "default.png";
    String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tutor);

        zid = (TextInputEditText) findViewById(R.id.et_zid);
        fname = (TextInputEditText) findViewById(R.id.et_fname);
        lname = (TextInputEditText) findViewById(R.id.et_lname);
        email = (TextInputEditText) findViewById(R.id.et_email);

        til_zid = (TextInputLayout) findViewById(R.id.til_zid);
        til_fName = (TextInputLayout) findViewById(R.id.til_fname);
        til_lName = (TextInputLayout) findViewById(R.id.til_lname);
        til_email = (TextInputLayout) findViewById(R.id.til_email);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTutor();
            }
        });

        captureButton = (ImageButton) findViewById(R.id.btn_camera_capture);
        profilePic = (ImageView) findViewById(R.id.iv_camera);
        profilePic.setImageResource(R.drawable.ic_default_profile_pic);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraPermission();
            }
        });

    }
    //http://stackoverflow.com/questions/32942909/provide-custom-text-for-android-m-permission-dialog
    public void getCameraPermission() {
        System.out.println("getCameraPermission called");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("Permission is required to access camera")
                        .setMessage("Camera permission is needed to take a picture")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(NewTutorActivity.this, permissions, CAMERA_PERMISSION_CODE);

                            }

                        });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                //no explanation is required, permission is automatically requested
                ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE);
            }
        } else {
            takePicture();
        }
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profilePic.setImageBitmap(photo);
                    saveImagePath(photo);
                    Log.d(TAG, "User successfully took image");
                    break;
                case Activity.RESULT_CANCELED:
                    //if user decides not to take a picture
                    imgPath = "default.png";
                    Log.d(TAG, "User cancelled request to take image");
                    break;
                default:
                    imgPath = "default.png";
                    break;
            }
        }
    }

    //http://stackoverflow.com/questions/23131768/how-to-save-an-image-to-internal-storage-and-then-show-it-on-another-activity?noredirect=1&lq=1
    private String saveImagePath(Bitmap bitmap){
        FileOutputStream fileOutputStream = null;
        String imgFilePath = this.getFilesDir().toString();
        fileName = zid.getText().toString()+"_tutor.PNG";

        try{
            fileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(DbBitmapUtility.getBytes(bitmap));
            fileOutputStream.flush();
            imgPath = imgFilePath+"/" + fileName;
            //TODO: delete the test line below
            System.out.println("imgPath = " + imgPath);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                fileOutputStream.close();
                Log.d(TAG, "FileOutputStream is closed successfully");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "File path is saved to DB");
        return imgFilePath;

    }
    public void createTutor() {
        //input validation
        //make sure zID has no 'z'
        //TODO: improve validation, handle null
        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if (zidMatcher.find()) {
            int zidInt = Integer.parseInt(zid.getText().toString());
            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();
            String emailString = email.getText().toString();
            String profilePath = imgPath;



            PersonQueries personQueries = new PersonQueries(this);
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt, profilePath, emailString));

            TutorQueries tutorQueries = new TutorQueries(this);
            tutorQueries.addTutor(new Tutor(addedPerson.getPersonID(), addedPerson));

            Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
            //add stuff to database
            finish();
        } else {
            //the zid has non-digits in it
            zid.setError("zID must only contain numbers.");
        }


    }
}

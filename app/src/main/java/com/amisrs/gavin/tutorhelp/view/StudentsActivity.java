package com.amisrs.gavin.tutorhelp.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.OnItemClickListener;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentsActivity extends AppCompatActivity implements StudentListFragment.OnFragmentInteractionListener,
        OnItemClickListener,
        NewStudentDialogFragment.OnFragmentInteractionListener,
        NewStudentDialogFragment.NewStudentDialogFragmentListener,
        StudentDetailsFragment.OnFragmentInteractionListener {
    private static final String TAG = "StudentsActivity";
    private ImageView profilePic;
    private ImageButton captureButton;
    private String fileName;
    private String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 100;
    private String imgPath = "default.png";

    TextInputEditText zid;
    TextInputEditText fname;
    TextInputEditText lname;
    TextInputEditText email;

    Tutorial tutorial;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        tutorial = getIntent().getParcelableExtra("tutorial");

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_add_student);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rl_left, StudentListFragment.newInstance(tutorial));
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStudentList();

    }

    public void addStudent() {
        NewStudentDialogFragment nsdf = NewStudentDialogFragment.newInstance(tutorial);
        FragmentManager fragmentManager = getSupportFragmentManager();
        nsdf.show(fragmentManager, "dialog");
    }

    public void changeStudent(Student student) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_right, StudentDetailsFragment.newInstance(student, tutorial));
        fragmentTransaction.commit();
    }

    public void refreshStudentList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_left, StudentListFragment.newInstance(tutorial));
        fragmentTransaction.commit();

    }
/*
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
                                ActivityCompat.requestPermissions(StudentsActivity.this, permissions, CAMERA_PERMISSION_CODE);

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
        NewStudentDialogFragment.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
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
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String name) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        refreshStudentList();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        Log.d(TAG, "Cancelled add student.");
    }

    @Override
    public void onStudentClick(View view, Student student) {
        changeStudent(student);
    }
}

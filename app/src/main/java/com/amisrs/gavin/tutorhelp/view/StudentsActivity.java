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
import android.support.v4.app.Fragment;
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
import com.amisrs.gavin.tutorhelp.controller.MarkListAdapter;
import com.amisrs.gavin.tutorhelp.controller.OnDeleteListener;
import com.amisrs.gavin.tutorhelp.controller.OnItemClickListener;
import com.amisrs.gavin.tutorhelp.controller.OnMarkUpdateListener;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Mark;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.NavDrawer.DrawerActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentsActivity extends DrawerActivity implements StudentListFragment.OnFragmentInteractionListener,
        OnItemClickListener,
        NewStudentDialogFragment.OnFragmentInteractionListener,
        NewStudentDialogFragment.NewStudentDialogFragmentListener,
        StudentDetailsFragment.OnFragmentInteractionListener,
        OnMarkUpdateListener,
        OnDeleteListener {

    private static final String TAG = "StudentsActivity";

    private final String RIGHT_TAG = "right";
    private ImageView profilePic;
    private ImageButton captureButton;
    private String fileName;
    private String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 100;
    private String imgPath = "default.png";

    private TextInputEditText zid;
    private TextInputEditText fname;
    private TextInputEditText lname;
    private TextInputEditText email;

    //private static final String TAG = "AssessmentsActivity";
    private Student currentStudent;

    private Tutor tutor;
    private Tutorial tutorial;
    private FloatingActionButton floatingActionButton;
    private String overrideBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        tutor = getIntent().getParcelableExtra("tutor");
        tutorial = getIntent().getParcelableExtra("tutorial");
        overrideBack = getIntent().getExtras().getString("fromAttendance");
        System.out.println(TAG + " " + overrideBack);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add_student);
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
        currentStudent = student;
        Log.d(TAG, "Swapping details fragment to student: " + student.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_right, StudentDetailsFragment.newInstance(student, tutorial, tutor), RIGHT_TAG);
        fragmentTransaction.commit();
    }

    public void refreshStudentList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_left, StudentListFragment.newInstance(tutorial), RIGHT_TAG);
        fragmentTransaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String name) {
        if (name.equals("save")) {
            refreshStudentList();
            //changeStudent(currentStudent);
        }
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
        currentStudent = student;
        Log.d(TAG, "currentStudent is " + currentStudent.toString());
        changeStudent(student);
    }

    @Override
    public void onMarkUpdate(Mark mark) {
        //recalculate student grade
        //this is not used
        StudentQueries studentQueries = new StudentQueries(this);
        Student student = studentQueries.getStudentById(mark.getStudentID());
        studentQueries.recalculateGradeForStudentAndTerm(student, tutorial.getTerm());
        changeStudent(student);
    }

    @Override
    public void onDelete() {
        refreshStudentList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(RIGHT_TAG);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        //override back button press if activity is started from BaseActivity

        if (overrideBack != null) {
            Intent intent = new Intent(this, TutorialListActivity.class);
            intent.putExtra("tutor", tutor);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}

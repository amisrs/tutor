package com.amisrs.gavin.tutorhelp.view;

import android.app.Dialog;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.OnItemClickListener;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentsActivity extends DrawerActivity implements StudentListFragment.OnFragmentInteractionListener,
        OnItemClickListener,
        NewStudentDialogFragment.OnFragmentInteractionListener,
        NewStudentDialogFragment.NewStudentDialogFragmentListener,
        StudentDetailsFragment.OnFragmentInteractionListener {
    private static final String TAG = "StudentsActivity";


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
        NewStudentDialogFragment nsdf = NewStudentDialogFragment.newInstance();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String name) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        Dialog dialog  = fragment.getDialog();
        EditText zid = (EditText)dialog.findViewById(R.id.student_zid);
        EditText fname = (EditText)dialog.findViewById(R.id.student_fname);
        EditText lname = (EditText)dialog.findViewById(R.id.student_lname);

        //TODO: improve validation
        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if(zidMatcher.find()) {


            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();
            int zidInt = Integer.parseInt(zid.getText().toString());

            PersonQueries personQueries = new PersonQueries(this);
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt));

            StudentQueries studentQueries = new StudentQueries(this);
            studentQueries.addStudent(new Student(addedPerson.getPersonID(), addedPerson), tutorial);

            Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
            refreshStudentList();

        } else {
            //the zid has non-digits in it
            zid.setError("zID must only contain numbers.");
        }
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

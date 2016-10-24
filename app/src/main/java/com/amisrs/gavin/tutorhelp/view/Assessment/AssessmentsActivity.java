package com.amisrs.gavin.tutorhelp.view.Assessment;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.OnAssessmentClickListener;
import com.amisrs.gavin.tutorhelp.controller.OnDeleteListener;
import com.amisrs.gavin.tutorhelp.controller.OnItemClickListener;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.NavDrawer.DrawerActivity;
import com.amisrs.gavin.tutorhelp.view.NewStudentDialogFragment;
import com.amisrs.gavin.tutorhelp.view.StudentDetailsFragment;
import com.amisrs.gavin.tutorhelp.view.StudentListFragment;

import java.util.ArrayList;

public class AssessmentsActivity extends DrawerActivity implements AssessmentListFragment.OnFragmentInteractionListener,
        AssessmentDetailsFragment.OnFragmentInteractionListener,
        NewAssessmentDialogFragment.NewAssessmentDialogFragmentListener,
        NewAssessmentDialogFragment.OnNewAssessmentDialogFragmentInteractionListener,
        OnAssessmentClickListener,
        OnDeleteListener
        {
    private static final String TAG = "AssessmentsActivity";
    private final String RIGHT_TAG = "right";
    private Assessment currentAssessment;
    Tutor tutor;
    Tutorial tutorial;
    String currentTerm;
    Spinner termSp;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        tutor = getIntent().getParcelableExtra("tutor");
        tutorial = getIntent().getParcelableExtra("tutorial");
        currentTerm = tutorial.getTerm();
        Log.d(TAG, "got term: " + currentTerm);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_add_assessment);
        termSp = (Spinner)findViewById(R.id.sp_term);
        termSp.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        termSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTerm((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAssessment();
            }
        });

        refreshSpinner();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_left, AssessmentListFragment.newInstance(currentTerm));
        fragmentTransaction.commit();

    }

    public void refreshSpinner() {
        AssessmentQueries assessmentQueries = new AssessmentQueries(this);
        ArrayList<String> terms = assessmentQueries.getTermsThatExist();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, terms);
        termSp.setAdapter(arrayAdapter);

    }

    public void changeTerm(String term) {
        currentTerm = term;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_left, AssessmentListFragment.newInstance(currentTerm));
        Fragment fragment = fragmentManager.findFragmentByTag(RIGHT_TAG);
        if(fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAssessmentList();

    }

    public void addAssessment() {
          NewAssessmentDialogFragment nadf = NewAssessmentDialogFragment.newInstance(currentTerm);
          FragmentManager fragmentManager = getSupportFragmentManager();
          nadf.show(fragmentManager, "dialog");
    }

    public void changeAssessment(Assessment assessment) {
        currentAssessment = assessment;
        //Log.d(TAG, "Swapping details fragment to student: " + student.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_right, AssessmentDetailsFragment.newInstance(assessment), RIGHT_TAG);
        fragmentTransaction.commit();
    }

    public void refreshAssessmentList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_left, AssessmentListFragment.newInstance(currentTerm));
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String name) {
        if(name.equals("save")) {
            refreshAssessmentList();
            refreshSpinner();
            //changeAssessment(currentAssessment);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        refreshAssessmentList();
        refreshSpinner();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        Log.d(TAG, "Cancelled add assessment.");
    }

    @Override
    public void onAssessmentClicked(View view, Assessment assessment) {
        currentAssessment = assessment;
        Log.d(TAG, "currentAssessment is " + currentAssessment.toString());
        changeAssessment(currentAssessment);

    }

    @Override
    public void onNewAssessmentDialogFragmentInteraction(String name) {

    }

    @Override
    public void onDelete() {
        refreshAssessmentList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(RIGHT_TAG);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();

        StudentQueries studentQueries = new StudentQueries(this);
        ArrayList<Student> students = studentQueries.getStudentsByTerm(currentTerm);

        for(Student s : students) {
            studentQueries.recalculateGradeForStudentAndTerm(s, currentTerm);
        }

    }
}

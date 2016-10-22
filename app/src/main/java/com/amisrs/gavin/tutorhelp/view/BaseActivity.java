package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.OnItemClickListener;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.db.WeekQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements StudentListFragment.OnFragmentInteractionListener,
        StudentWeekDetailsFragment.OnFragmentInteractionListener,NewStudentDialogFragment.OnFragmentInteractionListener,
        NewStudentDialogFragment.NewStudentDialogFragmentListener,
        OnItemClickListener {
    private static final String TAG = "BaseActivity";
    Tutorial tutorial;
    Spinner spinner;
    Student currentStudent;
    Week currentWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        tutorial = getIntent().getParcelableExtra("tutorial");


        TutorialQueries tutorialQueries = new TutorialQueries(this);
        ArrayList<Student> students = tutorialQueries.getStudentsForTutorial(tutorial);
        if(students.size() < 1) {
            NewStudentDialogFragment nsdf = NewStudentDialogFragment.newInstance(tutorial);
            FragmentManager fragmentManager = getSupportFragmentManager();
            nsdf.show(fragmentManager, "dialog");

        } else {
            currentStudent = students.get(0);

            ArrayList<Week> weeks = new ArrayList<>();
            WeekQueries weekQueries = new WeekQueries(this);
            weeks = weekQueries.getAllWeeksForTutorial(tutorial);

            spinner = (Spinner) findViewById(R.id.sp_week);
            ArrayAdapter<Week> arrayAdapter = new ArrayAdapter<Week>(this, android.R.layout.simple_spinner_item, weeks);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Week selectedWeek = (Week) adapterView.getItemAtPosition(i);
                    currentWeek = selectedWeek;
                    changeFragmentWeek(selectedWeek);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d(TAG, "Nothing selected.");
                }
            });

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rl_left, StudentListFragment.newInstance(tutorial));
            fragmentTransaction.replace(R.id.rl_right, StudentWeekDetailsFragment.newInstance(weeks.get(weeks.size() - 1), currentStudent, tutorial));
            fragmentTransaction.commit();

        }
    }

    public void changeFragmentWeek(Week week) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_right, StudentWeekDetailsFragment.newInstance(week,currentStudent,tutorial));
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra("tutorial", tutorial);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {

    }

    @Override
    public void onFragmentInteraction(String string) {
        if(string.equals("save")) {
            Log.d(TAG, "save was pressed");
            changeFragmentWeek(currentWeek);
        }
    }

    @Override
    public void onStudentClick(View view, Student student) {
        currentStudent = student;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_right, StudentWeekDetailsFragment.newInstance(currentWeek,currentStudent,tutorial));
        fragmentTransaction.commit();
    }
}

package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;
import com.amisrs.gavin.tutorhelp.view.NavDrawer.DrawerActivity;

import java.util.ArrayList;

public class BaseActivity extends DrawerActivity implements StudentListFragment.OnFragmentInteractionListener,
        StudentWeekDetailsFragment.OnFragmentInteractionListener,
        OnItemClickListener {
    private static final String TAG = "BaseActivity";
    private static final String overrideKey = "override";
    Tutor tutor;
    Tutorial tutorial;
    Spinner spinner;
    Student currentStudent;
    Week currentWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        tutor = getIntent().getParcelableExtra("tutor");
        tutorial = getIntent().getParcelableExtra("tutorial");
        setToolbarTitle(getString(R.string.attendance));

        TutorialQueries tutorialQueries = new TutorialQueries(this);
        ArrayList<Student> students = tutorialQueries.getStudentsForTutorial(tutorial);
        if(students.size() < 1) {
            Toast.makeText(BaseActivity.this, getString(R.string.nostudents), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, StudentsActivity.class);
            //savedInstanceState.putString("fromAttendance");
            intent.putExtra("fromAttendance", overrideKey);
            intent.putExtra("tutor", tutor);
            intent.putExtra("tutorial", tutorial);
            startActivity(intent);
        } else {
            currentStudent = students.get(0);

            ArrayList<Week> weeks = new ArrayList<>();
            WeekQueries weekQueries = new WeekQueries(this);
            weeks = weekQueries.getAllWeeksForTutorial(tutorial);

            spinner = (Spinner) findViewById(R.id.sp_week);
            spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            ArrayAdapter<Week> arrayAdapter = new ArrayAdapter<Week>(this, R.layout.coolspinner, weeks);
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

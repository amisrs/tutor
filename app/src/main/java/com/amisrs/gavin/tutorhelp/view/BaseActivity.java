package com.amisrs.gavin.tutorhelp.view;

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

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.WeekQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements StudentListFragment.OnFragmentInteractionListener, StudentWeekDetailsFragment.OnFragmentInteractionListener {
    private static final String TAG = "BaseActivity";
    Tutorial tutorial;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        tutorial = getIntent().getParcelableExtra("tutorial");
        Person fakePerson = new Person(1, "test", "testlname", 2222);
        Student fakeStudent = new Student(1,1,fakePerson);

        ArrayList<Week> weeks = new ArrayList<>();
        WeekQueries weekQueries = new WeekQueries(this);
        weeks = weekQueries.getAllWeeksForTutorial(tutorial);

        spinner = (Spinner)findViewById(R.id.sp_week);
        ArrayAdapter<Week> arrayAdapter = new ArrayAdapter<Week>(this, android.R.layout.simple_spinner_item, weeks);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Week selectedWeek = (Week)adapterView.getItemAtPosition(i);
                changeFragmentWeek(selectedWeek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "Nothing selected.");
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rl_left, StudentListFragment.newInstance(tutorial));
        fragmentTransaction.add(R.id.rl_right, StudentWeekDetailsFragment.newInstance(weeks.get(weeks.size()-1),fakeStudent,tutorial));
        fragmentTransaction.commit();
    }

    public void changeFragmentWeek(Week week) {
        Person fakePerson = new Person(1, "test", "testlname", 2222);
        Student fakeStudent = new Student(1,1,fakePerson);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_right, StudentWeekDetailsFragment.newInstance(week,fakeStudent,tutorial));
        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

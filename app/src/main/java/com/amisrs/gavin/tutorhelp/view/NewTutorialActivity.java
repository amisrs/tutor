package com.amisrs.gavin.tutorhelp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.db.WeekQueries;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;
import java.util.Calendar;

public class NewTutorialActivity extends AppCompatActivity {
    private static final String TAG = "NewTutorialActivity";

    Tutor tutor;
    Button createButton;
    EditText name;
    EditText timeSlot;
    EditText location;
    Spinner semSp;
    Spinner yearSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tutorial);
        tutor = getIntent().getParcelableExtra("tutor");

        name = (EditText)findViewById(R.id.et_name);
        timeSlot = (EditText)findViewById(R.id.et_timeSlot);
        location = (EditText)findViewById(R.id.et_location);
        semSp = (Spinner)findViewById(R.id.sp_sem);
        yearSp = (Spinner)findViewById(R.id.sp_year);

        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=currentYear; i<currentYear+5; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yearSp.setAdapter(yearAdapter);

        ArrayList<String> sems = new ArrayList<>();
        sems.add(getString(R.string.S1));
        sems.add(getString(R.string.S2));
        sems.add(getString(R.string.ST));
        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sems);
        semSp.setAdapter(semAdapter);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTutorial();
            }
        });
    }

    public void createTutorial() {
        TutorialQueries tutorialQueries = new TutorialQueries(this);
        String termString = semSp.getSelectedItem().toString() + " " + yearSp.getSelectedItem().toString();

        Tutorial newTutorial = new Tutorial(tutor.getTutorID(),
                name.getText().toString(),
                timeSlot.getText().toString(),
                location.getText().toString(),
                termString);
        int tutorialID = (int)tutorialQueries.addTutorial(newTutorial);
        WeekQueries weekQueries = new WeekQueries(this);
        for(int i=1; i<Tutorial.MAX_WEEKS+1; i++) {
            Week newWeek = new Week(tutorialID, i, "");
            weekQueries.addWeek(newWeek);
            Log.d(TAG, "Added week " + i + " of 13 for tutorial " + newTutorial.getName());
        }

        finish();
    }
}

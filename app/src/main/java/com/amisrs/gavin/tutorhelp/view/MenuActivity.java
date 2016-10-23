package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.Assessment.AssessmentsActivity;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    //TODO: decide what happens with this activity (get rid of it?)
    private static final String TAG = "MenuActivity";
    TextView nameTextView;
    Button attendanceButton;
    Button studentsButton;
    Button assessmentsButton;
    Tutorial tutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent fromIntent = getIntent();
        tutorial = fromIntent.getParcelableExtra("tutorial");


        attendanceButton = (Button)findViewById(R.id.btn_attendance);
        studentsButton = (Button)findViewById(R.id.btn_students);
        assessmentsButton = (Button)findViewById(R.id.btn_assessments);

        nameTextView = (TextView)findViewById(R.id.tv_name);

        //nameTextView.setText(tutorial.getName());
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAttendance();
            }
        });
        studentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStudents();
            }
        });
        assessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAssessments();
            }
        });
    }

    public void goToAttendance() {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra("tutorial", tutorial);
        Log.d(TAG, "Put extra tutorial: " + tutorial.getName());
        startActivity(intent);
    }

    public void goToStudents() {
        Intent intent = new Intent(this, StudentsActivity.class);
        intent.putExtra("tutorial", tutorial);
        startActivity(intent);
    }

    public void goToAssessments() {
        Intent intent = new Intent(this, AssessmentsActivity.class);
        intent.putExtra("term", tutorial.getTerm());
        Log.d(TAG, "going to assessments for term " + tutorial.getTerm());
        startActivity(intent);
    }
}

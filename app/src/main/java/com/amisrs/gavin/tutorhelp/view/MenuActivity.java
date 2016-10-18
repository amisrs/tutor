package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

public class MenuActivity extends AppCompatActivity {
    //TODO: decide what happens with this activity (get rid of it?)
    private static final String TAG = "MenuActivity";
    TextView nameTextView;
    Button attendanceButton;
    Button studentsButton;
    Tutorial tutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent fromIntent = getIntent();
        tutorial = fromIntent.getParcelableExtra("tutorial");


        attendanceButton = (Button)findViewById(R.id.btn_attendance);
        studentsButton = (Button)findViewById(R.id.btn_students);
        nameTextView = (TextView)findViewById(R.id.tv_name);

        nameTextView.setText(tutorial.getName());
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
}

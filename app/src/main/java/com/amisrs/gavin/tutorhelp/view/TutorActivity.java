package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.amisrs.gavin.tutorhelp.R;

import java.util.ArrayList;

public class TutorActivity extends AppCompatActivity {
    private static final String TAG = "TutorActivity";
    Spinner tutorSpinner;
    Button loginButton;
    Button newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        tutorSpinner = (Spinner)findViewById(R.id.sp_tutor);
        loginButton = (Button)findViewById(R.id.btn_login);
        newButton = (Button)findViewById(R.id.btn_newTutor);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTutor();
            }
        });

        //might use EditText + password
        ArrayList<Integer> tutorList = new ArrayList<>();
        tutorList.add(1234567);
        tutorList.add(9876543);

        ArrayAdapter<Integer> tutorSpinnerAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, tutorList);
        tutorSpinner.setAdapter(tutorSpinnerAdapter);
    }

    public void login() {
        int tutorZID = Integer.parseInt(tutorSpinner.getSelectedItem().toString());
        Log.d(TAG, "Login button pressed. Logging in with: " + tutorZID);

        //go to TutorialListActivity with the tutorID
        Intent intent = new Intent(this, TutorialListActivity.class);
        intent.putExtra("zid", tutorZID);
        startActivity(intent);
    }

    public void createNewTutor() {
        Intent intent = new Intent(this, NewTutorActivity.class);
        startActivity(intent);
    }
}

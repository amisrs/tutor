package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Tutor;

import java.util.ArrayList;

public class TutorActivity extends AppCompatActivity implements NewTutorDialogFragment.OnFragmentInteractionListener,
        NewTutorDialogFragment.NewTutorDialogFragmentListener {
    //TODO: handle login as null
    private static final String TAG = "TutorActivity";
    Spinner tutorSpinner;
    Button loginButton;
    Button newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);


        tutorSpinner = (Spinner) findViewById(R.id.sp_tutor);
        loginButton = (Button) findViewById(R.id.btn_login);
        newButton = (Button) findViewById(R.id.btn_newTutor);

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

        //get arraylist of tutors from db

        //test tutor
//        Person testperson = new Person("Gavin", "Chiem", 5062206);
//        Tutor test = new Tutor(5,testperson);
//        tutorList.add(test);
        refreshTutorSpinner();
    }

    public void login() {
        Tutor tutor = (Tutor) tutorSpinner.getSelectedItem();

        Log.d(TAG, "Login button pressed. Logging in with tutor: " + tutor.getTutorID());

        //go to TutorialListActivity with the tutorID
        Intent intent = new Intent(this, TutorialListActivity.class);
        intent.putExtra("tutor", tutor);
        startActivity(intent);
    }

    public void createNewTutor() {
        /*Intent intent = new Intent(this, NewTutorActivity.class);
        startActivity(intent);*/
//TODO: currently working on this
        NewTutorDialogFragment ntdf = NewTutorDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        ntdf.show(fragmentManager, "dialog");
    }


    public void refreshTutorSpinner() {
        TutorQueries tutorQueries = new TutorQueries(this);
        ArrayList<Tutor> tutorList = tutorQueries.getTutorList();
        ArrayAdapter<Tutor> tutorSpinnerAdapter = new ArrayAdapter<Tutor>(this, android.R.layout.simple_spinner_item, tutorList);
        tutorSpinner.setAdapter(tutorSpinnerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTutorSpinner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    

    @Override
    public void onFragmentInteraction(String name) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        refreshTutorSpinner();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        Log.d(TAG, "Cancelled add student.");
    }

}

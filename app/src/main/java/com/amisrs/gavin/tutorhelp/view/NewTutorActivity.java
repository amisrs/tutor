package com.amisrs.gavin.tutorhelp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.TutorQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Tutor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewTutorActivity extends AppCompatActivity {
    private static final String TAG = "NewTutorActivity";

    Button createButton;
    EditText zid;
    EditText fname;
    EditText lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tutor);

        createButton = (Button)findViewById(R.id.btn_create);
        zid = (EditText)findViewById(R.id.et_zid);
        fname = (EditText)findViewById(R.id.et_fname);
        lname = (EditText)findViewById(R.id.et_lname);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTutor();
            }
        });
    }

    public void createTutor() {
        //input validation
        //make sure zID has no 'z'
        //TODO: improve validation, handle null
        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if(zidMatcher.find()) {
            int zidInt = Integer.parseInt(zid.getText().toString());
            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();

            PersonQueries personQueries = new PersonQueries(this);
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt));

            TutorQueries tutorQueries = new TutorQueries(this);
            tutorQueries.addTutor(new Tutor(addedPerson.getPersonID(), addedPerson));

            Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
            //add stuff to database
            finish();
        } else {
            //the zid has non-digits in it
            zid.setError("zID must only contain numbers.");
        }


    }
}
package com.amisrs.gavin.tutorhelp.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewStudentActivity extends Activity {
    private static final String TAG = "NewStudentActivity";

    Button createButton;
    EditText zid;
    EditText fname;
    EditText lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);

        createButton = (Button)findViewById(R.id.btn_create);
        zid = (EditText)findViewById(R.id.student_zid);
        fname = (EditText)findViewById(R.id.student_fname);
        lname = (EditText)findViewById(R.id.student_lname);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStudent();
            }
        });
    }
    public void createStudent() {
        //input validation
        //make sure zID has no 'z'
        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if(zidMatcher.find()) {
            int zidInt = Integer.parseInt(zid.getText().toString());
            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();

            PersonQueries personQueries = new PersonQueries(this);
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt));

            StudentQueries studentQueries = new StudentQueries(this);
            //studentQueries.addStudent(new Student(addedPerson.getPersonID(), addedPerson));

            Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
            //add stuff to database
            finish();

//            Intent intent = new Intent(this, StudentListActivity.class);
//            startActivity(intent);

        } else {
            //the zid has non-digits in it
            zid.setError("zID must only contain numbers.");
        }


    }
}

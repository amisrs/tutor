package com.amisrs.gavin.tutorhelp.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;

import com.amisrs.gavin.tutorhelp.db.DBHelper;
import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttendanceActivity extends AppCompatActivity implements TableFragment.OnFragmentInteractionListener,
                                                                    NewStudentDialogFragment.OnFragmentInteractionListener,
                                                                    NewStudentDialogFragment.NewStudentDialogFragmentListener {
    private static final String TAG = "AttendanceActivity";
    Tutorial tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_attendance);
        Intent fromIntent = getIntent();
        tutorial = fromIntent.getParcelableExtra("tutorial");
        Log.d(TAG, "Got tutorial from intent: " + tutorial.getName());

        refreshTable();

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onFragmentInteraction(String name) {

    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment fragment) {
        Dialog dialog  = fragment.getDialog();
        EditText zid = (EditText)dialog.findViewById(R.id.student_zid);
        EditText fname = (EditText)dialog.findViewById(R.id.student_fname);
        EditText lname = (EditText)dialog.findViewById(R.id.student_lname);

        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if(zidMatcher.find()) {


            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();
            int zidInt = Integer.parseInt(zid.getText().toString());

            PersonQueries personQueries = new PersonQueries(this);
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt));

            StudentQueries studentQueries = new StudentQueries(this);
            studentQueries.addStudent(new Student(addedPerson.getPersonID(), addedPerson), tutorial);

            Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
            refreshTable();

        } else {
            //the zid has non-digits in it
            zid.setError("zID must only contain numbers.");
        }
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment fragment) {
        Log.d(TAG, "Cancelled add student.");

    }

    public void refreshTable() {
        Fragment tableFragment = TableFragment.newInstance(tutorial);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rl_main, tableFragment);
        fragmentTransaction.commit();

    }

    public Tutorial getTutorial() {
        return tutorial;
    }
}

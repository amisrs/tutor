package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.ArrayList;

/**
 * Created by Gavin on 15/10/2016.
 */
public class TutorialQueries extends QueryBase {
    private static final String TAG = "TutorialQueries";
    private static final String COMMA_SEP = ", ";

    public TutorialQueries(Context context) {
        super(context);
    }

    public ArrayList<Tutorial> getTutorialListForTutor(Tutor tutor) {
        open();
        ArrayList<Tutorial> tutorials = new ArrayList<>();
        String[] projection = {
                DBContract.TutorialTable.COLUMN_TUTORIALID + COMMA_SEP +
                        DBContract.TutorialTable.COLUMN_TUTORID + COMMA_SEP +
                        DBContract.TutorialTable.COLUMN_NAME + COMMA_SEP +
                        DBContract.TutorialTable.COLUMN_TIMESLOT + COMMA_SEP +
                        DBContract.TutorialTable.COLUMN_LOCATION
        };

        //get tutor by zid


        String selection = DBContract.TutorialTable.COLUMN_TUTORID + " = ?";
        String[] selectionArgs = {String.valueOf(tutor.getTutorID())};

        Cursor c = db.query(
                DBContract.TutorialTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Tutorial newTutorial = new Tutorial(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4));
            tutorials.add(newTutorial);
            c.moveToNext();
        }
        c.close();
        close();
        return tutorials;
    }

    public long addTutorial(Tutorial tutorial) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TutorialTable.COLUMN_TUTORID, tutorial.getTutorID());
        contentValues.put(DBContract.TutorialTable.COLUMN_NAME, tutorial.getName());
        contentValues.put(DBContract.TutorialTable.COLUMN_TIMESLOT, tutorial.getTimeSlot());
        contentValues.put(DBContract.TutorialTable.COLUMN_LOCATION, tutorial.getLocation());

        long newRowId = db.insert(DBContract.TutorialTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Added new Tutorial: " + tutorial.getTutorID() + " " + tutorial.getName() + tutorial.getTimeSlot() + tutorial.getLocation());
        close();

        return newRowId;
    }

    public ArrayList<Student> getStudentsForTutorial(Tutorial tutorial) {
        open();
        ArrayList<Student> students = new ArrayList<>();

        String query = "select s." + DBContract.StudentTable.COLUMN_PERSONID + COMMA_SEP +
                              "s." + DBContract.StudentTable.COLUMN_STUDENTID + COMMA_SEP +
                              "p." + DBContract.PersonTable.COLUMN_FIRSTNAME + COMMA_SEP +
                              "p." + DBContract.PersonTable.COLUMN_LASTNAME + COMMA_SEP +
                              "p." + DBContract.PersonTable.COLUMN_ZID + COMMA_SEP +
                              "p." + DBContract.PersonTable.COLUMN_PROFILEPIC + COMMA_SEP +
                              "p." + DBContract.PersonTable.COLUMN_EMAIL +
                " from " + DBContract.PersonTable.TABLE_NAME + " p" +
                " join " + DBContract.StudentTable.TABLE_NAME + " s" +
                " on s." + DBContract.StudentTable.COLUMN_PERSONID + " = " +
                "p." + DBContract.PersonTable.COLUMN_PERSONID +
                " join " + DBContract.EnrolmentTable.TABLE_NAME + " e" +
                " on s." + DBContract.StudentTable.COLUMN_STUDENTID + " = " +
                "e." + DBContract.EnrolmentTable.COLUMN_STUDENTID +
                " where " + DBContract.EnrolmentTable.COLUMN_TUTORIALID + " = ?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(tutorial.getTutorialID())});
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Person newPerson = new Person(c.getInt(0), c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
            Student newStudent = new Student(c.getInt(1), c.getInt(0), newPerson);
            students.add(newStudent);
            c.moveToNext();
        }
        c.close();
        close();
        return students;
    }

}

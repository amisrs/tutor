package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.Mark;
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
                        DBContract.TutorialTable.COLUMN_LOCATION + COMMA_SEP +
                        DBContract.TutorialTable.COLUMN_TERM
        };

        //get tutor by zid


        String selection = DBContract.TutorialTable.COLUMN_TUTORID + " = ?";
        String[] selectionArgs = { String.valueOf(tutor.getTutorID()) };

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
        while(!c.isAfterLast()) {
            Tutorial newTutorial = new Tutorial(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
            tutorials.add(newTutorial);
            Log.d(TAG, "tutorial in term: " + c.getString(5));
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
        contentValues.put(DBContract.TutorialTable.COLUMN_TERM, tutorial.getTerm());

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

        Cursor c = db.rawQuery(query, new String[] { String.valueOf(tutorial.getTutorialID()) });
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Person newPerson = new Person(c.getInt(0),c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
            Student newStudent = new Student(c.getInt(1), c.getInt(0), newPerson);
            students.add(newStudent);
            c.moveToNext();
        }
        c.close();
        close();
        return students;
    }

    public ArrayList<Tutorial> getTutorialsForStudent(Student student) {
        open();
        String query =
                "select t." + DBContract.TutorialTable.COLUMN_TUTORIALID + COMMA_SEP +
                        "t." + DBContract.TutorialTable.COLUMN_TUTORID + COMMA_SEP +
                        "t." + DBContract.TutorialTable.COLUMN_NAME + COMMA_SEP +
                        "t." + DBContract.TutorialTable.COLUMN_TIMESLOT + COMMA_SEP +
                        "t." + DBContract.TutorialTable.COLUMN_LOCATION + COMMA_SEP +
                        "t." + DBContract.TutorialTable.COLUMN_TERM +
                        " from " + DBContract.TutorialTable.TABLE_NAME + " t" +
                        " join " + DBContract.EnrolmentTable.TABLE_NAME + " e" +
                        " on t." + DBContract.TutorialTable.COLUMN_TUTORIALID + " = e." + DBContract.EnrolmentTable.COLUMN_TUTORIALID +
                        " join " + DBContract.StudentTable.TABLE_NAME + " s" +
                        " on e." + DBContract.StudentTable.COLUMN_STUDENTID + " = s." + DBContract.EnrolmentTable.COLUMN_STUDENTID +
                        " where e." + DBContract.EnrolmentTable.COLUMN_STUDENTID + " = ?";
        String[] selectionArgs = { String.valueOf(student.getStudentID()) };

        Cursor c = db.rawQuery(query, selectionArgs);
        ArrayList<Tutorial> tutorials = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Tutorial newTutorial = new Tutorial(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
            tutorials.add(newTutorial);
            c.moveToNext();
        }
        c.close();
        close();
        return tutorials;
    }

    public ArrayList<Tutorial> getTutorialsForTerm(String term) {
        open();
        String[] projection = {
                DBContract.TutorialTable.COLUMN_TUTORIALID,
                DBContract.TutorialTable.COLUMN_TUTORID,
                DBContract.TutorialTable.COLUMN_NAME,
                DBContract.TutorialTable.COLUMN_TIMESLOT,
                DBContract.TutorialTable.COLUMN_LOCATION,
                DBContract.TutorialTable.COLUMN_TERM
        };

        String selection = DBContract.TutorialTable.COLUMN_TERM + " = ?";
        String[] selectionArgs = { term };

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
        ArrayList<Tutorial> tutorials = new ArrayList<>();
        while(!c.isAfterLast()) {
            tutorials.add(new Tutorial(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            c.moveToNext();
        }
        c.close();
        close();

        return tutorials;
    }

    public void deleteTutorial(Tutorial tutorial) {
        open();
        String whereClause = DBContract.TutorialTable.COLUMN_TUTORIALID + " = ?";
        String[] whereArgs = { String.valueOf(tutorial.getTutorialID()) };
        db.delete(
                DBContract.TutorialTable.TABLE_NAME,
                whereClause,
                whereArgs
        );
        close();
        Log.d(TAG, "Deleted tutorial: " + tutorial.getName());
    }


    public void updateTutorial(Tutorial tutorial) {
        open();
        String update = "update " + DBContract.TutorialTable.TABLE_NAME + " set " +
                DBContract.TutorialTable.COLUMN_NAME + " = \"" + tutorial.getName() + "\"" + COMMA_SEP +
                DBContract.TutorialTable.COLUMN_TIMESLOT + " = \"" + tutorial.getTimeSlot() + "\"" + COMMA_SEP +
                DBContract.TutorialTable.COLUMN_LOCATION + " = \"" + tutorial.getLocation() + "\"" +
                " where " + DBContract.TutorialTable.COLUMN_TUTORIALID + " = " + tutorial.getTutorialID();
        db.execSQL(update);
        Log.d(TAG, "Updated tutorial, new: " + tutorial.getName());
        close();
    }

    public ArrayList<Enrolment> getEnrolmentsForTutorial(Tutorial tutorial) {
        open();
        String[] projection = {
                DBContract.EnrolmentTable.COLUMN_STUDENTID,
                DBContract.EnrolmentTable.COLUMN_TUTORIALID,
                DBContract.EnrolmentTable.COLUMN_GRADE
        };
        String selection = DBContract.EnrolmentTable.COLUMN_TUTORIALID + " = ?";
        String[] selectionArgs = { String.valueOf(tutorial.getTutorialID()) };

        Cursor c = db.query(
                DBContract.EnrolmentTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        ArrayList<Enrolment> enrolments = new ArrayList<>();
        while(!c.isAfterLast()){
            enrolments.add(new Enrolment(c.getInt(0), c.getInt(1), c.getDouble(2)));
            c.moveToNext();
        }

        c.close();
        close();

        return enrolments;
    }
}
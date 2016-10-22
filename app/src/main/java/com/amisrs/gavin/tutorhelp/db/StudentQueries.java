package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;

/**
 * Created by karenhuang on 16/10/16.
 */

public class StudentQueries extends QueryBase {
    private static final String TAG = "StudentQueries";

    public StudentQueries(Context context) {
        super(context);
    }

    public void addStudent(Student student, Tutorial tutorial) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.StudentTable.COLUMN_PERSONID, student.getPersonID());

        long newRowId = db.insert(DBContract.StudentTable.TABLE_NAME, null, contentValues);

        Log.d(TAG, "Inserted new student to database: " + student.getStudentID() + " rowId = " + newRowId);

        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(DBContract.EnrolmentTable.COLUMN_STUDENTID, (int)newRowId);
        contentValues1.put(DBContract.EnrolmentTable.COLUMN_TUTORIALID, tutorial.getTutorialID());

        long newRowId1 = db.insert(DBContract.EnrolmentTable.TABLE_NAME, null, contentValues1);

        Log.d(TAG, "Inserted enrolment for that student to tutorial " + tutorial.getName());

        WeekQueries weekQueries = new WeekQueries(context);
        ArrayList<Week> weeks = weekQueries.getAllWeeksForTutorial(tutorial);
        Log.d(TAG, "Starting to create StudentWeek for number of weeks: " + weeks.size());

        for(Week w : weeks) {
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put(DBContract.StudentWeekTable.COLUMN_STUDENTID, (int) newRowId);
            contentValues2.put(DBContract.StudentWeekTable.COLUMN_WEEKID, w.getWeekID());
            contentValues2.put(DBContract.StudentWeekTable.COLUMN_ATTENDED, 1);
            contentValues2.put(DBContract.StudentWeekTable.COLUMN_PRIVATECOMMENT, "");
            contentValues2.put(DBContract.StudentWeekTable.COLUMN_PUBLICCOMMENT, "");
            long newRowId2 = db.insert(DBContract.StudentWeekTable.TABLE_NAME, null, contentValues2);
            Log.d(TAG, "Inserted new StudentWeek for student " + student.toString() + " for week " + w.getWeekNumber());
        }
        close();
    }


    public ArrayList<Student> getStudentList() {
        open();
        ArrayList<Student> students = new ArrayList<>();
        String[] projection = {
                DBContract.StudentTable.COLUMN_STUDENTID,
                DBContract.StudentTable.COLUMN_PERSONID
        };

        Cursor c = db.query(
                DBContract.StudentTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        c.moveToFirst();
        PersonQueries personQueries = new PersonQueries(getContext());
        while(!c.isAfterLast() && c != null) {
            Log.d(TAG, "Getting Person for StudentID: " + c.getInt(1));
            Person newPerson = personQueries.getPersonByID(c.getInt(1));
            Log.d(TAG, "Person is: " + newPerson.getFirstName() + " " + newPerson.getLastName());
            Student newStudent = new Student(c.getInt(0), c.getInt(1), newPerson);
            students.add(newStudent);
            Log.d(TAG, "Populating Student list: " + newStudent.getPerson().getFirstName());
            c.moveToNext();
        }
        c.close();
        close();
        return students;
    }

    public Enrolment getEnrolmentForStudentAndTutorial(Student student, Tutorial tutorial) {
        open();
        String[] projection = {
                DBContract.EnrolmentTable.COLUMN_STUDENTID,
                DBContract.EnrolmentTable.COLUMN_TUTORIALID,
                DBContract.EnrolmentTable.COLUMN_GRADE
        };

        String selection = DBContract.EnrolmentTable.COLUMN_STUDENTID + " = ? AND " +
                DBContract.EnrolmentTable.COLUMN_TUTORIALID + " = ?";
        String[] selectionArgs = {
                String.valueOf(student.getStudentID()),
                String.valueOf(tutorial.getTutorialID())
        };

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
        Enrolment enrolment = new Enrolment(c.getInt(0), c.getInt(1), c.getInt(2));
        c.close();
        close();

        return enrolment;
    }
}

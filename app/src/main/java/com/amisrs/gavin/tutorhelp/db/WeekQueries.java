package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Gavin on 17/10/2016.
 */
public class WeekQueries extends QueryBase {
    private static final String TAG = "WeekQueries";

    public WeekQueries(Context context) {
        super(context);
    }

    public void addWeek(Week week) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.WeekTable.COLUMN_TUTORIALID, week.getTutorialID());
        contentValues.put(DBContract.WeekTable.COLUMN_WEEKNUMBER, week.getWeekNumber());
        contentValues.put(DBContract.WeekTable.COLUMN_DESCRIPTION, week.getDescription());

        long newRowId = db.insert(DBContract.WeekTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Inserted new week into database: " + week.getWeekNumber() + " " + week.getDescription());
        close();
    }

    public ArrayList<Week> getAllWeeksForTutorial(Tutorial tutorial) {
        open();
        Log.d(TAG, "Getting weeks for tutorial: " + tutorial.getName());
        ArrayList<Week> weeks = new ArrayList<>();
        String[] projection = {
                DBContract.WeekTable.COLUMN_WEEKID,
                DBContract.WeekTable.COLUMN_TUTORIALID,
                DBContract.WeekTable.COLUMN_WEEKNUMBER,
                DBContract.WeekTable.COLUMN_DESCRIPTION
        };
        String selection = DBContract.WeekTable.COLUMN_TUTORIALID + " = ?";
        String[] selectionArgs = { String.valueOf(tutorial.getTutorialID())};

        Cursor c = db.query(
                DBContract.WeekTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        c.moveToFirst();
        while(!c.isAfterLast()) {
            Week newWeek = new Week(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3));
            weeks.add(newWeek);
            Log.d(TAG, "Found week: " + newWeek.getWeekNumber());
            c.moveToNext();
        }
        Log.d(TAG, "Returning list of weeks: " + weeks.size());
        c.close();
        close();

        return weeks;
    }

    public StudentWeek getStudentWeekForWeekAndStudentAndTutorial(Week week, Student student, Tutorial tutorial) {
        open();
        String query = "select sw." + DBContract.StudentWeekTable.COLUMN_STUDENTID + ", " +
                              "sw." + DBContract.StudentWeekTable.COLUMN_WEEKID + ", " +
                              "sw." + DBContract.StudentWeekTable.COLUMN_ATTENDED + ", " +
                              "sw." + DBContract.StudentWeekTable.COLUMN_PRIVATECOMMENT + ", " +
                              "sw." + DBContract.StudentWeekTable.COLUMN_PUBLICCOMMENT +
                        " from " + DBContract.StudentWeekTable.TABLE_NAME + " sw" +
                        " join " + DBContract.WeekTable.TABLE_NAME + " w" +
                        " on w."+ DBContract.WeekTable.COLUMN_WEEKID + " = sw." + DBContract.StudentWeekTable.COLUMN_WEEKID +
                        " where w." + DBContract.WeekTable.COLUMN_TUTORIALID + " = ?" +
                        " and sw." + DBContract.StudentWeekTable.COLUMN_STUDENTID + " = ?" +
                        " and sw." + DBContract.StudentWeekTable.COLUMN_WEEKID + " = ?";

        String[] selectionArgs = { String.valueOf(tutorial.getTutorialID()),
                                   String.valueOf(student.getStudentID()),
                                   String.valueOf(week.getWeekID()) };

        Log.d(TAG, "Getting StudentWeek for: " + student.toString() + " week " + week.getWeekNumber() + " tutorial " + tutorial.getName());
        Log.d(TAG, "args: " + tutorial.getTutorialID() + ", " + student.getStudentID() + ", " + week.getWeekID());

        Cursor c = db.rawQuery(query, selectionArgs);
        c.moveToFirst();

        StudentWeek retval = new StudentWeek(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4));
        c.close();
        close();
        return retval;

    }

    //TODO: update studentweek
}

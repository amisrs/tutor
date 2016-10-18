package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;

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
            c.moveToNext();
        }
        c.close();
        close();

        return weeks;
    }
}

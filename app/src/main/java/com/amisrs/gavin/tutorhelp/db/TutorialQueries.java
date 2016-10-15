package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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
            Tutorial newTutorial = new Tutorial(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4));
            tutorials.add(newTutorial);
            c.moveToNext();
        }
        c.close();
        close();
        return tutorials;
    }

    public void addTutorial(Tutorial tutorial) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TutorialTable.COLUMN_TUTORID, tutorial.getTutorID());
        contentValues.put(DBContract.TutorialTable.COLUMN_NAME, tutorial.getName());
        contentValues.put(DBContract.TutorialTable.COLUMN_TIMESLOT, tutorial.getTimeSlot());
        contentValues.put(DBContract.TutorialTable.COLUMN_LOCATION, tutorial.getLocation());

        long newRowId = db.insert(DBContract.TutorialTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Added new Tutorial: " + tutorial.getTutorID() + " " + tutorial.getName() + tutorial.getTimeSlot() + tutorial.getLocation());
        close();
    }

}

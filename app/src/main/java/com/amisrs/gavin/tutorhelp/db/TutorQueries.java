package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Tutor;

import java.util.ArrayList;

/**
 * Created by Gavin on 15/10/2016.
 */
public class TutorQueries extends QueryBase {
    private static final String TAG = "TutorQueries";

    public TutorQueries(Context context) {
        super(context);
    }

    public void addTutor(Tutor tutor) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TutorTable.COLUMN_PERSONID, tutor.getPersonID());

        long newRowId = db.insert(DBContract.TutorTable.TABLE_NAME, null, contentValues);

        Log.d(TAG, "Inserted new tutor to database: " + tutor.getTutorID() + " rowId = " + newRowId);
        close();
    }

    public ArrayList<Tutor> getTutorList() {
        open();
        ArrayList<Tutor> tutors = new ArrayList<>();
        String[] projection = {
                DBContract.TutorTable.COLUMN_TUTORID,
                DBContract.TutorTable.COLUMN_PERSONID
        };

        Cursor c = db.query(
                DBContract.TutorTable.TABLE_NAME,
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
            Log.d(TAG, "Getting Person for TutorID: " + c.getInt(1));
            Person newPerson = personQueries.getPersonByID(c.getInt(1));
            Log.d(TAG, "Person is: " + newPerson.getFirstName() + " " + newPerson.getLastName());
            Tutor newTutor = new Tutor(c.getInt(0), c.getInt(1), newPerson);
            tutors.add(newTutor);
            Log.d(TAG, "Populating tutor list: " + newTutor.getPerson().getFirstName());
            c.moveToNext();
        }

        return tutors;
    }

//    public Tutor getTutorByZID(int zid) {
//        Tutor toGet = new Tutor();
//
//        return toGet;
//    }
}

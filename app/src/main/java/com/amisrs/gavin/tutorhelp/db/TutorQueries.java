package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;

import java.util.ArrayList;

/**
 * Created by Gavin on 15/10/2016.
 */
public class TutorQueries extends QueryBase {
    private static final String TAG = "TutorQueries";
    private static final String COMMA_SEP = ", ";

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
        c.close();
        close();
        return tutors;

    }

    public void deleteTutor(Tutor tutor) {
        open();
        PersonQueries personQueries = new PersonQueries(context);
        Person toDelete = personQueries.getPersonByID(tutor.getPersonID());

        Log.d(TAG, "Person profilepath is " + toDelete.getProfilePath());
        //technically could just delete zID_tutor.PNG but don't be lazy
        if(!toDelete.getProfilePath().equals("default.png") && !toDelete.getProfilePath().equals("")) {
            int folderIndex = toDelete.getProfilePath().indexOf("files");
            String filePathPenultimate = toDelete.getProfilePath().substring(folderIndex);
            int fileIndex = filePathPenultimate.indexOf("/");
            String fileName = filePathPenultimate.substring(fileIndex + 1);

            context.deleteFile(fileName);
        }

        String whereClause = DBContract.PersonTable.COLUMN_PERSONID + " = ?";
        String[] whereArgs = { String.valueOf(tutor.getPersonID()) };
        db.delete(
                DBContract.PersonTable.TABLE_NAME,
                whereClause,
                whereArgs
        );
        close();
        Log.d(TAG, "Deleted person: " + tutor.getPerson().getFirstName());

    }

    public Tutor getTutorById(int id) {
        open();
        String query = "select t." + DBContract.TutorTable.COLUMN_PERSONID + COMMA_SEP +
                "t." + DBContract.TutorTable.COLUMN_TUTORID + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_FIRSTNAME + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_LASTNAME + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_ZID + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_PROFILEPIC + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_EMAIL +
                " from " + DBContract.PersonTable.TABLE_NAME + " p" +
                " join " + DBContract.TutorTable.TABLE_NAME + " t" +
                " on s." + DBContract.TutorTable.COLUMN_PERSONID + " = " +
                "p." + DBContract.PersonTable.COLUMN_PERSONID +
                " where " + DBContract.TutorTable.COLUMN_TUTORID + " = ?";

        Cursor c = db.rawQuery(query, new String[] { String.valueOf(id) });
        c.moveToFirst();
        Person newPerson = new Person(c.getInt(0),c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
        Tutor newTutor = new Tutor(c.getInt(1), c.getInt(0), newPerson);
        c.close();
        close();
        return newTutor;
    }

//    public Tutor getTutorByZID(int zid) {
//        Tutor toGet = new Tutor();
//
//        return toGet;
//    }
}

package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.other.DbBitmapUtility;

/**
 * Created by Gavin on 15/10/2016.
 */
public class PersonQueries extends QueryBase {
    private static final String TAG = "PersonQueries";
    private static final String COMMA_SEP = ",";
    public PersonQueries(Context context) {
        super(context);
    }

    public Person getPersonByID(int personID) {
        open();
        String[] projection = {
                DBContract.PersonTable.COLUMN_PERSONID,
                DBContract.PersonTable.COLUMN_FIRSTNAME,
                DBContract.PersonTable.COLUMN_LASTNAME,
                DBContract.PersonTable.COLUMN_ZID,
                DBContract.PersonTable.COLUMN_PROFILEPIC,
                DBContract.PersonTable.COLUMN_EMAIL
        };

        String selection = DBContract.PersonTable.COLUMN_PERSONID + " = ?";
        String[] selectionArgs = {personID + ""};
        Cursor c = db.query(
                DBContract.PersonTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        Person person = new Person();
        if (c.getCount() != 0) {
            Log.d(TAG, "Person: " + c.getString(1) + " " + c.getString(2));
            person = new Person(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5));
        }
        c.close();
        close();
        return person;
    }

    public Person addPerson(Person person) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.PersonTable.COLUMN_FIRSTNAME, person.getFirstName());
        contentValues.put(DBContract.PersonTable.COLUMN_LASTNAME, person.getLastName());
        contentValues.put(DBContract.PersonTable.COLUMN_ZID, person.getzID());
        contentValues.put(DBContract.PersonTable.COLUMN_PROFILEPIC, person.getProfilePath());
        contentValues.put(DBContract.PersonTable.COLUMN_EMAIL, person.getEmail());


        long newRowID = db.insert(DBContract.PersonTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Added new Person to database: " + person.getFirstName() + " " + person.getLastName() + " rowId = " + newRowID);
        person.setPersonID((int) newRowID);
        Log.d(TAG, "Set new PersonID to equal newRowID... now is " + person.getPersonID());
        close();
        return person;
    }

    public void updatePerson(int id, String fname, String lname, int zid, String email) {
        open();
        String update = "update " + DBContract.PersonTable.TABLE_NAME +
                " set " + DBContract.PersonTable.COLUMN_FIRSTNAME + " = \"" + fname + "\"" + COMMA_SEP +
                DBContract.PersonTable.COLUMN_LASTNAME + " = \"" + lname + "\"" + COMMA_SEP +
<<<<<<< HEAD
                DBContract.PersonTable.COLUMN_ZID + " = " + zid  + COMMA_SEP +
                DBContract.PersonTable.COLUMN_EMAIL + " = " + "\"" + email + "\"" +
=======
                DBContract.PersonTable.COLUMN_ZID + " = " + zid + COMMA_SEP +
                DBContract.PersonTable.COLUMN_EMAIL + " = \"" + email + "\"" +
>>>>>>> a7ef165f92399f95e3f49289bb2e13249a081b90
                " where " + DBContract.PersonTable.COLUMN_PERSONID + " = " + id;

        db.execSQL(update);
        Log.d(TAG, "Updated person, is now " + fname + " " + lname);
        close();
    }

    public void addImageFilePathForPerson(int id, String imgPath) {
        open();
        String updatePath = "update" + DBContract.PersonTable.TABLE_NAME +
                " set " + DBContract.PersonTable.COLUMN_PROFILEPIC + " = \"" + imgPath + "\"" +
                " where " + DBContract.PersonTable.COLUMN_PERSONID + " = " + id;
        db.execSQL(updatePath);
        close();
    }


}

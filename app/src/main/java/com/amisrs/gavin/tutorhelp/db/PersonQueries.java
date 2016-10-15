package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Person;

/**
 * Created by Gavin on 15/10/2016.
 */
public class PersonQueries extends QueryBase {
    private static final String TAG = "PersonQueries";

    public PersonQueries(Context context) {
        super(context);
    }

    public Person getPersonByID(int personID) {
        open();
        String[] projection = {
                DBContract.PersonTable.COLUMN_PERSONID,
                DBContract.PersonTable.COLUMN_FIRSTNAME,
                DBContract.PersonTable.COLUMN_LASTNAME,
                DBContract.PersonTable.COLUMN_ZID
        };

        String selection = DBContract.PersonTable.COLUMN_PERSONID + " = ?";
        String[] selectionArgs = { personID+"" };
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
        if(c.getCount() != 0) {
            Log.d(TAG, "Person: " + c.getString(1) + " " + c.getString(2));
            person = new Person(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
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

        long newRowID = db.insert(DBContract.PersonTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Added new Person to database: " + person.getFirstName() + " " + person.getLastName() + " rowId = " + newRowID);
        person.setPersonID((int)newRowID);
        Log.d(TAG, "Set new PersonID to equal newRowID... now is " + person.getPersonID());
        close();
        return person;
    }



}

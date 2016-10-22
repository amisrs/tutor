package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Assessment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gavin on 22/10/2016.
 */
public class AssessmentQueries extends QueryBase {
    private static final String TAG = "AssessmentQueries";
    public AssessmentQueries(Context context) {
        super(context);
    }

    public void addAssessment(Assessment assessment) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.AssessmentTable.COLUMN_NAME, assessment.getName());
        contentValues.put(DBContract.AssessmentTable.COLUMN_DESCRIPTION, assessment.getDescription());
        contentValues.put(DBContract.AssessmentTable.COLUMN_TERM, assessment.getTerm());
        contentValues.put(DBContract.AssessmentTable.COLUMN_WEIGHTING, assessment.getWeighting());

        long newRowId = db.insert(DBContract.AssessmentTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Inserted new assessment: " + newRowId + " " + assessment.getName() + " " + assessment.getWeighting());
        close();
    }

    public ArrayList<Assessment> getAssessmentsForTerm(String term) {
        open();
        String[] projection = {
                DBContract.AssessmentTable.COLUMN_ASSESSMENTID,
                DBContract.AssessmentTable.COLUMN_NAME,
                DBContract.AssessmentTable.COLUMN_DESCRIPTION,
                DBContract.AssessmentTable.COLUMN_TERM,
                DBContract.AssessmentTable.COLUMN_WEIGHTING
        };

        String selection = DBContract.AssessmentTable.COLUMN_TERM + " = ?";
        String[] selectionArgs = { term };

        Cursor c = db.query(
                DBContract.AssessmentTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        ArrayList<Assessment> assessments = new ArrayList<>();
        while(!c.isAfterLast()) {
            Assessment newAssessment = new Assessment(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4));
            assessments.add(newAssessment);
            c.moveToNext();
        }
        close();

        return assessments;
    }
}

package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Student;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gavin on 22/10/2016.
 */
public class AssessmentQueries extends QueryBase {
    private static final String TAG = "AssessmentQueries";
    private static final String COMMA_SEP = ", ";
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
        contentValues.put(DBContract.AssessmentTable.COLUMN_MAXMARK, assessment.getMaxMark());

        long newRowId = db.insert(DBContract.AssessmentTable.TABLE_NAME, null, contentValues);
        Log.d(TAG, "Inserted new assessment: " + newRowId + " " + assessment.getName() + " " + assessment.getWeighting() + " " + assessment.getTerm());

        StudentQueries studentQueries = new StudentQueries(context);
        ArrayList<Student> studentsToAddMark = studentQueries.getStudentsByTerm(assessment.getTerm());
        Log.d(TAG, "Got students for the term that the assessment is in: " + studentsToAddMark.size() + " term:" + assessment.getTerm());

        for(Student s : studentsToAddMark) {
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put(DBContract.MarkTable.COLUMN_STUDENTID, s.getStudentID());
            // dont need tutorial?
            contentValues1.put(DBContract.MarkTable.COLUMN_ASSESSMENTID, (int)newRowId);
            contentValues1.put(DBContract.MarkTable.COLUMN_MARK, 0);
            long newRowId2 = db.insert(DBContract.MarkTable.TABLE_NAME, null, contentValues1);
            Log.d(TAG, "Added mark for assessment: " + assessment.getName() + " for student in this term " + s.toString() + " " + assessment.getTerm());
        }
        close();
    }

    public ArrayList<Assessment> getAssessmentsForTerm(String term) {
        open();
        String[] projection = {
                DBContract.AssessmentTable.COLUMN_ASSESSMENTID,
                DBContract.AssessmentTable.COLUMN_NAME,
                DBContract.AssessmentTable.COLUMN_DESCRIPTION,
                DBContract.AssessmentTable.COLUMN_TERM,
                DBContract.AssessmentTable.COLUMN_WEIGHTING,
                DBContract.AssessmentTable.COLUMN_MAXMARK
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
            Assessment newAssessment = new Assessment(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4), c.getInt(5));
            assessments.add(newAssessment);
            Log.d(TAG, "Got new assessment for term: " + newAssessment.getName() + " " + newAssessment.getTerm());
            c.moveToNext();
        }

        close();

        return assessments;
    }

    public ArrayList<String> getTermsThatExist() {
        open();
        String[] projection = {
                DBContract.AssessmentTable.COLUMN_TERM
        };

        Cursor c = db.query(
                DBContract.AssessmentTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        c.moveToFirst();
        ArrayList<String> terms = new ArrayList<>();
        while(!c.isAfterLast()) {
            Log.d(TAG, "looking at term; " + c.getString(0));
            boolean termExists = false;

            if(terms.size() > 0) {
                for (String s : terms) {
                    if (s.equals(c.getString(0))) {
                        Log.d(TAG, "Term " + c.getString(0) + " already exists in the list: " + s);
                        termExists = true;
                    }
                }
            }

            if (!termExists) {
                Log.d(TAG, "Adding term: " + c.getString(0));
                terms.add(c.getString(0));
            }

            c.moveToNext();
        }

//        for(int i=0; i<terms.size(); i++) {
//            for(int k=i+1; k<terms.size(); k++) {
//                if(terms.get(i).compareTo(terms.get(k)) > 0) {
//                    String temp = terms.get(k);
//                    terms.set(k, terms.get(i));
//                    terms.set(i, temp);
//                }
//            }
//        }



        c.close();
        close();

        Log.d(TAG, "Got terms that exist: " + terms.size());

        return terms;
    }

    public void updateAssessment(Assessment assessment) {
        open();
        String update = "update " + DBContract.AssessmentTable.TABLE_NAME + " set " +
                DBContract.AssessmentTable.COLUMN_NAME + " = \"" + assessment.getName() + "\"" + COMMA_SEP +
                DBContract.AssessmentTable.COLUMN_DESCRIPTION + " = \"" + assessment.getDescription() + "\"" + COMMA_SEP +
                DBContract.AssessmentTable.COLUMN_WEIGHTING + " = " + assessment.getWeighting() +
                " where " + DBContract.AssessmentTable.COLUMN_ASSESSMENTID + " = " + assessment.getAssessmentId();
        db.execSQL(update);
        Log.d(TAG, "Updated assessment, new: " + assessment.getName());
        close();
    }

    public Assessment getAssessmentById(int id) {
        open();
        Log.d(TAG, "Getting assessment that has id: " + id);
        String[] projection = {
                DBContract.AssessmentTable.COLUMN_ASSESSMENTID,
                DBContract.AssessmentTable.COLUMN_NAME,
                DBContract.AssessmentTable.COLUMN_DESCRIPTION,
                DBContract.AssessmentTable.COLUMN_TERM,
                DBContract.AssessmentTable.COLUMN_WEIGHTING,
                DBContract.AssessmentTable.COLUMN_MAXMARK
        };
        String selection = DBContract.AssessmentTable.COLUMN_ASSESSMENTID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

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
        Assessment newAssessment = new Assessment(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4), c.getInt(5));
        c.close();
        close();
        return newAssessment;
    }

    public void deleteAssessment(Assessment assessment) {
        
    }
}

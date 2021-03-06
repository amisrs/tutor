package com.amisrs.gavin.tutorhelp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.Mark;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by karenhuang on 16/10/16.
 */

public class StudentQueries extends QueryBase {
    private static final String TAG = "StudentQueries";
    private static final String COMMA_SEP = ", ";

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

        //add mark for assessment
        AssessmentQueries assessmentQueries = new AssessmentQueries(context);
        ArrayList<Assessment> assessmentArrayList = assessmentQueries.getAssessmentsForTerm(tutorial.getTerm());

        for(Assessment a : assessmentArrayList) {
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put(DBContract.MarkTable.COLUMN_STUDENTID, (int) newRowId);
            contentValues3.put(DBContract.MarkTable.COLUMN_ASSESSMENTID, a.getAssessmentId());
            contentValues3.put(DBContract.MarkTable.COLUMN_MARK, 0);
            long newRowId3 = db.insert(DBContract.MarkTable.TABLE_NAME, null, contentValues3);
            Log.d(TAG, "Inserted new Mark for student " + student.toString() + " for assessment " + a.getName());
        }
        close();
    }

    public Student getStudentById(int id) {
        open();
        String query = "select s." + DBContract.StudentTable.COLUMN_PERSONID + COMMA_SEP +
                "s." + DBContract.StudentTable.COLUMN_STUDENTID + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_FIRSTNAME + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_LASTNAME + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_ZID + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_PROFILEPIC + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_EMAIL +
                " from " + DBContract.PersonTable.TABLE_NAME + " p" +
                " join " + DBContract.StudentTable.TABLE_NAME + " s" +
                " on s." + DBContract.StudentTable.COLUMN_PERSONID + " = " +
                "p." + DBContract.PersonTable.COLUMN_PERSONID +
                " where " + DBContract.StudentTable.COLUMN_STUDENTID + " = ?";

        Cursor c = db.rawQuery(query, new String[] { String.valueOf(id) });
        c.moveToFirst();
        Person newPerson = new Person(c.getInt(0),c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
        Student newStudent = new Student(c.getInt(1), c.getInt(0), newPerson);
        c.close();
        close();
        return newStudent;

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

    public ArrayList<Student> getStudentsByTerm(String term) {
        open();
        ArrayList<Student> students = new ArrayList<>();
        String query = "select s." + DBContract.StudentTable.COLUMN_PERSONID + COMMA_SEP +
                "s." + DBContract.StudentTable.COLUMN_STUDENTID + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_FIRSTNAME + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_LASTNAME + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_ZID + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_PROFILEPIC + COMMA_SEP +
                "p." + DBContract.PersonTable.COLUMN_EMAIL +
                " from " + DBContract.PersonTable.TABLE_NAME + " p" +
                " join " + DBContract.StudentTable.TABLE_NAME + " s" +
                " on s." + DBContract.StudentTable.COLUMN_PERSONID + " = " +
                "p." + DBContract.PersonTable.COLUMN_PERSONID +
                " join " + DBContract.EnrolmentTable.TABLE_NAME + " e" +
                " on s." + DBContract.StudentTable.COLUMN_STUDENTID + " = " +
                "e." + DBContract.EnrolmentTable.COLUMN_STUDENTID +
                " join " + DBContract.TutorialTable.TABLE_NAME + " t" +
                " on e." + DBContract.EnrolmentTable.COLUMN_TUTORIALID + " = " +
                "t." + DBContract.TutorialTable.COLUMN_TUTORIALID +
                " where " + DBContract.TutorialTable.COLUMN_TERM + " = ?";

        Cursor c = db.rawQuery(query, new String[] { term });
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Person newPerson = new Person(c.getInt(0),c.getString(2), c.getString(3), c.getInt(4), c.getString(5), c.getString(6));
            Student newStudent = new Student(c.getInt(1), c.getInt(0), newPerson);
            students.add(newStudent);
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

    public ArrayList<Mark> getMarksForStudent(Student student) {
        open();
        ArrayList<Mark> marks = new ArrayList<>();
        String[] projection = {
                DBContract.MarkTable.COLUMN_STUDENTID,
                DBContract.MarkTable.COLUMN_ASSESSMENTID,
                DBContract.MarkTable.COLUMN_MARK
        };

        String selection = DBContract.MarkTable.COLUMN_STUDENTID + " = ?";
        String[] selectionArgs = { String.valueOf(student.getStudentID()) };

        Cursor c = db.query(
                DBContract.MarkTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        while(!c.isAfterLast()) {
            Mark newMark = new Mark(c.getInt(0), c.getInt(1), c.getInt(2));
            marks.add(newMark);
            c.moveToNext();
        }
        c.close();
        close();

        return marks;
    }

    public void updateMark(Mark mark) {
        open();
        String query = "update " + DBContract.MarkTable.TABLE_NAME + " set " +
                DBContract.MarkTable.COLUMN_MARK + " = " + mark.getMark() +
                " where " + DBContract.MarkTable.COLUMN_ASSESSMENTID + " = " + mark.getAssessmentID() +
                " and " + DBContract.MarkTable.COLUMN_STUDENTID + " = " + mark.getStudentID();
        db.execSQL(query);
        close();
    }

    public double recalculateGradeForStudentAndTerm(Student student, String term) {
        double grade = 0;
        double totalWeighting = 0;
        AssessmentQueries assessmentQueries = new AssessmentQueries(context);

        ArrayList<Mark> marks = getMarksForStudent(student);
        for(Mark m : marks) {
            Assessment assessment = assessmentQueries.getAssessmentById(m.getAssessmentID());
            Log.d(TAG, "Mark for assessment " + assessment.getName() + " = " + m.getMark() + "/" + assessment.getMaxMark());
            double rawPercentage = (double)m.getMark()/(double)assessment.getMaxMark();
            Log.d(TAG, "Percentage = " + rawPercentage);
            double weightedMark = rawPercentage * (assessment.getWeighting()/100);
            Log.d(TAG, "Multiply by weighting: " + assessment.getWeighting()/100 + " = " + weightedMark);
            grade += weightedMark;
            Log.d(TAG, "New grade: " + grade);
        }

        //get total weighting (in case it doesnt add to 100
        ArrayList<Assessment> assessmentArrayList = assessmentQueries.getAssessmentsForTerm(term);
        for(Assessment a : assessmentArrayList) {
            totalWeighting += a.getWeighting();
        }
        Log.d(TAG, "All assessments this term combine weighting to: " + totalWeighting);
        grade = (grade/(totalWeighting/100)) * 100;
        Log.d(TAG, "Grade so far: " + grade);

        open();

        //TODO: if you want to reuse students, change this query to identify by tutorial as well
        String query = "update " + DBContract.EnrolmentTable.TABLE_NAME + " set " +
                DBContract.EnrolmentTable.COLUMN_GRADE + " = " + grade +
                " where " + DBContract.EnrolmentTable.COLUMN_STUDENTID + " = " + student.getStudentID();
        db.execSQL(query);

        Log.d(TAG, "Updated grade to be " + grade);
        close();
        return grade;
    }

    public void deleteStudent(Student student) {
        open();
        PersonQueries personQueries = new PersonQueries(context);
        Person toDelete = personQueries.getPersonByID(student.getPersonID());

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
        String[] whereArgs = { String.valueOf(student.getPersonID()) };
        db.delete(
                DBContract.PersonTable.TABLE_NAME,
                whereClause,
                whereArgs
        );
        close();
        Log.d(TAG, "Deleted person: " + student.getPerson().getFirstName());
    }

    public ArrayList<StudentWeek> getStudentWeekForStudent(Student student) {
        open();
        String[] projection = {
                DBContract.StudentWeekTable.COLUMN_STUDENTID,
                DBContract.StudentWeekTable.COLUMN_WEEKID,
                DBContract.StudentWeekTable.COLUMN_ATTENDED,
                DBContract.StudentWeekTable.COLUMN_PRIVATECOMMENT,
                DBContract.StudentWeekTable.COLUMN_PUBLICCOMMENT
        };

        String selection = DBContract.StudentWeekTable.COLUMN_STUDENTID + " = ?";
        String[] selectionArgs = { String.valueOf(student.getStudentID()) };

        Cursor c = db.query(
                DBContract.StudentWeekTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();
        ArrayList<StudentWeek> studentWeeks = new ArrayList<>();
        while(!c.isAfterLast()) {
            studentWeeks.add(new StudentWeek(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4)));
            c.moveToNext();
        }
        c.close();
        close();

        return studentWeeks;
    }
}

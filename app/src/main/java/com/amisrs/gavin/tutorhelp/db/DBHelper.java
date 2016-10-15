package com.amisrs.gavin.tutorhelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gavin on 11/10/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    public static final String DATABASE_NAME = "tutor.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " text";
    private static final String INTEGER_TYPE = " integer";

    private static final String NOT_NULL = " not null";
    private static final String PRIMARY_KEY = " primary key";
    private static final String UNIQUE = " unique";

    private static final String COMMA_SEP = ", ";

    private static final String DROP_TABLE_IF_EXISTS = "drop table if exists ";

    public static final String SQL_CREATE_PERSONTABLE =
            "create table " + DBContract.PersonTable.TABLE_NAME + "(" +
                    DBContract.PersonTable.COLUMN_PERSONID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    DBContract.PersonTable.COLUMN_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                    DBContract.PersonTable.COLUMN_LASTNAME + TEXT_TYPE + COMMA_SEP +
                    DBContract.PersonTable.COLUMN_ZID + INTEGER_TYPE + UNIQUE +
                    ");";

    public static final String SQL_CREATE_STUDENTTABLE =

            "create table " + DBContract.StudentTable.TABLE_NAME + "(" +
                    DBContract.StudentTable.COLUMN_STUDENTID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    DBContract.StudentTable.COLUMN_PERSONID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    "foreign key(" + DBContract.StudentTable.COLUMN_PERSONID + ")" +
                        " references " + DBContract.PersonTable.TABLE_NAME + "(" + DBContract.PersonTable.COLUMN_PERSONID + ")" +
                    "); ";

    public static final String SQL_CREATE_TUTORTABLE =
            "create table " + DBContract.TutorTable.TABLE_NAME + "(" +
                    DBContract.TutorTable.COLUMN_TUTORID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    DBContract.TutorTable.COLUMN_PERSONID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    "foreign key(" + DBContract.TutorTable.COLUMN_PERSONID + ")" +
                        " references " + DBContract.PersonTable.TABLE_NAME + "(" + DBContract.PersonTable.COLUMN_PERSONID + ")" +
                    "); ";

    public static final String SQL_CREATE_TUTORIALTABLE =
            "create table " + DBContract.TutorialTable.TABLE_NAME + "(" +
                    DBContract.TutorialTable.COLUMN_TUTORIALID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    DBContract.TutorialTable.COLUMN_TUTORID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.TutorialTable.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    DBContract.TutorialTable.COLUMN_TIMESLOT + TEXT_TYPE + COMMA_SEP +
                    DBContract.TutorialTable.COLUMN_LOCATION + TEXT_TYPE + COMMA_SEP +
                    "foreign key(" + DBContract.TutorialTable.COLUMN_TUTORID + ")" +
                        " references " + DBContract.TutorTable.TABLE_NAME + "(" + DBContract.TutorialTable.COLUMN_TUTORID + ")" +
                    "); ";

    public static final String SQL_CREATE_WEEKTABLE =
            "create table " + DBContract.WeekTable.TABLE_NAME + "(" +
                    DBContract.WeekTable.COLUMN_WEEKID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    DBContract.WeekTable.COLUMN_TUTORIALID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.WeekTable.COLUMN_WEEKNUMBER + INTEGER_TYPE + COMMA_SEP +
                    DBContract.WeekTable.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    "foreign key(" + DBContract.WeekTable.COLUMN_TUTORIALID + ")" +
                        " references " + DBContract.TutorialTable.TABLE_NAME + "(" + DBContract.TutorialTable.COLUMN_TUTORIALID + ")" +
                    "); ";

    public static final String SQL_CREATE_ASSESSMENTTABLE =
            "create table " + DBContract.AssessmentTable.TABLE_NAME + "(" +
                    DBContract.AssessmentTable.COLUMN_ASSESSMENTID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    DBContract.AssessmentTable.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    DBContract.AssessmentTable.COLUMN_DESCRIPTION + TEXT_TYPE +
                    "); ";

    public static final String SQL_CREATE_STUDENTWEEKTABLE =
            "create table " + DBContract.StudentWeekTable.TABLE_NAME + "(" +
                    DBContract.StudentWeekTable.COLUMN_STUDENTID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.StudentWeekTable.COLUMN_WEEKID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.StudentWeekTable.COLUMN_ATTENDED + INTEGER_TYPE +
                        " check(" + DBContract.StudentWeekTable.COLUMN_ATTENDED + " in (0,1))" + COMMA_SEP +
                    DBContract.StudentWeekTable.COLUMN_PRIVATECOMMENT + TEXT_TYPE + COMMA_SEP +
                    DBContract.StudentWeekTable.COLUMN_PUBLICCOMMENT + TEXT_TYPE + COMMA_SEP +
                    "primary key(" + DBContract.StudentWeekTable.COLUMN_STUDENTID + COMMA_SEP + DBContract.StudentWeekTable.COLUMN_WEEKID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.StudentWeekTable.COLUMN_STUDENTID + ")" +
                        " references " + DBContract.StudentTable.TABLE_NAME + "(" + DBContract.StudentTable.COLUMN_STUDENTID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.StudentWeekTable.COLUMN_WEEKID + ")" +
                        " references " + DBContract.WeekTable.TABLE_NAME + "(" + DBContract.WeekTable.COLUMN_WEEKID + ")" +
                    "); ";

    public static final String SQL_CREATE_ENROLMENTTABLE =
            "create table " + DBContract.EnrolmentTable.TABLE_NAME + "(" +
                    DBContract.EnrolmentTable.COLUMN_STUDENTID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.EnrolmentTable.COLUMN_TUTORIALID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.EnrolmentTable.COLUMN_GRADE + INTEGER_TYPE + COMMA_SEP +
                    "primary key(" + DBContract.EnrolmentTable.COLUMN_STUDENTID + COMMA_SEP + DBContract.EnrolmentTable.COLUMN_TUTORIALID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.EnrolmentTable.COLUMN_STUDENTID + ") " +
                        " references " + DBContract.StudentTable.TABLE_NAME + "(" + DBContract.StudentTable.COLUMN_STUDENTID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.EnrolmentTable.COLUMN_TUTORIALID + ")" +
                        " references " + DBContract.TutorialTable.TABLE_NAME + "(" + DBContract.TutorialTable.COLUMN_TUTORIALID + ")" +
                    "); ";

    public static final String SQL_CREATE_MARKTABLE =
            "create table " + DBContract.MarkTable.TABLE_NAME + "(" +
                    DBContract.MarkTable.COLUMN_STUDENTID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.MarkTable.COLUMN_TUTORIALID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.MarkTable.COLUMN_ASSESSMENTID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    DBContract.MarkTable.COLUMN_MARK + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    "primary key(" + DBContract.MarkTable.COLUMN_STUDENTID + COMMA_SEP +
                                     DBContract.MarkTable.COLUMN_TUTORIALID + COMMA_SEP +
                                     DBContract.MarkTable.COLUMN_ASSESSMENTID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.MarkTable.COLUMN_STUDENTID + ")" +
                        " references " + DBContract.StudentTable.TABLE_NAME + "(" + DBContract.StudentTable.COLUMN_STUDENTID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.MarkTable.COLUMN_TUTORIALID + ")" +
                        " references " + DBContract.TutorialTable.TABLE_NAME + "(" + DBContract.TutorialTable.COLUMN_TUTORIALID + ")" + COMMA_SEP +
                    "foreign key(" + DBContract.MarkTable.COLUMN_ASSESSMENTID + ")" +
                        " references " + DBContract.AssessmentTable.TABLE_NAME + "(" + DBContract.AssessmentTable.COLUMN_ASSESSMENTID + ")" +
                    "); ";

    public static final String SQL_DROP_PERSONTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.PersonTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_STUDENTTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.StudentTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_TUTORTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.TutorTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_TUTORIALTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.TutorialTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_WEEKTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.WeekTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_ASSESSMENTTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.AssessmentTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_STUDENTWEEKTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.StudentWeekTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_ENROLMENTTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.EnrolmentTable.TABLE_NAME + "; ";
    public static final String SQL_DROP_MARKTABLE =
            DROP_TABLE_IF_EXISTS + DBContract.MarkTable.TABLE_NAME + "; ";




    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: executing SQL_CREATE_PERSONTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_PERSONTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_STUDENTTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENTTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_TUTORTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_TUTORTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_TUTORIALTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_TUTORIALTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_WEEKTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_WEEKTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_ASSESSMENTTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_ASSESSMENTTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_STUDENTWEEKTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENTWEEKTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_ENROLMENTTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_ENROLMENTTABLE);
        Log.d(TAG, "onCreate: executing SQL_CREATE_MARKTABLE statement.");
        sqLiteDatabase.execSQL(SQL_CREATE_MARKTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "dropTables: executing DROP statements.");
        sqLiteDatabase.execSQL(SQL_DROP_PERSONTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_STUDENTTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_TUTORTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_TUTORIALTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_WEEKTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_ASSESSMENTTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_STUDENTWEEKTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_ENROLMENTTABLE);
        sqLiteDatabase.execSQL(SQL_DROP_MARKTABLE);
    }
}

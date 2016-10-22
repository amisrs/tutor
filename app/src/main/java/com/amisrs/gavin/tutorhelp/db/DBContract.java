package com.amisrs.gavin.tutorhelp.db;

import android.provider.BaseColumns;

/**
 * Created by Gavin on 11/10/2016.
 */
public class DBContract {
    private DBContract() {

    }

    public static class PersonTable implements BaseColumns {
        public static final String TABLE_NAME = "Person";
        public static final String COLUMN_PERSONID = "PersonID";
        public static final String COLUMN_FIRSTNAME = "FirstName";
        public static final String COLUMN_LASTNAME = "LastName";
        public static final String COLUMN_ZID = "ZID";
        public static final String COLUMN_PROFILEPIC = "ProfilePic";
        public static final String COLUMN_EMAIL = "Email";
    }

    public static class StudentTable implements BaseColumns {
        public static final String TABLE_NAME = "Student";
        public static final String COLUMN_STUDENTID = "StudentID";
        public static final String COLUMN_PERSONID = "PersonID";
    }

    public static class TutorTable implements BaseColumns {
        public static final String TABLE_NAME = "Tutor";
        public static final String COLUMN_TUTORID = "TutorID";
        public static final String COLUMN_PERSONID = "PersonID";
    }

    public static class TutorialTable implements BaseColumns {
        public static final String TABLE_NAME = "Tutorial";
        public static final String COLUMN_TUTORIALID = "TutorialID";
        public static final String COLUMN_TUTORID = "TutorID";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_TIMESLOT = "TimeSlot";
        public static final String COLUMN_LOCATION = "Location";
        public static final String COLUMN_TERM = "Term";
    }

    public static class WeekTable implements BaseColumns {
        public static final String TABLE_NAME = "Week";
        public static final String COLUMN_WEEKID = "WeekID";
        public static final String COLUMN_TUTORIALID = "TutorialID";
        public static final String COLUMN_WEEKNUMBER = "WeekNumber";
        public static final String COLUMN_DESCRIPTION = "Description";
    }

    public static class AssessmentTable implements BaseColumns {
        public static final String TABLE_NAME = "Assessment";
        public static final String COLUMN_ASSESSMENTID = "AssessmentID";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_TERM = "Term";
        public static final String COLUMN_WEIGHTING = "Weighting";
    }

    public static class StudentWeekTable implements BaseColumns {
        public static final String TABLE_NAME = "StudentWeek";
        public static final String COLUMN_WEEKID = "WeekID";
        public static final String COLUMN_STUDENTID = "StudentID";
        public static final String COLUMN_ATTENDED = "Attended";
        public static final String COLUMN_PUBLICCOMMENT = "PublicComment";
        public static final String COLUMN_PRIVATECOMMENT = "PrivateComment";
    }

    public static class EnrolmentTable implements BaseColumns {
        public static final String TABLE_NAME = "Enrolment";
        public static final String COLUMN_STUDENTID = "StudentID";
        public static final String COLUMN_TUTORIALID = "TutorialID";
        public static final String COLUMN_GRADE = "Grade";
    }

    public static class MarkTable implements BaseColumns {
        public static final String TABLE_NAME = "Mark";
        public static final String COLUMN_STUDENTID = "StudentID";
        public static final String COLUMN_TUTORIALID = "TutorialID";
        public static final String COLUMN_ASSESSMENTID = "AssessmentID";
        public static final String COLUMN_MARK = "Mark";
    }

}

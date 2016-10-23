package com.amisrs.gavin.tutorhelp.model;

/**
 * Created by Gavin on 22/10/2016.
 */
public class Enrolment {
    public int studentID;
    public int tutorialID;
    public double grade;

    public Enrolment(int studentID, int tutorialID, double grade) {
        this.studentID = studentID;
        this.tutorialID = tutorialID;
        this.grade = grade;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getTutorialID() {
        return tutorialID;
    }

    public void setTutorialID(int tutorialID) {
        this.tutorialID = tutorialID;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}

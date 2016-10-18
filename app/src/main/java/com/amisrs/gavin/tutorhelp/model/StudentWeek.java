package com.amisrs.gavin.tutorhelp.model;

/**
 * Created by Gavin on 18/10/2016.
 */
public class StudentWeek {
    private static final String TAG = "StudentWeek";
    private int studentID;
    private int weekID;
    private int attended;
    private String privateComment;
    private String publicComment;

    public StudentWeek(Student student, Week week) {
        this.studentID = student.getStudentID();
        this.weekID = week.getWeekID();
        attended = 0;
        privateComment = "";
        publicComment = "";
    }

    //from db
    public StudentWeek(int studentID, int weekID, int attended, String privateComment, String publicComment) {
        this.studentID = studentID;
        this.weekID = weekID;
        this.attended = attended;
        this.privateComment = privateComment;
        this.publicComment = publicComment;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getWeekID() {
        return weekID;
    }

    public void setWeekID(int weekID) {
        this.weekID = weekID;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public String getPrivateComment() {
        return privateComment;
    }

    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    public String getPublicComment() {
        return publicComment;
    }

    public void setPublicComment(String publicComment) {
        this.publicComment = publicComment;
    }
}

package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 23/10/2016.
 */

public class Mark implements Parcelable, Comparable<Mark> {
    private static final Parcelable.Creator CREATOR = new MarkCreator();
    private int studentID;
    private int assessmentID;
    private int mark;

    public Mark(int studentID, int assessmentID, int mark) {
        this.studentID = studentID;
        this.assessmentID = assessmentID;
        this.mark = mark;
    }

    //unparcel
    public Mark(Parcel parcel) {
        this.studentID = parcel.readInt();
        this.assessmentID = parcel.readInt();
        this.mark = parcel.readInt();
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(studentID);
        dest.writeInt(assessmentID);
        dest.writeInt(mark);
    }

    @Override
    public int compareTo(Mark m) {
        int compareMark = m.getMark();
        return getMark() - compareMark;
    }

    static class MarkCreator implements Parcelable.Creator<Mark> {
        @Override
        public Mark createFromParcel(Parcel parcel) {
            return new Mark(parcel);
        }

        @Override
        public Mark[] newArray(int i) {
            return new Mark[i];
        }
    }

}

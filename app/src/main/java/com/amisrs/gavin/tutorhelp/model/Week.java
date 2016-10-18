package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 17/10/2016.
 */
public class Week implements Parcelable {
    int weekID;
    int tutorialID;
    int weekNumber;
    String description;

    //constructor from db
    public Week(int weekID, int tutorialID, int weekNumber, String description) {
        this.weekID = weekID;
        this.tutorialID = tutorialID;
        this.weekNumber = weekNumber;
        this.description = description;
    }

    //constructor to put in db
    public Week(int tutorialID, int weekNumber, String description) {
        this.tutorialID = tutorialID;
        this.weekNumber = weekNumber;
        this.description = description;
    }


    public int getWeekID() {
        return weekID;
    }

    public void setWeekID(int weekID) {
        this.weekID = weekID;
    }

    public int getTutorialID() {
        return tutorialID;
    }

    public void setTutorialID(int tutorialID) {
        this.tutorialID = tutorialID;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(weekID);
        parcel.writeInt(tutorialID);
        parcel.writeInt(weekNumber);
        parcel.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

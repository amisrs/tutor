package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Gavin on 15/10/2016.
 */
public class Tutorial implements Parcelable{
    private static final String TAG = "Tutorial";
    public static final Creator CREATOR = new TutorialCreator();
    public static final int MAX_WEEKS = 13;

    private int tutorialID;
    private int tutorID;
    private String name;
    private String timeSlot;
    private String location;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tutorialID);
        parcel.writeInt(tutorID);
        parcel.writeString(name);
        parcel.writeString(timeSlot);
        parcel.writeString(location);
    }
    //from parcel
    public Tutorial(Parcel parcel) {
        this.tutorialID = parcel.readInt();
        this.tutorID = parcel.readInt();
        this.name = parcel.readString();
        this.timeSlot = parcel.readString();
        this.location = parcel.readString();
    }


    //constructor for adding a new tutorial
    //the tutorialID should autoincrement in database
    public Tutorial(int tutorID, String name, String timeSlot, String location) {
        this.tutorID = tutorID;
        this.name = name;
        this.timeSlot = timeSlot;
        this.location = location;
        Log.d(TAG, "Made new tutorial object for entry: " + tutorID + " " + name);

    }

    //constructor for getting object from database values
    public Tutorial(int tutorialID, int tutorID, String name, String timeSlot, String location) {
        this.tutorialID = tutorialID;
        this.tutorID = tutorID;
        this.name = name;
        this.timeSlot = timeSlot;
        this.location = location;
        Log.d(TAG, "Retrieved tutorial data from database: " + tutorialID + " " + tutorID + "" + "name");
    }


    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getTutorialID() {
        return tutorialID;
    }

    public void setTutorialID(int tutorialID) {
        this.tutorialID = tutorialID;
    }

    public int getTutorID() {
        return tutorID;
    }

    public void setTutorID(int tutorID) {
        this.tutorID = tutorID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static class TutorialCreator implements Creator<Tutorial> {
        @Override
        public Tutorial createFromParcel(Parcel parcel) {
            return new Tutorial(parcel);
        }

        @Override
        public Tutorial[] newArray(int i) {
            return new Tutorial[0];
        }
    }
}

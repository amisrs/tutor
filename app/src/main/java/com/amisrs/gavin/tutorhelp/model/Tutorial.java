package com.amisrs.gavin.tutorhelp.model;

import android.util.Log;

/**
 * Created by Gavin on 15/10/2016.
 */
public class Tutorial {
    private static final String TAG = "Tutorial";

    private int tutorialID;
    private int tutorID;
    private String name;
    private String timeSlot;
    private String location;


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
}

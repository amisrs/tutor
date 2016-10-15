package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Gavin on 15/10/2016.
 */
public class Person implements Parcelable{
    private static final String TAG = "Person";
    public static final Creator CREATOR = new PersonCreator();
    private int personID;
    private String firstName;
    private String lastName;
    private int zID;

    public Person() {

    }

    //constructor for adding to database, aka from login screen
    //should always make the Person first, then make the Tutor/Student from the PersonID
    public Person(String firstName, String lastName, int zID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zID = zID;
    }

    //constructor for getting from database
    public Person(int personID, String firstName, String lastName, int zID) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zID = zID;
    }

    //read from parcel
    public Person(Parcel parcel) {
        this.personID = parcel.readInt();
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.zID = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(personID);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeInt(zID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public int getzID() {
        return zID;
    }

    public void setzID(int zID) {
        this.zID = zID;
    }

    static class PersonCreator implements Parcelable.Creator<Person> {
        @Override
        public Person createFromParcel(Parcel parcel) {
            return new Person(parcel);
        }

        @Override
        public Person[] newArray(int i) {
            return new Person[i];
        }
    }
}

package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 15/10/2016.
 */
public class Tutor implements Parcelable{
    //http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable
    public static final Creator CREATOR = new TutorCreator();
    private int tutorID;
    private int personID;
    private Person person;

    // made new tutor from the login screen
    // should only be made after the Person is made
    public Tutor(int personID, Person person) {
        //get from db
        this.personID = personID;
        this.person = person;
    }

    // from db
    public Tutor(int tutorID, int personID, Person person) {
        this.tutorID = tutorID;
        this.personID = personID;
        this.person = person;
    }

    //read from parcel
    public Tutor(Parcel parcel) {
        this.tutorID = parcel.readInt();
        this.personID = parcel.readInt();
        this.person = parcel.readParcelable(Person.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tutorID);
        parcel.writeInt(personID);
        parcel.writeParcelable(person, 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return person.getFirstName() + " " + person.getLastName() + " (" + person.getzID() + ")";
    }

    public Person getPerson() {
        return person;
    }

    public int getPersonID() {
        return personID;
    }

    public int getTutorID() {
        return tutorID;
    }

    static class TutorCreator implements Parcelable.Creator<Tutor> {
        @Override
        public Tutor createFromParcel(Parcel parcel) {
            return new Tutor(parcel);
        }

        @Override
        public Tutor[] newArray(int i) {
            return new Tutor[i];
        }
    }
}

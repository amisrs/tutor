package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by karenhuang on 16/10/16.
 */

public class Student implements Parcelable {
    private static final String TAG = "Student";
    public static final Creator CREATOR = new StudentCreator();
    private int studentID;
    private int personID;
    private Person person;

    public Student(int personID, Person person){
        this.personID = personID;
        this.person = person;

    }

    // from db
    public Student(int studentID, int personID, Person person) {
        this.studentID = studentID;
        this.personID = personID;
        this.person = person;
    }

    //read from parcel
    public Student(Parcel parcel) {
        this.studentID = parcel.readInt();
        this.personID = parcel.readInt();
        this.person = parcel.readParcelable(Person.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(studentID);
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

    public int getStudentID() {
        return studentID;
    }

    static class StudentCreator implements Parcelable.Creator<Student> {
        @Override
        public Student createFromParcel(Parcel parcel) {
            return new Student(parcel);
        }

        @Override
        public Student[] newArray(int i) {
            return new Student[i];
        }
    }

}

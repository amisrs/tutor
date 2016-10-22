package com.amisrs.gavin.tutorhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 22/10/2016.
 */
public class Assessment implements Parcelable{
    private static final Creator CREATOR = new AssessmentCreator();
    private int assessmentId;
    private String name;
    private String description;
    private String term;
    private double weighting;

    //add to db
    public Assessment(String name, String description, String term, double weighting) {
        this.name = name;
        this.description = description;
        this.term = term;
        this.weighting = weighting;
    }

    //from db
    public Assessment(int assessmentId, String name, String description, String term, double weighting) {
        this.assessmentId = assessmentId;
        this.name = name;
        this.description = description;
        this.term = term;
        this.weighting = weighting;
    }

    //unparcel
    public Assessment(Parcel parcel) {
        this.assessmentId = parcel.readInt();
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.term = parcel.readString();
        this.weighting = parcel.readDouble();
    }

    public double getWeighting() {
        return weighting;
    }

    public void setWeighting(double weighting) {
        this.weighting = weighting;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(assessmentId);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(term);
        parcel.writeDouble(weighting);
    }

    static class AssessmentCreator implements Parcelable.Creator<Assessment> {
        @Override
        public Assessment createFromParcel(Parcel parcel) {
            return new Assessment(parcel);
        }

        @Override
        public Assessment[] newArray(int i) {
            return new Assessment[0];
        }
    }

}

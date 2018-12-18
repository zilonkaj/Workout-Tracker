package com.zilonkaj.workouttracker.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private String name;
    private int reps;
    private int currentWeight;

    public Exercise(String name, int reps, int currentWeight) {
        this.name = name;
        this.reps = reps;
        this.currentWeight = currentWeight;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to passed in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(reps);
        out.writeInt(currentWeight);
    }

    // used to regenerate object. All Parcelables must have a CREATOR that implements
    // these two methods
    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    private Exercise(Parcel in)
    {
        name = in.readString();
        reps = in.readInt();
        currentWeight = in.readInt();
    }
}

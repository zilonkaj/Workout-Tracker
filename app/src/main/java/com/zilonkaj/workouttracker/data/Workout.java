package com.zilonkaj.workouttracker.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Workout implements Parcelable {
    private List<Exercise> exercises = new ArrayList<>();
    private String WorkoutName;

    public List<Exercise> getExercises()
    {
        return exercises;
    }

    public String getWorkoutName() {
        return WorkoutName;
    }

    public void setWorkoutName(String workoutName) {
        WorkoutName = workoutName;
    }

    public Workout(String WorkoutName)
    {
        this.WorkoutName = WorkoutName;
        exercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
    }

    /* Parcelable interface allows for passing POJOs between activities
     * Functions below are to make this work */

    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to passed in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(exercises);
        out.writeString(WorkoutName);
    }

    // used to regenerate object. All Parcelables must have a CREATOR that implements
    // these two methods
    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    private Workout(Parcel in)
    {
        in.readList(exercises, null);
        WorkoutName = in.readString();
    }
}

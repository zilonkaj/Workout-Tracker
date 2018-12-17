package com.zilonkaj.workouttracker.data;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    private List<Exercise> exercises;
    private String WorkoutName;

    public Workout()
    {
        exercises = new ArrayList<>();
    }

    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
    }
}

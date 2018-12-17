package com.zilonkaj.workouttracker.data;

public class Exercise {
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
}

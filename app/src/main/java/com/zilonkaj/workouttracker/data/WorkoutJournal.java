/*
WorkoutJournal is the abstraction for the JSON file that
contains all of the user's workouts (individual workouts and
the respective exercises)
*/

package com.zilonkaj.workouttracker.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutJournal {

    private static final String JSON_FILENAME = "workout_journal.json";
    private final File journalPath;
    private static final Gson gson = new Gson();

    public WorkoutJournal(File directory) {
        journalPath = new File(directory, JSON_FILENAME);

        try {
            journalPath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeWorkoutsToFile(List<Workout> workoutList) {
        try (FileWriter fileWriter = new FileWriter(journalPath)) {
            gson.toJson(workoutList, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Workout> readWorkoutsFromFile() {
        try (FileReader fileReader = new FileReader(journalPath)) {
            // Gson requires TypeToken to deserialize List<>
            Type listType = new TypeToken<List<Workout>>(){}.getType();

            return gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addWorkout(Workout workoutToAdd) {
        List<Workout> workouts = readWorkoutsFromFile();

        if (workouts == null) {
            workouts = new ArrayList<>();
        }

        workouts.add(workoutToAdd);
        writeWorkoutsToFile(workouts);
    }

    public void updateWorkout(Workout workoutToUpdate) {
        List<Workout> workouts = readWorkoutsFromFile();

        for (int i = 0; i < workouts.size(); i++) {
            if (workouts.get(i).getWorkoutName().equals(workoutToUpdate.getWorkoutName())) {
                workouts.set(i, workoutToUpdate);
                break;
            }
        }

        writeWorkoutsToFile(workouts);
    }

    public void removeWorkout(Workout workoutToRemove) {
        List<Workout> workouts = readWorkoutsFromFile();

        for (int i = 0; i < workouts.size(); i++) {
            if (workouts.get(i).getWorkoutName().equals(workoutToRemove.getWorkoutName())) {
                workouts.remove(i);
                break;
            }

            writeWorkoutsToFile(workouts);
        }
    }

    public Workout getWorkoutAt(int index) {
        return readWorkoutsFromFile().get(index);
    }

    public boolean isEmpty() {
        if (readWorkoutsFromFile() == null) {
            return true;
        } else {
            return readWorkoutsFromFile().isEmpty();
        }
    }

    public String[] getWorkoutNames() {
        List<Workout> workoutList = readWorkoutsFromFile();
        List<String> names = new ArrayList<>();

        if (workoutList != null) {
            for (Workout workout : workoutList) {
                names.add(workout.getWorkoutName());
            }
        }

        return names.toArray(new String[0]);
    }
}

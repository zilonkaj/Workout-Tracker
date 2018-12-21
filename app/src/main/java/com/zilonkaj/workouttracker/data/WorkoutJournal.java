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
    private File journalPath;
    private Gson gson;

    public WorkoutJournal(File directory) {
        journalPath = new File(directory, JSON_FILENAME);

        // create file if it doesn't exist
        try (FileWriter fileWriter = new FileWriter(journalPath)) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        gson = new Gson();
    }

    public void write(List<Workout> workoutList) {
        try (FileWriter fileWriter = new FileWriter(journalPath)) {
            gson.toJson(workoutList, fileWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Workout> read() {
        try (FileReader fileReader = new FileReader(journalPath)) {
            Type listType = new TypeToken<ArrayList<Workout>>(){}.getType();
            return gson.fromJson(fileReader, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

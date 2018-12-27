package com.zilonkaj.workouttracker.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zilonkaj.workouttracker.custom.NewWorkoutDialog;
import com.zilonkaj.workouttracker.R;
import com.zilonkaj.workouttracker.data.Workout;
import com.zilonkaj.workouttracker.data.WorkoutJournal;

public class MainActivity extends AppCompatActivity {

    private WorkoutJournal workoutJournal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // applies GUI from XML file
        setContentView(R.layout.activity_main);

        workoutJournal = new WorkoutJournal(getApplicationContext().getFilesDir());

        if (workoutJournal.isEmpty()) {
            Button viewWorkouts = findViewById(R.id.view_workouts);
            Button workoutMode = findViewById(R.id.workout_mode);

            disableButton(viewWorkouts);
            disableButton(workoutMode);
        }
    }

    private void disableButton(Button buttonToDisable) {
        buttonToDisable.setAlpha(.5f);
        buttonToDisable.setClickable(false);
    }

    // called from Create New Workout button
    public void createNewWorkoutDialog(View view) {
        NewWorkoutDialog dialog = new NewWorkoutDialog(this);
        dialog.show();
    }

    // called if user clicks OK on Create New Workout dialog
    public void openNewWorkout(String workoutName) {
        Workout newWorkout = new Workout(workoutName);
        workoutJournal.addWorkout(newWorkout);

        startActivityWithWorkout(WorkoutActivity.class, newWorkout);
    }

    private void startActivityWithWorkout(Class activityClass, Workout workout) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("WORKOUT", workout);
        startActivity(intent);
    }

    // called from View Workouts button
    public void chooseWorkoutDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a workout to view");

        builder.setItems(workoutJournal.getWorkoutNames(), (dialog, which) -> {
            Workout workout =  workoutJournal.getWorkoutAt(which);
            startActivityWithWorkout(WorkoutActivity.class, workout);
        });

        builder.show();
    }

    public void workoutMode(View view) {
        startActivity(new Intent(this, WorkoutModeActivity.class));
    }
}

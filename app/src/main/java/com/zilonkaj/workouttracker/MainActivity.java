package com.zilonkaj.workouttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zilonkaj.workouttracker.data.Workout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // applies GUI from XML file
        setContentView(R.layout.activity_main);

        //workoutJournal = new WorkoutJournal(getApplicationContext().getFilesDir());
    }

    // popup created when Create New Workout button is pressed
    public void createNewWorkoutDialog(View view) {
        NewWorkoutDialog dialog = new NewWorkoutDialog(this);

        dialog.show();
    }

    public void launchNewWorkoutWizard(String workoutName) {
        Workout newWorkout = new Workout(workoutName);
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("WORKOUT_TO_ADD", newWorkout);
        startActivity(intent);
    }
}

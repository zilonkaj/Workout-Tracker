package com.zilonkaj.workouttracker;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        if (workoutJournal.getWorkouts().isEmpty())
        {
            Button viewWorkouts = findViewById(R.id.viewworkouts);
            Button workoutMode = findViewById(R.id.workoutmode);

            viewWorkouts.setAlpha(.5f);
            viewWorkouts.setClickable(false);

            workoutMode.setAlpha(.5f);
            workoutMode.setClickable(false);
        }
    }

    // popup created when Create New Workout button is pressed
    public void createNewWorkoutDialog(View view) {
        NewWorkoutDialog dialog = new NewWorkoutDialog(this);

        dialog.show();
    }

    public void launchNewWorkoutWizard(String workoutName) {
        Workout newWorkout = new Workout(workoutName);
        workoutJournal.addWorkout(newWorkout);
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("WORKOUT", newWorkout);
        // finish();
        startActivity(intent);
    }

    public void chooseWorkoutDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose a workout to view");

        builder.setItems(workoutJournal.getWorkoutNames(), (dialog, which) -> {
            Intent intent = new Intent(this, WorkoutActivity.class);
            intent.putExtra("WORKOUT", workoutJournal.getWorkouts().get(which));
            // finish();
            startActivity(intent);
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void workoutMode(View view){
        Intent intent = new Intent(this, WorkoutModeActivity.class);
        startActivity(intent);
    }
}

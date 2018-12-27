package com.zilonkaj.workouttracker.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zilonkaj.workouttracker.custom.CustomItemTouchHelperCallback;
import com.zilonkaj.workouttracker.custom.ExerciseRecyclerViewAdapter;
import com.zilonkaj.workouttracker.R;
import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;
import com.zilonkaj.workouttracker.data.WorkoutJournal;

public class WorkoutActivity extends AppCompatActivity {
    private Workout workout;
    private TextView emptyRecyclerView;
    private RecyclerView exerciseRecyclerView;
    private ExerciseRecyclerViewAdapter adapter;
    private WorkoutJournal workoutJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workoutJournal = new WorkoutJournal(getApplicationContext().getFilesDir());

        Intent intent = getIntent();
        workout = intent.getParcelableExtra("WORKOUT");

        setTitle(workout.getWorkoutName());

        emptyRecyclerView = findViewById(R.id.empty_recycler_view);

        buildRecyclerView(workout);
    }

    // Adds save button (check mark) to action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.workout_action_bar, menu);
        return true;
    }

    // Handles which action bar button was pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                // set focus to trigger the focus listeners in the CustomEditTexts
                exerciseRecyclerView.requestFocus();

                hideKeyboard(this, emptyRecyclerView);

                workoutJournal.updateWorkout(workout);

                Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show();

                return true;
            }
            case R.id.action_delete: {
                exerciseRecyclerView.requestFocus();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm deletion").setMessage("Are you sure you want to delete" +
                        " this workout?");

                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    workoutJournal.removeWorkout(workout);

                    Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(intent);
                });

                builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                });

                builder.show();

                return true;
            }
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                // unrecognized button pressed
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void buildRecyclerView(Workout workout) {
        // get RecyclerView from this activity
        exerciseRecyclerView = findViewById(R.id.recycler_view_exercise);

        // improves performance if size of RecyclerView content is fixed
        // taken from developer.android.com
        exerciseRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        exerciseRecyclerView.setLayoutManager(layoutManager);

        // add divider
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        exerciseRecyclerView.addItemDecoration(itemDecoration);

        // Create adapter and set its data set to workout
        adapter = new ExerciseRecyclerViewAdapter(workout, this);

        // Set up swipe to dismiss and ability to move RecyclerView items around

        // Create callback object for ItemTouchHelper
        ItemTouchHelper.Callback callback = new CustomItemTouchHelperCallback(adapter);

        // Implement object created above
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(exerciseRecyclerView);

        if (adapter.getItemCount() == 0)
        {
            showEmptyRecyclerViewText();
        }
        else
        {
            exerciseRecyclerView.setAdapter(adapter);
        }
    }

    public void showEmptyRecyclerViewText()
    {
        emptyRecyclerView.setVisibility(View.VISIBLE);
    }

    public void addNewExercise(View view)
    {
        Exercise newExercise = new Exercise("", -1, -1);
        if (exerciseRecyclerView.getAdapter() == null) {
            exerciseRecyclerView.setAdapter(adapter);
        }

        emptyRecyclerView.setVisibility(View.INVISIBLE);

        workout.addExercise(newExercise);

        adapter.notifyItemInserted(workout.getExercises().size() - 1);
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService
                (Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

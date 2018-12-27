package com.zilonkaj.workouttracker.activities;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.zilonkaj.workouttracker.custom.ExerciseRecyclerViewAdapter;
import com.zilonkaj.workouttracker.custom.ItemTouchHelperCallback;
import com.zilonkaj.workouttracker.R;
import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;
import com.zilonkaj.workouttracker.data.WorkoutJournal;

public class WorkoutActivity extends AppCompatActivity {
    private Workout workout;
    private TextView emptyRecyclerViewText;
    private RecyclerView exerciseRecyclerView;
    private ExerciseRecyclerViewAdapter adapter;

    private WorkoutJournal workoutJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workoutJournal = new WorkoutJournal(getApplicationContext().getFilesDir());

        emptyRecyclerViewText = findViewById(R.id.empty_recycler_view_text);

        workout = getIntent().getParcelableExtra("WORKOUT");

        setTitle(workout.getWorkoutName());

        buildRecyclerView(workout);
    }

    private void buildRecyclerView(Workout workout) {
        exerciseRecyclerView = findViewById(R.id.recycler_view_exercise);

        // improves performance if size of RecyclerView content is fixed
        // taken from developer.android.com
        exerciseRecyclerView.setHasFixedSize(true);

        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // add divider
        exerciseRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Create adapter and set its data to to workout
        adapter = new ExerciseRecyclerViewAdapter(workout, this);

        // Sets up swiping left/right to delete items and ability to move items around
        buildItemTouchHelper();

        if (adapter.getItemCount() == 0) {
            showEmptyRecyclerViewText();
        } else {
            exerciseRecyclerView.setAdapter(adapter);
        }
    }

    private void buildItemTouchHelper() {
        // Create custom callback object for ItemTouchHelper (this detects user swipes in
        // RecyclerView and lets the RecyclerView's adapter know they occurred)
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);

        // Attach callback object
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(exerciseRecyclerView);
    }

    // Called from this class and also from ExerciseRecyclerViewAdapter if it is empty
    public void showEmptyRecyclerViewText() {
        emptyRecyclerViewText.setVisibility(View.VISIBLE);
    }

    // Adds buttons to action bar
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
                // request focus to trigger the focus listeners in the CustomEditTexts
                exerciseRecyclerView.requestFocus();

                hideKeyboard();

                workoutJournal.updateWorkout(workout);

                Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show();

                return true;
            }
            case R.id.action_delete: {
                exerciseRecyclerView.requestFocus();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm deletion");
                builder.setMessage("Are you sure you want to delete this workout?");

                builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    workoutJournal.removeWorkout(workout);

                    Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show();

                    returnToMainActivity();
                });

                builder.setNegativeButton(android.R.string.no, (dialog, which) ->
                        dialog.dismiss());

                builder.show();

                return true;
            }
            // "home" is id for back arrow in action bar
            case android.R.id.home:
                returnToMainActivity();
                return true;
            default:
                // unrecognized button pressed
                return super.onOptionsItemSelected(item);
        }
    }

    // hideKeyboard requires a view object to work, so the choice of exerciseRecyclerView was
    // arbitrary
    private void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(exerciseRecyclerView.getWindowToken(), 0);
    }

    private void returnToMainActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void onBackPressed() {
        returnToMainActivity();
    }

    // Called from FloatingActionButton
    public void addNewExercise(View view) {
        Exercise newExercise = new Exercise("", -1, -1);

        if (exerciseRecyclerView.getAdapter() == null) {
            exerciseRecyclerView.setAdapter(adapter);
        }

        emptyRecyclerViewText.setVisibility(View.INVISIBLE);

        workout.addExercise(newExercise);

        adapter.notifyItemInserted(workout.getExercises().size() - 1);
    }
}
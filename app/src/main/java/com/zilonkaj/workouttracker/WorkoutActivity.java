package com.zilonkaj.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;
import com.zilonkaj.workouttracker.data.WorkoutJournal;

public class WorkoutActivity extends AppCompatActivity {
    private Workout workout;
    private TextView emptyRecyclerView;
    private RecyclerView exerciseRecyclerView;
    private ExerciseRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
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

    private void buildRecyclerView(Workout workout) {
        // get RecyclerView from this activity
        exerciseRecyclerView = findViewById(R.id.recycler_view_exercise);

        // improves performance if size of RecyclerView content is fixed
        // taken from developer.android.com
        exerciseRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for RecyclerView
        layoutManager = new LinearLayoutManager(this);
        exerciseRecyclerView.setLayoutManager(layoutManager);

        // Create adapter and set its data set to workout
        adapter = new ExerciseRecyclerViewAdapter(workout);

        setupOnFocusChangeListeners();

        if (adapter.getItemCount() == 0)
        {
            emptyRecyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            exerciseRecyclerView.setAdapter(adapter);
        }
    }

    private void setupOnFocusChangeListeners() {
        layoutManager.find



        adapter.ViewHolder.exerciseName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus){
                int adapterPosition = layoutManager.getPosition(v);

            }
        });
    }


    public void addNewExercise(View view)
    {
        Exercise newExercise = new Exercise("", -1, -1);
        if (exerciseRecyclerView.getAdapter() == null) {
            exerciseRecyclerView.setAdapter(adapter);
            emptyRecyclerView.setVisibility(View.INVISIBLE);
        }

        workout.addExercise(newExercise);

        adapter.notifyItemInserted(workout.getExercises().size() - 1);
    }
}

package com.zilonkaj.workouttracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zilonkaj.workouttracker.data.Workout;
import com.zilonkaj.workouttracker.data.WorkoutRecycleViewAdapter;

public class WorkoutActivity extends AppCompatActivity {
    private Workout workout;
    private RecyclerView workoutList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        workoutList = findViewById(R.id.workout_recycle_view);

        // improves performance if size of RecyclerView content is fixed
        // taken from developer.android.com
        workoutList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        workoutList.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        workout = intent.getParcelableExtra("WORKOUT_TO_ADD");

        setTitle(workout.getWorkoutName());

        populateRecycleView();
    }

    private void populateRecycleView() {
        RecyclerView workoutRecycleView = findViewById(R.id.workout_recycle_view);

        WorkoutRecycleViewAdapter adapter = new WorkoutRecycleViewAdapter(workout);
        workoutRecycleView.setAdapter(adapter);
        workoutRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}

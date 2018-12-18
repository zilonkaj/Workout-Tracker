package com.zilonkaj.workouttracker.data;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zilonkaj.workouttracker.R;

import java.util.List;

public class WorkoutRecycleViewAdapter extends
        RecyclerView.Adapter<WorkoutRecycleViewAdapter.WorkoutViewHolder>{

    private List<Exercise> exercises;

    public WorkoutRecycleViewAdapter(Workout workout)
    {
        exercises = workout.getExercises();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder
    {
        public ConstraintLayout constraintLayout;
        public TextView exerciseName;
        public TextView repCount;
        public TextView currentWeightCount;

        public WorkoutViewHolder(View view)
        {
            super(view);

            constraintLayout = view.findViewById(R.id.exercise_constraint_layout);
            exerciseName = view.findViewById(R.id.exercise_name);
            repCount = view.findViewById(R.id.exercise_reps);
            currentWeightCount = view.findViewById(R.id.exercise_weight);
        }
    }

    @Override
    public WorkoutRecycleViewAdapter.WorkoutViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View exerciseView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        // Return a new holder instance
        WorkoutViewHolder viewHolder = new WorkoutViewHolder(exerciseView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(WorkoutRecycleViewAdapter.WorkoutViewHolder viewHolder,
                                 int position) {
        // Get the data model based on position
        Exercise exercise = exercises.get(position);

        // Set item views based on your views and data model
        viewHolder.exerciseName.setText(exercise.getName());
        viewHolder.repCount.setText(String.valueOf(exercise.getReps()));
        viewHolder.currentWeightCount.setText(String.valueOf(exercise.getCurrentWeight()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if (exercises == null)
            return 0;
        else
            return exercises.size();
    }
}


package com.zilonkaj.workouttracker;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;

import java.util.List;

public class ExerciseRecyclerViewAdapter extends
        RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ViewHolder>{

    private List<Exercise> exercises;

    // pass data into this object
    public ExerciseRecyclerViewAdapter(Workout workout)
    {
        exercises = workout.getExercises();
    }

    // ViewHolder provides direct references to each view in an individual row
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ConstraintLayout constraintLayout;
        public CustomEditText exerciseName;
        public CustomEditText repCount;
        public CustomEditText currentWeightCount;

        // builds each row's view (passed in as parameter)
        public ViewHolder(View view)
        {
            super(view);

            constraintLayout = view.findViewById(R.id.exercise_constraint_layout);
            exerciseName = view.findViewById(R.id.exercise_name);
            repCount = view.findViewById(R.id.exercise_reps);
            currentWeightCount = view.findViewById(R.id.exercise_weight);
        }
    }

    // inflates XML and returns ViewHolder object
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View exerciseView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(exerciseView);
    }

    // Involves populating data into the item through ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        /*
        Get correct Exercise object based on which position the ViewHolder is in
        the RecycleView (so if ViewHolder is 4th in the RecycleView, we grab the 4th
        item in the list
        */
        Exercise exercise = exercises.get(position);

        String repCountText;
        String currentWeightCountText;

        if (exercise.getReps() == -1) {
            repCountText = "";
        }
        else {
            repCountText = String.valueOf(exercise.getReps()) + " reps";
        }

        if (exercise.getCurrentWeight() == -1) {
            currentWeightCountText = "";
        }
        else {
            currentWeightCountText = String.valueOf(exercise.getCurrentWeight()) + " lbs";
        }

        // Set item views
        viewHolder.exerciseName.setText(exercise.getName());
        viewHolder.currentWeightCount.setText(currentWeightCountText);
        viewHolder.repCount.setText(repCountText);
    }

    @Override
    public int getItemCount() {
        if (exercises == null)
            return 0;
        else
            return exercises.size();
    }
}


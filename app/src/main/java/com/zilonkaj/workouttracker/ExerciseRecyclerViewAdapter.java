package com.zilonkaj.workouttracker;

import android.app.AlertDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.TextView;

import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;

import java.util.Collections;
import java.util.List;

public class ExerciseRecyclerViewAdapter extends
        RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {

    private List<Exercise> exercises;
    private final WorkoutActivity parentActivity;

    // pass data into this object
    ExerciseRecyclerViewAdapter(Workout workout, WorkoutActivity parentActivity) {
        exercises = workout.getExercises();
        this.parentActivity = parentActivity;
    }

    // ViewHolder provides direct references to each view in an individual row
    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        CustomEditText exerciseName;
        CustomEditText repCount;
        CustomEditText currentWeightCount;
        TextView lbs;
        TextView reps;

        // builds each row's view (passed in as parameter)
        ViewHolder(View view) {
            super(view);

            constraintLayout = view.findViewById(R.id.exercise_constraint_layout);
            exerciseName = view.findViewById(R.id.exercise_name);
            lbs = view.findViewById(R.id.lbs);
            currentWeightCount = view.findViewById(R.id.exercise_weight);
            repCount = view.findViewById(R.id.exercise_reps);
            reps = view.findViewById(R.id.reps);

        }

        void showLbsTextView()
        {
            lbs.setVisibility(View.VISIBLE);
        }

        void hideLbsTextView()
        {
            lbs.setVisibility(View.INVISIBLE);
        }

        void showRepsTextView()
        {
            reps.setVisibility(View.VISIBLE);
        }

        void hideRepsTextView()
        {
            reps.setVisibility(View.INVISIBLE);
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

        String currentWeightCountText;
        String repCountText;

        if (exercise.getCurrentWeight() == -1) {
            currentWeightCountText = "";
            viewHolder.lbs.setVisibility(View.INVISIBLE);
        } else {
            currentWeightCountText = String.valueOf(exercise.getCurrentWeight());
        }

        if (exercise.getReps() == -1) {
            repCountText = "";
            viewHolder.reps.setVisibility(View.INVISIBLE);
        } else {
            repCountText = String.valueOf(exercise.getReps());
        }

        // Set item views
        viewHolder.exerciseName.setText(exercise.getName());
        viewHolder.currentWeightCount.setText(currentWeightCountText);
        viewHolder.repCount.setText(repCountText);

        setOnFocusChangeListeners(viewHolder, exercise);
        setTextChangedListeners(viewHolder);
    }

    // saves data to exercise when user updates CustomEditText
    private void setOnFocusChangeListeners(ViewHolder viewHolder, Exercise exercise) {

        viewHolder.exerciseName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && viewHolder.exerciseName.getText() != null)
                exercise.setName(viewHolder.exerciseName.getText().toString());
        });

        viewHolder.currentWeightCount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && viewHolder.currentWeightCount.getText() != null) {
                String weight = viewHolder.currentWeightCount.getText().toString();

                if (weight.equals("")) {
                    exercise.setCurrentWeight(-1);
                } else {
                    exercise.setCurrentWeight(Double.parseDouble(weight));
                }
            }
        });

        viewHolder.repCount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && viewHolder.repCount.getText() != null) {
                String reps = viewHolder.repCount.getText().toString();

                if (reps.equals("")) {
                    exercise.setReps(-1);
                } else {
                    exercise.setReps(Integer.parseInt(reps));
                }
            }
        });
    }

    private void setTextChangedListeners(ViewHolder viewHolder)
    {
        viewHolder.currentWeightCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    viewHolder.hideLbsTextView();
                } else {
                    viewHolder.showLbsTextView();
                }
            }
        });

        viewHolder.repCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    viewHolder.hideRepsTextView();
                } else {
                    viewHolder.showRepsTextView();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exercises == null)
            return 0;
        else
            return exercises.size();
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    // called when user swipes/drags items in RecyclerView
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(exercises, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(exercises, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);

        if (getItemCount() == 0) {
// Delays TextView reappearing for a couple of milliseconds
            final Handler handler = new Handler();
            handler.postDelayed(parentActivity::showEmptyRecyclerViewText, 300);
        }
    }
}


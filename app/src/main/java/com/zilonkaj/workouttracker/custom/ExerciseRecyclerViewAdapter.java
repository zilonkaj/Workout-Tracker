package com.zilonkaj.workouttracker.custom;

/*
ExerciseRecyclerViewAdapter is the adapter for WorkoutActivity's RecyclerView

ItemTouchHelperInterface allows the custom ItemTouchHelper callback object (ItemTouchHelperCallback)
to notify this adapter of any changes the user made to the RecyclerView (deletion or rearrangement)
*/

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.widget.TextView;

import com.zilonkaj.workouttracker.R;
import com.zilonkaj.workouttracker.activities.WorkoutActivity;
import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;

import java.util.Collections;
import java.util.List;

public class ExerciseRecyclerViewAdapter extends
        RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ViewHolder> implements
        ItemTouchHelperInterface {

    private final List<Exercise> exercises;
    private final WorkoutActivity workoutActivity;

    // ViewHolder provides direct references to each view in an individual row ("holds" views)
    class ViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout constraintLayout;
        final CustomEditText exerciseName;
        final CustomEditText repCount;
        final CustomEditText currentWeightCount;

        // TextViews next to the CustomEditTexts
        final TextView lbs;
        final TextView reps;

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

        void showLbsTextView() {
            lbs.setVisibility(View.VISIBLE);
        }

        void hideLbsTextView() {
            lbs.setVisibility(View.INVISIBLE);
        }

        void showRepsTextView() {
            reps.setVisibility(View.VISIBLE);
        }

        void hideRepsTextView() {
            reps.setVisibility(View.INVISIBLE);
        }
    }

    public ExerciseRecyclerViewAdapter(Workout workout, WorkoutActivity workoutActivity) {
        exercises = workout.getExercises();
        this.workoutActivity = workoutActivity;
    }

    // inflates XML and returns ViewHolder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout. Third parameter as false is required for ViewHolders
        View exerciseView = inflater.inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(exerciseView);
    }

    // Set up ViewHolders
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Get correct Exercise object based on position of ViewHolder in the RecycleView
        Exercise exercise = exercises.get(position);

        setCorrectText(viewHolder, exercise);

        setOnFocusChangeListeners(viewHolder, exercise);

        setTextChangedListeners(viewHolder);
    }

    // Sets correct text for CustomEditTexts and hides the corresponding TextViews next to them
    // (since the TextView's visibility depends on if the CustomEditText is empty or not)
    private void setCorrectText(ViewHolder viewHolder, Exercise exercise)
    {
        viewHolder.exerciseName.setText(exercise.getName());

        if (exercise.getCurrentWeight() == -1) {
            viewHolder.currentWeightCount.setText("");
            // Hide the TextView next to the CustomEditText if empty
            viewHolder.hideLbsTextView();
        } else {
            String correctWeightText = String.valueOf(exercise.getCurrentWeight());
            viewHolder.currentWeightCount.setText(correctWeightText);
        }

        if (exercise.getReps() == -1) {
            viewHolder.repCount.setText("");
            viewHolder.hideRepsTextView();
        } else {
            String correctRepsText = String.valueOf(exercise.getReps());
            viewHolder.repCount.setText(correctRepsText);
        }
    }

    // Saves data to exercise when user switches focus to different view
    private void setOnFocusChangeListeners(ViewHolder viewHolder, Exercise exercise) {
        // Save exercise name
        viewHolder.exerciseName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                exercise.setName(viewHolder.exerciseName.getText().toString());
        });

        // Save current weight
        viewHolder.currentWeightCount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String weightText = viewHolder.currentWeightCount.getText().toString();

                if (weightText.equals("")) {
                    exercise.setCurrentWeight(-1);
                } else {
                    exercise.setCurrentWeight(Double.parseDouble(weightText));
                }
            }
        });

        // Save current reps
        viewHolder.repCount.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String repsText = viewHolder.repCount.getText().toString();

                if (repsText.equals("")) {
                    exercise.setReps(-1);
                } else {
                    exercise.setReps(Integer.parseInt(repsText));
                }
            }
        });
    }

    // Shows or hides TextViews next to CustomEditTexts based on if CustomEditTexts are empty
    private void setTextChangedListeners(ViewHolder viewHolder) {
        // CustomTextWatcher is just a TextWatcher with the other two required methods already
        // overloaded
        viewHolder.currentWeightCount.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    viewHolder.hideLbsTextView();
                } else {
                    viewHolder.showLbsTextView();
                }
            }
        });

        viewHolder.repCount.addTextChangedListener(new CustomTextWatcher() {
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

    // called by ItemTouchHelperCallback when user drags items in RecyclerView
    @Override
    public boolean onItemMove(int startPosition, int endPosition) {
        if (startPosition < endPosition) {
            for (int i = startPosition; i < endPosition; i++) {
                Collections.swap(exercises, i, i + 1);
            }
        } else {
            for (int i = startPosition; i > endPosition; i--) {
                Collections.swap(exercises, i, i - 1);
            }
        }
        notifyItemMoved(startPosition, endPosition);
        return true;
    }

    // called by ItemTouchHelperCallback when user dismisses (deletes) items in RecyclerView
    @Override
    public void onItemDismiss(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);

        // If RecyclerView empty, show emptyRecyclerViewText TextView in WorkoutActivity
        // Handler delays TextView reappearing immediately
        if (getItemCount() == 0) {
            Handler handler = new Handler();
            handler.postDelayed(workoutActivity::showEmptyRecyclerViewText, 300);
        }
    }
}


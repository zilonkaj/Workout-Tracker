package com.zilonkaj.workouttracker;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;

import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;

import java.util.Collections;
import java.util.List;

enum Field {
    NAME, WEIGHT, REPS
}

public class ExerciseRecyclerViewAdapter extends
        RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {

    private List<Exercise> exercises;
    private final WorkoutActivity parentActivity;

    // pass data into this object
    public ExerciseRecyclerViewAdapter(Workout workout, WorkoutActivity parentActivity) {
        exercises = workout.getExercises();
        this.parentActivity = parentActivity;
    }

    // ViewHolder provides direct references to each view in an individual row
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public CustomEditText exerciseName;
        public CustomEditText repCount;
        public CustomEditText currentWeightCount;

        // builds each row's view (passed in as parameter)
        public ViewHolder(View view) {
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
        } else {
            repCountText = String.valueOf(exercise.getReps()) + " reps";
        }

        if (exercise.getCurrentWeight() == -1) {
            currentWeightCountText = "";
        } else {
            currentWeightCountText = String.valueOf(exercise.getCurrentWeight()) + " lbs";
        }

        // Set item views
        viewHolder.exerciseName.setText(exercise.getName());
        viewHolder.currentWeightCount.setText(currentWeightCountText);
        viewHolder.repCount.setText(repCountText);

        setOnFocusChangeListener(viewHolder.exerciseName, exercise, Field.NAME);
        setOnFocusChangeListener(viewHolder.currentWeightCount, exercise, Field.WEIGHT);
        setOnFocusChangeListener(viewHolder.repCount, exercise, Field.REPS);
    }

    // saves data to exercise when user updates CustomEditText
    private void setOnFocusChangeListener(CustomEditText editText, Exercise exercise,
                                          Field field) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && editText.getText() != null) {

                String text = editText.getText().toString();
                switch (field) {
                    case NAME: {
                        exercise.setName(text);
                        break;
                    }
                    case WEIGHT: {
                        if (!text.equals("")) {
                            exercise.setCurrentWeight(Integer.parseInt(removeLetters(text)));
                        }
                        break;
                    }
                    case REPS: {
                        if (!text.equals("")) {
                            exercise.setReps(Integer.parseInt(removeLetters(text)));
                        }
                        break;
                    }
                }
            }
        });
    }

    private String removeLetters(String string) {
        if (string != null) {
            StringBuilder newText = new StringBuilder(string);

            for (int i = 0; i < newText.length(); i++) {
                if (Character.isLetter(newText.charAt(i)) || newText.charAt(i) == ' ') {
                    newText.deleteCharAt(i);
                }
            }
            return newText.toString();
        }
        return null;
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

        if (getItemCount() == 0)
        {
            // Delays TextView reappearing for a couple of milliseconds
            final Handler handler = new Handler();
            handler.postDelayed(parentActivity::showEmptyRecyclerViewText, 300);
        }
    }
}


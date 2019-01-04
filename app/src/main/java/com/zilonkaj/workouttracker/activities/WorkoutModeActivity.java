package com.zilonkaj.workouttracker.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.zilonkaj.workouttracker.R;
import com.zilonkaj.workouttracker.data.Exercise;
import com.zilonkaj.workouttracker.data.Workout;
import com.zilonkaj.workouttracker.data.WorkoutJournal;

public class WorkoutModeActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_mode);

        Intent intent = getIntent();
        Workout workout = intent.getParcelableExtra("WORKOUT");

        setTitle(workout.getWorkoutName());

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        ExercisePagerAdapter exercisePagerAdapter =
                new ExercisePagerAdapter(getSupportFragmentManager(), workout);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(exercisePagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ExerciseFragment extends Fragment {

        public static ExerciseFragment newInstance(Exercise exercise) {
            ExerciseFragment fragment = new ExerciseFragment();

            Bundle args = new Bundle();
            args.putParcelable("EXERCISE", exercise);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_workout_mode, container,
                    false);

            TextView exerciseNameTextView = rootView.findViewById(R.id.exercisename);
            TextView weightTextView = rootView.findViewById(R.id.weight);
            TextView repsTextView = rootView.findViewById(R.id.repcount);

            Exercise exercise = getArguments().getParcelable("EXERCISE");

            exerciseNameTextView.setText(exercise.getName());
            weightTextView.setText(String.valueOf(exercise.getCurrentWeight()));
            repsTextView.setText(String.valueOf(exercise.getReps()));

            Button bumpWeight = rootView.findViewById(R.id.bumpWeight);
            bumpWeight.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Bump weight");

                NumberPicker numberPicker = new NumberPicker(getContext());
                numberPicker.setValue(exercise.getCurrentWeight());

                builder.setView(numberPicker);

                final AlertDialog dialog = builder.create();
                dialog.show();
            });

            return rootView;
        }
    }

    public class ExercisePagerAdapter extends FragmentPagerAdapter {
        private Workout workout;

        public ExercisePagerAdapter(FragmentManager fm, Workout workout) {
            super(fm);
            this.workout = workout;
        }

        @Override
        public Fragment getItem(int position) {
            Exercise exercise = workout.getExercises().get(position);
            return ExerciseFragment.newInstance(exercise);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return workout.getExercises().size();
        }
    }
}

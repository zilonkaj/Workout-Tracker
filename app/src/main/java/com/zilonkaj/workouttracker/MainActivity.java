package com.zilonkaj.workouttracker;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // popup created when Create New Workout button is pressed
    public void create_new_workout_dialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Workout");

        // constructs the layout of the popup from popup_create_new_workout.xml
        View popup = LayoutInflater.from(this).inflate(R.layout.popup_create_new_workout,
                (ViewGroup) findViewById(android.R.id.content), false);

        // user input
        final EditText input = popup.findViewById(R.id.input);

        builder.setView(popup);

        // Set up buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //workoutName = input.getText().toString();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

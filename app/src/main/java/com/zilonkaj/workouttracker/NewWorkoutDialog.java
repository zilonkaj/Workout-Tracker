/*
Wrapper for custom AlertDialog when Create New
Workout Button is pressed
*/

package com.zilonkaj.workouttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class NewWorkoutDialog {

    private AlertDialog dialog;
    private EditText input;

    public NewWorkoutDialog(MainActivity mainActivity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        builder.setTitle("Create New Workout");

        View view = buildView(mainActivity);

        input = view.findViewById(R.id.input);

        builder.setView(view);

        // Set up buttons w/lambda callback onClick
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String userInput = input.getText().toString();
            dialog.dismiss();

            mainActivity.launchNewWorkoutWizard(userInput);
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog.cancel();
        });

        dialog = builder.create();

        // sets up keyboard to show up when dialog opens and close when
        // dialog closes
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /*
    dialog buttons are inaccessible until dialog.show() is called,
    so this show() method calls dialog.show() and immediately sets
    up the buttons (essentially continues constructor method)
    */
    public void show()
    {
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        // if text in EditText, enable OK button. else, disabled
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private View buildView(Activity activity) {
        // constructs view from dialog_create_new_workout.xml
        return LayoutInflater.from(activity).inflate(R.layout.dialog_create_new_workout, null);
    }
}

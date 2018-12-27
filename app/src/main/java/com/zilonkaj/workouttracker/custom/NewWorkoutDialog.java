package com.zilonkaj.workouttracker.custom;

// Custom AlertDialog when Create New Workout Button is pressed

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.zilonkaj.workouttracker.R;
import com.zilonkaj.workouttracker.activities.MainActivity;

public class NewWorkoutDialog {

    private final AlertDialog dialog;
    private final EditText editText;

    public NewWorkoutDialog(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Create new workout");

        View view = buildView(mainActivity);

        editText = view.findViewById(R.id.input);

        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            mainActivity.openNewWorkout(editText.getText().toString());
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        dialog = builder.create();

        // Sets keyboard to show/close when dialog opens/closes
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /*
    This warning was suppressed due to AlertDialog.Builder not having a ViewGroup at this point in
    the application. In this case, null as the second parameter is required. See for more info:
    https://possiblemobile.com/2013/05/layout-inflation-as-intended/
    */
    @SuppressLint("InflateParams")
    private View buildView(Activity activity) {
        return LayoutInflater.from(activity).inflate(R.layout.dialog_create_new_workout, null);
    }

    /*
    Dialog buttons are inaccessible (other than to set the OnClickListeners) until dialog.show()
    is called, so this method calls dialog.show() and continues setting up the buttons
    (essentially continues constructor)
    */
    public void show()
    {
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        // If text in EditText, enable OK button. Else, disabled
        editText.addTextChangedListener(new CustomTextWatcher() {
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
}

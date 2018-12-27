package com.zilonkaj.workouttracker.custom;

/*
CustomEditText is normal EditText with ability to hide cursor & keyboard when user clicks back
button or Done
CHANGE
*/

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    // EditText constructor overloads
    public CustomEditText(Context context) {
        super(context);
        addEditorActionListener();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addEditorActionListener();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addEditorActionListener();
    }

    // hide cursor and keyboard when user hits back button
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideCursorAndKeyboard();
        }

        return super.onKeyPreIme(keyCode, event);
    }

    private void hideCursorAndKeyboard() {
        clearFocus();
        hideKeyboard(getContext(), getRootView());
    }

    private void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService
                (Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // hide cursor and keyboard when user hits Done button
    private void addEditorActionListener() {
        this.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideCursorAndKeyboard();
            }
            return false;
        });
    }
}

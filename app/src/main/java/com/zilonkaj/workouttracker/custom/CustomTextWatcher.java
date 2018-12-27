package com.zilonkaj.workouttracker.custom;

/*
This class is used to reduce the boilerplate of having to overload all three methods each time
a TextWatcher is needed. This app only needs to overload the afterTextChanged method, so to remove
all the clutter I simply overload this class's afterTextChanged method
*/

import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {}
}

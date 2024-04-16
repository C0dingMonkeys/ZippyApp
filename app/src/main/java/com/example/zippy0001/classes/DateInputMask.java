package com.example.zippy0001.classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class DateInputMask implements TextWatcher {
    private String current = "";
    private String ddmmyyyy = "DDMMYYYY";
    private Calendar cal = Calendar.getInstance();
    private EditText input;

    public DateInputMask(EditText input) {
        this.input = input;
        this.input.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals(current)) {
            return;
        }

        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
        String cleanC = current.replaceAll("[^\\d.]|\\.", "");

        int cl = clean.length();
        int sel = cl;
        for (int i = 2; i <= cl && i < 6; i += 2) {
            sel++;
        }
        //Fix for pressing delete next to a forward slash
        if (clean.equals(cleanC)) sel--;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

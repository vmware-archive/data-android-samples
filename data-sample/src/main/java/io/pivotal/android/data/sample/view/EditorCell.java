/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.sample.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.pivotal.android.data.sample.R;

public class EditorCell extends LinearLayout {

    private static final int[] baseRowColours = new int[]{0xddeeff, 0xddffee};

    private TextView label1;
    private TextView label2;
    private EditText editText1;
    private EditText editText2;
    private int backgroundColour;

    public EditorCell(Context context) {
        super(context);
        init(context);
    }

    public EditorCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.editor_cell, this);
        label1 = (TextView) findViewById(R.id.label1);
        label2 = (TextView) findViewById(R.id.label2);
        editText1 = (EditText) findViewById(R.id.value1);
        editText2 = (EditText) findViewById(R.id.value2);
    }

    public void setPosition(int position) {
        this.backgroundColour = getBackgroundColour(position);
        setBackgroundColor(backgroundColour);
    }

    public void setLabels(String label1, String label2) {
        this.label1.setText(label1);
        this.label2.setText(label2);
    }

    public void setHints(String hint1, String hint2) {
        this.editText1.setHint(hint1);
        this.editText2.setHint(hint2);
    }

    public void setReadOnly(boolean isReadOnly) {
        this.editText1.setEnabled(!isReadOnly);
        this.editText2.setEnabled(!isReadOnly);
        this.editText1.setFocusable(!isReadOnly);
        this.editText2.setFocusable(!isReadOnly);
    }

    public void setValue1(String value1) {
        this.editText1.setText(value1);
    }

    public void setValue2(String value2) {
        this.editText2.setText(value2);
    }

    public String getValue1() {
        return this.editText1.getText().toString();
    }

    public Object getValue2() {
        return this.editText2.getText().toString();
    }

    public static int getBackgroundColour(int position) {
        int rowColour = position == 0 ? baseRowColours[0] : baseRowColours[1];
        if (position % 2 == 0) {
            rowColour -= 0x00111111;
        }
        return rowColour | 0xff000000;
    }

    public void focus() {
        editText1.requestFocusFromTouch();
    }
}

/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.pivotal.android.auth.Authorization;
import io.pivotal.android.data.DataObject;
import io.pivotal.android.data.DataObject.Observer;
import io.pivotal.android.data.Error;

public class DataActivity extends ActionBarActivity implements Observer {

    private EditText mEditText;
    private DataObject mObject;

    private String mLastToken;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        io.pivotal.android.auth.Logger.setup(this);
        io.pivotal.android.data.Logger.setup(this);

        mEditText = (EditText) findViewById(R.id.saved_text);

        mObject = DataObject.create(this, "objects", "key");
        mObject.addObserver(this);
    }

    @Override
    public void onChange(final String key, final String value) {
        Toast.makeText(this, "data change: " + value, Toast.LENGTH_SHORT).show();

        mEditText.setText(value);
    }

    @Override
    public void onError(final String key, final Error error) {
        Toast.makeText(this, "data error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

        if (error.isUnauthorized()) {
            Authorization.invalidateAuthToken(DataActivity.this, mLastToken);
        }
    }

    public void onFetchClicked(final View view) {
        Authorization.getAuthToken(this, new Authorization.Listener() {
            @Override
            public void onAuthorizationFailure(final String error) {
                Toast.makeText(DataActivity.this, "auth error: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthorizationComplete(final String token) {
                final String value = mObject.get(token);
                mEditText.setText(value);

                mLastToken = token;
            }
        });
    }

    public void onSaveClicked(final View view) {
        Authorization.getAuthToken(this, new Authorization.Listener() {
            @Override
            public void onAuthorizationFailure(final String error) {
                Toast.makeText(DataActivity.this, "auth error: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthorizationComplete(final String token) {
                final String text = mEditText.getText().toString();
                mObject.put(token, text);

                mLastToken = token;
            }
        });
    }

    public void onInvalidateClicked(final View view) {
        Authorization.invalidateAuthToken(DataActivity.this, mLastToken);
    }
}

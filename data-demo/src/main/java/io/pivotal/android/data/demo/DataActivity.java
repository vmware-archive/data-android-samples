/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.demo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.KeyValue;
import io.pivotal.android.data.KeyValueObject;
import io.pivotal.android.data.Response;

public class DataActivity extends ActionBarActivity {

    private EditText mEditText;
    private KeyValueObject mObject;

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        io.pivotal.android.data.Logger.setup(this);
        io.pivotal.android.auth.Logger.setup(this);

        mEditText = (EditText) findViewById(R.id.saved_text);

        mObject = KeyValueObject.create(this, "objects", "key");

//        LoaderManager.enableDebugLogging(true);

//        Data.registerTokenProvider(new TokenProvider() {
//
//            @Override
//            public String provideToken() {
//                return Auth.provideToken(this);
//            }
//
//        });
    }

    public void onFetchClicked(final View view) {
        Auth.getAccessToken(this, new Auth.Listener() {
            @Override
            public void onFailure(final Error error) {
                Toast.makeText(DataActivity.this, "auth error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(final String token, final String account) {
                fetchData(token);
            }
        });
    }

    private void fetchData(final String token) {
        mObject.get(token, new DataStore.Listener<KeyValue>() {

            @Override
            public void onResponse(Response<KeyValue> response) {
                if (response.isSuccess()) {
                    mEditText.setText(response.object.value);
                }
            }
        });
    }

    public void onSaveClicked(final View view) {
        Auth.getAccessToken(this, new Auth.Listener() {
            @Override
            public void onFailure(final Error error) {
                Toast.makeText(DataActivity.this, "auth error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(final String token, final String account) {
                putData(token);
            }
        });
    }

    private void putData(final String token) {
        final String text = mEditText.getText().toString();
        mObject.put(token, text, new DataStore.Listener<KeyValue>() {
            @Override
            public void onResponse(final Response<KeyValue> response) {
                if (response.isSuccess()) {
                    mEditText.setText(response.object.value);
                }
            }
        });
    }

    public void onDeleteClicked(final View view) {
        Auth.getAccessToken(this, new Auth.Listener() {
            @Override
            public void onFailure(final Error error) {
                Toast.makeText(DataActivity.this, "auth error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(final String token, final String account) {
                deleteData(token);
            }
        });
    }

    private void deleteData(final String token) {
        mEditText.setText("");
        mObject.delete(token, new DataStore.Listener<KeyValue>() {
            @Override
            public void onResponse(final Response<KeyValue> response) {
                if (response.isSuccess()) {
                    mEditText.setText(response.object.value);
                }
            }
        });
    }

    public void onRemoveClicked(final View view) {
        Auth.removeAllAccounts(this);
    }
}

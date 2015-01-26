/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.KeyValue;
import io.pivotal.android.data.KeyValueObject;
import io.pivotal.android.data.Response;

public class DataActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String REQUEST_CACHE = "PCFData:RequestCache";
    private static final String REQUEST_KEY = "PCFData:Requests";

    private static final String COLLECTION = "objects";
    private static final String KEY = "key";

    private CheckBox mEtags;

    private TextView mServerText;
    private TextView mCollectionText;

    private EditText mEditText;
    private EditText mRequestCacheText;

    private KeyValueObject mObject;

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        io.pivotal.android.data.Logger.setup(this);
        io.pivotal.android.auth.Logger.setup(this);

        mEtags = (CheckBox) findViewById(R.id.etag);

        mServerText = (TextView) findViewById(R.id.server);
        mServerText.setText(Config.getServiceUrl());

        mCollectionText = (TextView) findViewById(R.id.collection);
        mCollectionText.setText("Collection: " + COLLECTION + ", Key: " + KEY);

        mEditText = (EditText) findViewById(R.id.saved_text);
        mRequestCacheText = (EditText) findViewById(R.id.request_cache);

        mObject = KeyValueObject.create(this, COLLECTION, KEY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final SharedPreferences preferences = getSharedPreferences(REQUEST_CACHE, Context.MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        final SharedPreferences preferences = getSharedPreferences(REQUEST_CACHE, Context.MODE_PRIVATE);
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (REQUEST_KEY.equals(key)) {
            mRequestCacheText.setText(sharedPreferences.getString(key, ""));
        }
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
        mObject.get(token, !mEtags.isChecked(), new DataStore.Listener<KeyValue>() {

            @Override
            public void onResponse(Response<KeyValue> response) {
                if (response.isSuccess()) {
                    mEditText.setText(response.object.value);
                } else {
                    Toast.makeText(DataActivity.this, "data error: " + response.error.getMessage(), Toast.LENGTH_SHORT).show();
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
        mObject.put(token, text, !mEtags.isChecked(), new DataStore.Listener<KeyValue>() {
            @Override
            public void onResponse(final Response<KeyValue> response) {
                if (response.isSuccess()) {
                    mEditText.setText(response.object.value);
                } else {
                    Toast.makeText(DataActivity.this, "data error: " + response.error.getMessage(), Toast.LENGTH_SHORT).show();
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
        mObject.delete(token, !mEtags.isChecked(),  new DataStore.Listener<KeyValue>() {
            @Override
            public void onResponse(final Response<KeyValue> response) {
                if (response.isSuccess()) {
                    mEditText.setText(response.object.value);
                } else {
                    Toast.makeText(DataActivity.this, "data error: " + response.error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

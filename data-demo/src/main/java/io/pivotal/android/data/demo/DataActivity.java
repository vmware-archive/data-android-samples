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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.pivotal.android.auth.Accounts;
import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.KeyValue;
import io.pivotal.android.data.KeyValueObject;
import io.pivotal.android.data.Response;

public class DataActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener, CompoundButton.OnCheckedChangeListener {

    private static final String REQUEST_CACHE = "PCFData:RequestCache";
    private static final String REQUEST_KEY = "PCFData:Requests";

    private static final String COLLECTION = "objects";
    private static final String KEY = "key";

    private EditText mEditText;
    private TextView mRequestCacheText;

    private KeyValueObject mObject;

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        io.pivotal.android.data.Logger.setup(this);
        io.pivotal.android.auth.Logger.setup(this);

        final CheckBox etags = (CheckBox) findViewById(R.id.etag);
        etags.setOnCheckedChangeListener(this);

        final TextView serverText = (TextView) findViewById(R.id.server);
        serverText.setText(DataConfig.getServiceUrl());

        final TextView collectionText = (TextView) findViewById(R.id.collection);
        collectionText.setText("Collection: " + COLLECTION + ", Key: " + KEY);

        mEditText = (EditText) findViewById(R.id.saved_text);
        mRequestCacheText = (TextView) findViewById(R.id.request_cache);

        mObject = KeyValueObject.create(this, COLLECTION, KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                Accounts.removeAllAccounts(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        mObject.setShouldForceRequest(!isChecked);
    }

    private DataStore.Listener<KeyValue> mListener = new DataStore.Listener<KeyValue>() {
        @Override
        public void onResponse(final Response<KeyValue> response) {
            if (response.isFailure()) {
                Toast.makeText(DataActivity.this, "ERROR: " + response.error.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                mEditText.setText(response.object.value);
            }
        }
    };

    public void onFetchClicked(final View view) {
        mObject.get(mListener);
    }

    public void onSaveClicked(final View view) {
        final String text = mEditText.getText().toString();
        mObject.put(text, mListener);
    }

    public void onDeleteClicked(final View view) {
        mEditText.setText("");
        mObject.delete(mListener);
    }

}

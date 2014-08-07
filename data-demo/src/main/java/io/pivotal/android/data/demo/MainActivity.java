/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataStoreParameters;
import io.pivotal.android.data.DataListener;
import io.pivotal.android.data.DataObject;


public class MainActivity extends ActionBarActivity {

    private static final String CLIENT_ID = "android-client";
    private static final String AUTHORIZATION_URL = "http://ident.one.pepsi.cf-app.com";
    private static final String DATA_SERVICES_URL = "http://data-service.one.pepsi.cf-app.com";
    private static final String REDIRECT_URL = "io.pivotal.android.data://identity/oauth2callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataStore.initialize(this, new DataStoreParameters(
            CLIENT_ID, AUTHORIZATION_URL, REDIRECT_URL, DATA_SERVICES_URL
        ));
    }

    public void onAuthorizeClicked(View view) {
        DataStore.getInstance().obtainAuthorization(this);
    }

    public void fetchObject() {
        final DataObject object = new DataObject("objects");
        object.setObjectId("my-object");

        object.fetch(DataStore.getInstance().getClient(this), new DataListener() {

            @Override
            public void onSuccess(DataObject object) {
                showToast("Object retrieved successfully");
            }

            @Override
            public void onUnauthorized(DataObject object) {
                showToast("Authorization error fetching object");
            }

            @Override
            public void onFailure(DataObject object, String reason) {
                showToast(reason);
            }
        });
    }

    public void saveObject() {
        final DataObject object = new DataObject("objects");
        object.setObjectId("my-object");
        object.put("key1", "value1");
        object.put("key2", "value2");

        object.save(DataStore.getInstance().getClient(this), new DataListener() {
            @Override
            public void onSuccess(DataObject object) {
                showToast("Object saved successfully");
            }

            @Override
            public void onUnauthorized(DataObject object) {
                showToast("Authorization error saving object");
            }

            @Override
            public void onFailure(DataObject object, String reason) {
                showToast(reason);
            }
        });
    }

    public void deleteObject() {
        final DataObject object = new DataObject("objects");
        object.setObjectId("my-object");

        object.delete(DataStore.getInstance().getClient(this), new DataListener() {
            @Override
            public void onSuccess(DataObject object) {
                showToast("Object deleted successfully");
            }

            @Override
            public void onUnauthorized(DataObject object) {
                showToast("Authorization error deleting object");
            }

            @Override
            public void onFailure(DataObject object, String reason) {
                showToast(reason);
            }
        });
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

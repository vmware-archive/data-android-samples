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

    private static final String CLIENT_ID = "47488d22-a2d4-47be-9c4a-7e3b1af9801d";
    private static final String CLIENT_SECRET = "AK048xmlnWdSnZYSuwcILGJBIA2xIOGKuFDqcxTNlnx2Q4VqL08MYgWS1R-gDAe1NAtYDSEj3Xtk3PC28MuRXlE";
    private static final String AUTHORIZATION_URL = "http://datasync-authentication.kona.coffee.cfms-apps.com/";
    private static final String DATA_SERVICES_URL = "http://datasync-datastore.kona.coffee.cfms-apps.com/";
    private static final String REDIRECT_URL = "io.pivotal.android.data.demo://identity/oauth2callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataStore.initialize(this, new DataStoreParameters(
            CLIENT_ID, CLIENT_SECRET, AUTHORIZATION_URL, REDIRECT_URL, DATA_SERVICES_URL
        ));
    }

    public void onAuthorizeClicked(View view) {
        DataStore.getInstance().obtainAuthorization(this);
    }

    public void onFetchClicked(View view) {
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

    public void onSaveClicked(View view) {
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

    public void onDeleteClicked(View view) {
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

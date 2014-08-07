/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.sample.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataStoreParameters;
import io.pivotal.android.data.client.AuthorizedResourceClientImpl;
import io.pivotal.android.data.sample.R;
import io.pivotal.android.data.sample.util.Preferences;
import io.pivotal.android.data.util.Logger;
import io.pivotal.android.data.util.StreamUtil;

public class MainActivity extends BaseMainActivity {

    private DataStore datastore;

    protected Class<? extends PreferencesActivity> getPreferencesActivity() {
        return PreferencesActivity.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logItems.isEmpty()) {
            addLogMessage("Press the \"Authorize\" button to obtain authorization.");
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCurrentBaseRowColour();
        try {
            DataStore.initialize(this, getDataParameters());
        } catch (Exception e) {
            addLogMessage("Could not set parameters: " + e.getLocalizedMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_authorize:
                doAuthorize();
                break;

            case R.id.action_clear_authorization:
                doClearAuthorization();
                break;

            case R.id.action_get_user_info:
                doGetUserInfo();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void doAuthorize() {
        try {
            datastore.obtainAuthorization(this);
        } catch (Exception e) {
            addLogMessage("Could not obtain authorization: '" + e + "'.");
        }
    }

    private void doClearAuthorization() {
        try {
            datastore.clearAuthorization(this);
        } catch (Exception e) {
            addLogMessage("Could not clear authorization: '" + e.getLocalizedMessage() + "'.");
        }
    }

    private void doGetUserInfo() {
        final String userInfoUrl = Preferences.getUserInfoUrl(this);
        final DataStoreParameters parameters = getDataParameters();
        try {
            datastore.getClient(this).executeHttpRequest("GET", new URL(userInfoUrl), null, "", "", null, new AuthorizedResourceClientImpl.Listener() {

                @Override
                public void onSuccess(int httpStatusCode, String contentType, String contentEncoding, InputStream result) {
                    Logger.d("GET userInfo onSuccess");
                    if (httpStatusCode >= 200 && httpStatusCode < 300) {
                        if (contentType.startsWith("application/json")) {
                            try {
                                final String responseData = StreamUtil.readInput(result);
                                Logger.d("Read user info datastore: " + responseData);
                                result.close();
                            } catch (IOException e) {
                                Logger.ex("Could not read user info response datastore", e);
                            }
                        } else {
                            Logger.e("Got invalid content type: " + contentType + ".");
                        }
                    } else {
                        Logger.e("Got error HTTP status getting user info: '" + httpStatusCode + ".");
                    }
                }

                @Override
                public void onUnauthorized() {
                    Logger.e("GET failed. Not authorized.");
                }

                @Override
                public void onFailure(String reason) {
                    Logger.e("GET userInfo onFailure reason: '" + reason + "'.");
                }
            });
        } catch (Exception e) {
            Logger.e("Could not prepare GET userInfo request. Reason: '" + e.getLocalizedMessage() + "'.");
        }
    }

    private DataStoreParameters getDataParameters() {
        return new DataStoreParameters(
            Preferences.getClientId(this),
            Preferences.getAuthorizationUrl(this),
            Preferences.getRedirectUrl(this),
            Preferences.getDataServicesUrl(this)
        );
    }
}

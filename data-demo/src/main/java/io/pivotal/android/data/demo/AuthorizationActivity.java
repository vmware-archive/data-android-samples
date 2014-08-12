/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import io.pivotal.android.data.activity.BaseAuthorizationActivity;

public class AuthorizationActivity extends BaseAuthorizationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    @Override
    public void onAuthorizationComplete() {
        showToast("Authorization completed.");
        returnToMainActivity();
    }

    @Override
    public void onAuthorizationDenied() {
        showToast("Authorization denied.");
        returnToMainActivity();
    }

    @Override
    public void onAuthorizationFailed(String reason) {
        showToast("Authorization failed: " + reason);
        returnToMainActivity();
    }

    private void returnToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AuthorizationActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

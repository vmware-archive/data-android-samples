/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.demo;

import android.content.Intent;
import android.os.Bundle;

import io.pivotal.android.data.activity.BaseAuthorizationActivity;

public class AuthorizationActivity extends BaseAuthorizationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    @Override
    public void onAuthorizationComplete() {
        returnToMainActivity();
    }

    @Override
    public void onAuthorizationDenied() {
        returnToMainActivity();
    }

    @Override
    public void onAuthorizationFailed(String reason) {
        returnToMainActivity();
    }

    private void returnToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}

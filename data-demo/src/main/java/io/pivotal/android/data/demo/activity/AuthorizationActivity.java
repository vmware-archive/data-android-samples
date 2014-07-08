package io.pivotal.android.data.demo.activity;

import android.content.Intent;
import android.os.Bundle;

import io.pivotal.android.data.activity.BaseAuthorizationActivity;
import io.pivotal.android.data.demo.R;

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
        final Intent i = new Intent(AuthorizationActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}

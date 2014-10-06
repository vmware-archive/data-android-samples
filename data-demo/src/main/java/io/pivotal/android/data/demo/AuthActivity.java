package io.pivotal.android.data.demo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import io.pivotal.android.auth.LoginActivity;
import io.pivotal.android.auth.Token;

public class AuthActivity extends LoginActivity {

    @Override
    protected void onCreateContentView(final Bundle savedInstanceState) {
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected String getUserName() {
        final EditText editText = (EditText) findViewById(R.id.login_user_name);
        return editText.getText().toString();
    }

    @Override
    protected String getPassword() {
        final EditText editText = (EditText) findViewById(R.id.login_password);
        return editText.getText().toString();
    }

    @Override
    public void onStartLoading() {
        final Button button = (Button) findViewById(R.id.login_submit);
        button.setText("Loading...");
        button.setEnabled(false);
    }

    @Override
    public void onAuthorizationComplete(Token token) {
        super.onAuthorizationComplete(token);
    }

    @Override
    public void onAuthorizationFailed(final Error error) {
        super.onAuthorizationFailed(error);

        final Button button = (Button) findViewById(R.id.login_submit);
        button.setText("Submit");
        button.setEnabled(true);
    }
}
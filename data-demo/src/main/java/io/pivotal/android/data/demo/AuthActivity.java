package io.pivotal.android.data.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.pivotal.android.auth.LoginPasswordActivity;

public class AuthActivity extends LoginPasswordActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onAuthorizationFailed(final Error error) {
        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        final Button button = (Button) findViewById(R.id.login_submit);
        button.setText("Submit");
        button.setEnabled(true);
    }

    @Override
    public void onLoginClicked(final View view) {
        super.onLoginClicked(view);
    }
}
/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.sample.activity;

import android.content.Intent;
import android.os.Bundle;

import io.pivotal.android.data.activity.BaseAuthorizationActivity;
import io.pivotal.android.data.sample.R;

public class AuthorizationActivity extends BaseAuthorizationActivity {

/*
    The functionality of this activity is taken care of by its parent class: `BaseAuthorizationActivity`.
    It is okay to be "mostly" empty - except for any code that you want to add to control the flow
    between the activities in your application.

    This activity is opened *after* your user has authenticated themselves with the Pivotal CF Mobile Service
    identity provider server.  It will be launched by the external browser and receive the redirect URL
    transmitted by the identity provider server.

    This activity does not require any user interface if you don't want.  You can simply finish it (after
    `onResume)`

    This activity must be declared in your `AndroidManifest.xml` file.

    It *MUST* have the following intent filter set up to match the _redirect URL_ passed in the
    `DataParameters` object to the `DataSDK.obtainAuthorization` method:

         <intent-filter>
             <action android:name="android.intent.action.VIEW" />
             <category android:name="android.intent.category.DEFAULT" />
             <category android:name="android.intent.category.BROWSABLE" />

             <data android:scheme="[YOUR.REDIRECT_URL.SCHEME]" />   <-- Fill the scheme in here!
             <data android:host="[YOUR.REDIRECT_URL.HOST]" />  <-- Fill the host in here!
             <data android:pathPrefix="[YOUR.REDIRECT_URL.PATH]" />  <-- Fill the path in here!

         </intent-filter>
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If you have not provided a UI for the authorization activity, then you will want to `finish`
        // it here.
    }

    /**
     * This callback is called after authorization is successfully completed.
     * You can use this opportunity to launch (or return to) the next activity in the
     * "secured" portion of your application.
     *
     * NOTE: this callback may be called on a background thread.
     */
    @Override
    public void onAuthorizationComplete() {
        showDataEditorActivity();
    }

    /**
     * This callback is called when authorization has been denied.  You can use this
     * callback to launch (or return to) some activity outside the "secured" portion
     * of your application.
     *
     * NOTE: this callback may be called on a background thread.
     */
    @Override
    public void onAuthorizationDenied() {
        returnToMainActivity();
    }

    /**
     * This callback is called after authorization has failed due to some other
     * reason (e.g.: network connectivity).  You can use this callback to launch
     * (or return to) some activity outside the "secured" portion of your application.
     *
     * NOTE: this callback may be called on a background thread.
     *
     * @param reason  The reason that the authorization attempt could not be completed.
     */
    @Override
    public void onAuthorizationFailed(String reason) {
        returnToMainActivity();
    }

    private void returnToMainActivity() {
        // NOTE: in this case, MainActivity has a `singleInstance` launch mode, so it will
        // always bring us back to the pre-existing instance of MainActivity (or start a new
        // one if necessary).
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Intent i = new Intent(AuthorizationActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void showDataEditorActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Intent i = new Intent(AuthorizationActivity.this, DataEditorActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}

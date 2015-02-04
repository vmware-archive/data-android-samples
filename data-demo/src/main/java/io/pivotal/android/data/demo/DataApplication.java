package io.pivotal.android.data.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.data.Data;
import io.pivotal.android.data.TokenProvider;

public class DataApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Data.registerTokenProvider(new TokenProvider() {

            @Override
            public String provideAccessToken(final Context context) {
                return Auth.getAccessToken(context).accessToken;
            }

            @Override
            public String provideAccessTokenWithUserPrompt(final Activity activity) {
                return Auth.getAccessTokenWithUserPrompt(activity).accessToken;
            }
        });
    }
}
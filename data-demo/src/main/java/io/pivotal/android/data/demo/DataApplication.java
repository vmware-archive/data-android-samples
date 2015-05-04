package io.pivotal.android.data.demo;

import android.app.Application;
import android.content.Context;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.auth.LogoutListener;
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
            public void invalidateAccessToken(final Context context) {
                Auth.invalidateAccessToken(context);
            }
        });

        Auth.registerLogoutListener(this, new LogoutListener() {

           @Override
           public void onLogout(final Context context) {
               Data.clearLocalCache(context);
           }
        });
    }
}
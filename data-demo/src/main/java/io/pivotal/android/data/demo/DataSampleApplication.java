package io.pivotal.android.data.demo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.data.ConnectivityListener;
import io.pivotal.android.data.Data;

public class DataSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Data.registerConnectivityListener(this, new ConnectivityListener() {

            @Override
            public void onNetworkConnected(final Context context) {
                Auth.getAccessToken(context, false, new Auth.Listener() {

                    @Override
                    public void onComplete(final String token, final String account) {
                        Data.syncInBackgroundWithAccessToken(context, token);
                    }

                    @Override
                    public void onFailure(final Error error) {
                        Toast.makeText(context, "Auth failure!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNetworkDisconnected(final Context context) {
                Toast.makeText(context, "Network was disconnected!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
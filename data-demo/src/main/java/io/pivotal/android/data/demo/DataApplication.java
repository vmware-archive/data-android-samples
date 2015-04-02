package io.pivotal.android.data.demo;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.auth.AuthError;
import io.pivotal.android.auth.LogoutListener;
import io.pivotal.android.data.Data;
import io.pivotal.android.data.TokenProvider;

public class DataApplication extends Application {
    private static AlertDialog sDialog;
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
               Log.d("Pivotal", "DataApplication:onLogout");
               Handler handler = new Handler(context.getMainLooper());
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       sDialog.hide();
                   }
               });
               Data.clearLocalCache(context);
           }
        });
    }

    public static void logout(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false).setMessage("Logging out\u2026");
        sDialog = builder.create();
        sDialog.show();

        try {
            Auth.logout(context);
        } catch(AuthError e) {
            Log.w("Pivotal", e.toString());
            sDialog.hide();
        }
    }
}
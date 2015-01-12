package io.pivotal.android.data.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import io.pivotal.android.auth.Auth;
import io.pivotal.android.data.KeyValueStore;
import io.pivotal.android.data.OfflineStore;
import io.pivotal.android.data.RequestCache;

public class DataSyncReceiver extends BroadcastReceiver {

    public void onReceive(final Context context, final Intent intent) {
        Toast.makeText(context, "Now Connected.", Toast.LENGTH_SHORT).show();

        Auth.getAccessToken(context, false, new Auth.Listener() {
            @Override
            public void onComplete(final String token, final String account) {
                RequestCache.Default cache = new RequestCache.Default(context, OfflineStore.createKeyValue(context), new KeyValueStore(context));

                cache.executePending(token);
            }

            @Override
            public void onFailure(final Error error) {}
        });
    }
}
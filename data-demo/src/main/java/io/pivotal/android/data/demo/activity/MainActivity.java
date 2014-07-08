package io.pivotal.android.data.demo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataStoreParameters;
import io.pivotal.android.data.data.DataListener;
import io.pivotal.android.data.data.DataObject;
import io.pivotal.android.data.demo.R;


public class MainActivity extends ActionBarActivity {

    private static final String CLIENT_ID = "739e8ae7-e518-4eac-b100-ceec2dd65459";
    private static final String CLIENT_SECRET = "AON8owWAztcnrnWDyOb1j_WOIS0LrnFbVoAzUATAYEjO92LGaq4ZG60-TCAxM6hAStfdrj9rY29_t6dJ_yO2Vno";

    private static final String USER_INFO_URL = "http://ident.one.pepsi.cf-app.com/userinfo";
    private static final String AUTHORIZATION_URL = "http://ident.one.pepsi.cf-app.com/oauth/authorize";
    private static final String TOKEN_URL = "http://ident.one.pepsi.cf-app.com/token";
    private static final String REDIRECT_URL = "io.pivotal.android.data://identity/oauth2callback";

    private static final String DATA_SERVICES_URL = "http://data-service.one.pepsi.cf-app.com";

    private DataStore datastore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DataStoreParameters parameters = new DataStoreParameters(
            CLIENT_ID, CLIENT_SECRET, AUTHORIZATION_URL, TOKEN_URL, REDIRECT_URL, DATA_SERVICES_URL
        );

        datastore = DataStore.getInstance();
        datastore.setParameters(this, parameters);
    }

    public void onAuthorizeClicked(final View view) {
        datastore.obtainAuthorization(this);
    }

    public void fetchObject() {
        final DataObject object = new DataObject("objects");
        object.setObjectId("my-object");

        object.fetch(datastore.getClient(this), new DataListener() {

            @Override
            public void onSuccess(final DataObject object) {
                showToast("Object retrieved successfully");
            }

            @Override
            public void onUnauthorized(DataObject object) {
                showToast("Authorization error fetching object");
            }

            @Override
            public void onFailure(DataObject object, String reason) {
                showToast(reason);
            }
        });
    }

    public void saveObject() {
        final DataObject object = new DataObject("objects");
        object.setObjectId("my-object");
        object.put("key1", "value1");
        object.put("key2", "value2");

        object.save(datastore.getClient(this), new DataListener() {
            @Override
            public void onSuccess(DataObject object) {
                showToast("Object saved successfully");
            }

            @Override
            public void onUnauthorized(DataObject object) {
                showToast("Authorization error saving object");
            }

            @Override
            public void onFailure(DataObject object, String reason) {
                showToast(reason);
            }
        });
    }

    public void deleteObject() {
        final DataObject object = new DataObject("objects");
        object.setObjectId("my-object");

        object.delete(datastore.getClient(this), new DataListener() {
            @Override
            public void onSuccess(DataObject object) {
                showToast("Object deleted successfully");
            }

            @Override
            public void onUnauthorized(DataObject object) {
                showToast("Authorization error deleting object");
            }

            @Override
            public void onFailure(DataObject object, String reason) {
                showToast(reason);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

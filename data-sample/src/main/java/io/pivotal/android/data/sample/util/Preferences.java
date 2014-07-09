/*
 * Copyright (C) 2014 Pivotal Software, Inc. All rights reserved.
 */
package io.pivotal.android.data.sample.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    public static final String CLIENT_ID = "test_client_id";
    public static final String CLIENT_SECRET = "test_client_secret";
    public static final String REDIRECT_URL = "test_redirect_url";
    public static final String AUTHORIZATION_URL = "test_authorization_url";
    public static final String TOKEN_URL = "test_token_url";
    public static final String DATA_SERVICES_URL = "test_data_services_url";
    public static final String USER_INFO_URL = "test_user_info_url";

    public static final String[] PREFERENCE_NAMES = {
            CLIENT_ID,
            CLIENT_SECRET,
            REDIRECT_URL,
            AUTHORIZATION_URL,
            TOKEN_URL,
            DATA_SERVICES_URL,
            USER_INFO_URL
    };

    public static String getClientId(Context context) {
        return getSharedPreferences(context).getString(CLIENT_ID, null);
    }

    public static String getClientSecret(Context context) {
        return getSharedPreferences(context).getString(CLIENT_SECRET, null);
    }

    public static String getRedirectUrl(Context context) {
        return getSharedPreferences(context).getString(REDIRECT_URL, null);
    }

    public static String getAuthorizationUrl(Context context) {
        return getSharedPreferences(context).getString(AUTHORIZATION_URL, null);
    }

    public static String getTokenUrl(Context context) {
        return getSharedPreferences(context).getString(TOKEN_URL, null);
    }

    public static String getDataServicesUrl(Context context) {
        return getSharedPreferences(context).getString(DATA_SERVICES_URL, null);
    }

    public static String getUserInfoUrl(Context context) {
        return getSharedPreferences(context).getString(USER_INFO_URL, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}

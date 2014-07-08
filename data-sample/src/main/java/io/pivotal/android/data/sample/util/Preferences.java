/* Copyright (c) 2013 Pivotal Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

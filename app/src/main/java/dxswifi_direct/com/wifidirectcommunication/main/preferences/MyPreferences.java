package dxswifi_direct.com.wifidirectcommunication.main.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Deepak Sharma on 5/7/16.
 * This is a shared preference class for persistance session level data storage.
 * You can add more parameters as per the requirements.
 */
public class MyPreferences {

    private static final String PREF_FILENAME = "pref_filename";
    private static final String PREF_USERNAME = "pref_username";
    private static final String PREF_PASSWORD = "pref_password";
    private static final String PREF_API_AUTH_TOKEN = "pref_api_auth_TOKEN";



    private static SharedPreferences getSharedPrefs(Context pContext) {
        return pContext.getSharedPreferences(PREF_FILENAME,
                Context.MODE_PRIVATE);
    }

    /**
     *
     * @param pContext
     * @param username
     */
    public static void setUsername(Context pContext, String username) {
        SharedPreferences.Editor editor = getSharedPrefs(pContext).edit();
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }

    /**
     *
     * @param pContext
     * @return
     */
    public static String getUsername(Context pContext) {
        return getSharedPrefs(pContext).getString(PREF_USERNAME, "");
    }


    /**
     *
     * @param pContext
     * @param password
     */
    public static void setPassword(Context pContext, String password) {
        SharedPreferences.Editor editor = getSharedPrefs(pContext).edit();
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

    /**
     *
     * @param pContext
     * @return
     */
    public static String getPassword(Context pContext) {
        return getSharedPrefs(pContext).getString(PREF_PASSWORD, "");
    }

    /**
     *
     * @param pContext
     * @param authToken
     */
    public static void setAuthToken(Context pContext, String authToken) {
        SharedPreferences.Editor editor = getSharedPrefs(pContext).edit();
        editor.putString(PREF_API_AUTH_TOKEN, authToken);
        editor.apply();
    }

    /**
     *
     * @param pContext
     * @return
     */
    public static String getAuthToken(Context pContext) {
        return getSharedPrefs(pContext).getString(PREF_API_AUTH_TOKEN, "");
    }
}

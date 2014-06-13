package com.krohnjw.trustedunlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.krohnjw.trustedunlock.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class PreferencesHelper {

    public static final String PREFS_NAME = "LockscreenPrefs";

    public static final int MODE_NONE           = 0;
    public static final int MODE_SWIPE          = 1; // TODO: Implement support for swipe lock screen
    public static final int MODE_PIN            = 2;
    public static final int MODE_PATTERN        = 3;
    public static final int MODE_PASSWORD       = 4;
    
    public static final String  PREF_LOCK_TYPE          
                                        = "prefLockType";
    public static final String  PREF_TRUSTED_DEVICES    
                                        = "prefTrustedDevices";
    
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static void storePref(Context context, String name, String value) {
        PreferencesHelper.getPreferences(context).edit().putString(name, value).commit();
    }
    
    public static String getPref(Context context, String name) {
        return PreferencesHelper.getPreferences(context).getString(name, "");
    }
    
    public static ArrayList<String> getTrustedDevices(Context context) {
        String devices = PreferencesHelper.getPreferences(context).getString(PREF_TRUSTED_DEVICES, "");
        if (devices.isEmpty()) {
            return new ArrayList<String>();
        }
        
        ArrayList<String> set = new ArrayList<String>();
        set.addAll(Arrays.asList(devices.split(",")));
        Logger.d("Returning " + set.size());
        return set;
    }
    
    public static void storeTrustedDevices(Context context, ArrayList<String> set) {
        Logger.d("Saving " + set.size());
        PreferencesHelper.getPreferences(context).edit().putString(PREF_TRUSTED_DEVICES, TextUtils.join(",", set)).commit();
    }
    
}

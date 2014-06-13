package com.krohnjw.trustedunlock.util;

import java.io.File;
import java.io.IOException;

import com.krohnjw.trustedunlock.PreferencesHelper;

import android.content.Context;

public class Lockscreen {
    
    private static final String VALUE_SALT_DISABLE     = "-6790834";
    private static final String NAME_PASSWORD_SALT     = "lockscreen.password_salt";
    
    private static final String PREF_PASSWORD_SALT     = "lockscreen.password.salt";
    
    private static final String LOCK_DATABASE          = "/data/system/locksettings.db";
    
    
    
    
    public static boolean disablePin(Context context) throws IOException, InterruptedException {
        String results = backupSalt(context);
        if (!results.isEmpty()) {
            Root.runCommandAsRoot("mv /data/system/password.key /data/system/password.key.bak");
            runQuery("update locksettings set value=" + VALUE_SALT_DISABLE + " WHERE name='" + NAME_PASSWORD_SALT + "';");
            runQuery("update locksettings set value='1' where name='lockscreen.disabled';");
            return true;
        }
        return false;
    }
    
    public static boolean enablePin(Context context) throws IOException, InterruptedException {
        File key = new File("/data/system/password.key.bak");
        if (key.exists()) {
            Logger.d("Restoring pin");
            restoreSalt(context);
            Root.runCommandAsRoot("mv /data/system/password.key.bak /data/system/password.key");
            runQuery("update locksettings set value='0' where name='lockscreen.disabled';");
            return true;
        }
        return false;
    }
    
    public static boolean disblePattern(Context context) throws IOException, InterruptedException {
        String results = backupSalt(context);
        if (!results.isEmpty()) {
            Root.runCommandAsRoot("mv /data/system/gesture.key /data/system/gesture.key.bak");
            runQuery("update locksettings set value='0' where name='lockscreen.password_type';");
            runQuery("update locksettings set value=-6790834 WHERE name='lockscreen.password_salt';");
            runQuery("update locksettings set value='1' where name='lockscreen.disabled';");
            return true;
        }
        return false;
    }
    
    public static boolean enablePattern(Context context) throws IOException, InterruptedException {
        File key = new File("/data/system/gesture.key.bak");
        if (key.exists()) {
            Logger.d("Restoring pattern");
            restoreSalt(context);
            Root.runCommandAsRoot("mv /data/system/gesture.key.bak /data/system/gesture.key");
            runQuery("update locksettings set value='65536' where name='lockscreen.password_type';");
            runQuery("update locksettings set value='0' where name='lockscreen.disabled';");
            return true;
        }
        return false;
    }
    
    public static boolean disblePassword(Context context) throws IOException, InterruptedException {
        String results = backupSalt(context);
        if (!results.isEmpty()) {
            Root.runCommandAsRoot("mv /data/system/password.key /data/system/password.key.bak");
            runQuery("update locksettings set value='0' where name='lockscreen.password_type';");
            runQuery("update locksettings set value=-6790834 WHERE name='lockscreen.password_salt';");
            runQuery("update locksettings set value='1' where name='lockscreen.disabled';");
            return true;
        }
        return false;
    }
    
    public static boolean enablePassword(Context context) throws IOException, InterruptedException {
        File key = new File("/data/system/password.key.bak");
        if (key.exists()) {
            Logger.d("Restoring password");
            restoreSalt(context);
            Root.runCommandAsRoot("mv /data/system/password.key.bak /data/system/password.key");
            runQuery("update locksettings set value=262144 where name='lockscreen.password_type';");
            runQuery("update locksettings set value='0' where name='lockscreen.disabled';");
            return true;
        }
        return false;
    }
    
    private static String backupSalt(Context context) throws IOException, InterruptedException {
        String salt = getSalt(context);
        Logger.d("Storing salt as " + salt);
        PreferencesHelper.storePref(context, PREF_PASSWORD_SALT, salt);
        return salt;
    }
    
    private static String getSalt(Context context) throws IOException, InterruptedException {
        return Root.runCommandAsRoot("sqlite3 " + LOCK_DATABASE + " \"select value from locksettings WHERE name='" + NAME_PASSWORD_SALT + "';\"");
    }
    
    private static void restoreSalt(Context context) throws IOException, InterruptedException {
        String salt = PreferencesHelper.getPref(context, PREF_PASSWORD_SALT);
        runQuery("update locksettings set value=" + salt + " WHERE name='" + NAME_PASSWORD_SALT + "';");
    }
    
    private static void runQuery(String query) throws IOException, InterruptedException {
        Root.runCommandAsRoot("sqlite3 " + LOCK_DATABASE + " \"" + query + "\"");
    }
}

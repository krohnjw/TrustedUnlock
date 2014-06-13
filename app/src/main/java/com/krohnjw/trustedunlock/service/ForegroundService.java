package com.krohnjw.trustedunlock.service;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.krohnjw.trustedunlock.PreferencesHelper;
import com.krohnjw.trustedunlock.R;

public class ForegroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(59898989, buildNotification(this));
        return START_STICKY;
    }
    
    private Notification buildNotification(Context context) {
        
        int mode = Integer.parseInt(PreferencesHelper.getPref(context, PreferencesHelper.PREF_LOCK_TYPE));
        int action = 0;
        switch(mode) {
            case PreferencesHelper.MODE_PASSWORD:
                action = LockIntentService.ACTION_ENABLE_PASSWORD;
                break;
            case PreferencesHelper.MODE_PIN:
                action = LockIntentService.ACTION_ENABLE_PIN;
                break;
            case PreferencesHelper.MODE_PATTERN:
                action = LockIntentService.ACTION_ENABLE_PATTERN;
                break;
        }
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        
        builder.setContentTitle("Trusted device paired")
        .setContentText("Tap to enable lock screen")
        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_action_lock_open_large))
        .setSmallIcon(R.drawable.ic_action_lock_open_large)
        .setAutoCancel(true)
        .setWhen(System.currentTimeMillis())
        .setTicker("Trusted device paired")
        .setContentIntent(PendingIntent.getService(context, 0, new Intent(context, LockIntentService.class).putExtra(LockIntentService.EXTRA_ACTION, action), PendingIntent.FLAG_ONE_SHOT));
        
        return builder.build();
    }
}

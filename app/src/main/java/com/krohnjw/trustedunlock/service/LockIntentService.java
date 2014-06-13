package com.krohnjw.trustedunlock.service;

import java.io.IOException;

import com.krohnjw.trustedunlock.util.Lockscreen;
import com.krohnjw.trustedunlock.util.Logger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class LockIntentService extends IntentService {

    public static String EXTRA_ACTION   = "ACTION_CHANGE_LOCK";
    
    public static final int ACTION_DISABLE_PATTERN      = 1;
    public static final int ACTION_ENABLE_PATTERN       = 2;
    public static final int ACTION_ENABLE_PIN           = 3;
    public static final int ACTION_DISABLE_PIN          = 4;
    public static final int ACTION_ENABLE_PASSWORD      = 5;
    public static final int ACTION_DISABLE_PASSWORD     = 6;
    
    public LockIntentService() {
        super("Trusted Unlock intent service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_ACTION)) {
            int action = intent.getIntExtra(EXTRA_ACTION, 0);
            try {
                switch (action) {
                    case ACTION_DISABLE_PATTERN:
                        startNotificationService(this);
                        Lockscreen.disblePattern(this);
                        
                        break;
                    case ACTION_ENABLE_PATTERN:
                        stopNotificationService(this);
                        Lockscreen.enablePattern(this);
                        
                        break;
                    case ACTION_DISABLE_PIN:
                        startNotificationService(this);
                        Lockscreen.disablePin(this);
                        
                        break;
                    case ACTION_ENABLE_PIN:
                        stopNotificationService(this);
                        Lockscreen.enablePin(this);
                        
                        break;
                    case ACTION_DISABLE_PASSWORD:
                        startNotificationService(this);
                        Lockscreen.disblePassword(this);
                        
                        break;
                    case ACTION_ENABLE_PASSWORD:
                        stopNotificationService(this);
                        Lockscreen.enablePassword(this);
                        
                        break;
                }
            } catch (IOException e1) {
                Logger.e("IO Exception changing lock screen", e1);
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                Logger.e("Interrupted Exception changing lock screen", e1);
                e1.printStackTrace();
            }
        }
        
    }
    
    private void startNotificationService(Context context) {
        context.startService(new Intent(context, ForegroundService.class));
    }
    
    private void stopNotificationService(Context context) {
        context.stopService(new Intent(context, ForegroundService.class));
    }

}

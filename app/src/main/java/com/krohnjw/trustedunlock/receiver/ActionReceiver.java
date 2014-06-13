package com.krohnjw.trustedunlock.receiver;

import com.krohnjw.trustedunlock.PreferencesHelper;
import com.krohnjw.trustedunlock.service.LockIntentService;
import com.krohnjw.trustedunlock.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActionReceiver extends BroadcastReceiver {

	public static final int ACTION_ENABLE = 1;
	public static final int ACTION_DISABLE = 0;
	
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("Received action");
        int instruction = intent.getIntExtra("ACTION", -1);
        Logger.d("Instruction is " + instruction);
        int action = 0;
        if (instruction  == ACTION_ENABLE) {

            int mode = Integer.parseInt(PreferencesHelper.getPref(context, PreferencesHelper.PREF_LOCK_TYPE));
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
        } else if (instruction == ACTION_DISABLE) {
            int mode = Integer.parseInt(PreferencesHelper.getPref(context, PreferencesHelper.PREF_LOCK_TYPE));
            
            switch(mode) {
                case PreferencesHelper.MODE_PASSWORD:
                    action = LockIntentService.ACTION_DISABLE_PASSWORD;
                    break;
                case PreferencesHelper.MODE_PIN:
                    action = LockIntentService.ACTION_DISABLE_PIN;
                    break;
                case PreferencesHelper.MODE_PATTERN:
                    action = LockIntentService.ACTION_DISABLE_PATTERN;
                    break;
            }
        }
        
        if (action != 0) {
            context.startService(new Intent(context, LockIntentService.class).putExtra(LockIntentService.EXTRA_ACTION, action));
        }
    }

}

package com.krohnjw.trustedunlock.receiver;

import java.util.List;

import com.krohnjw.trustedunlock.PreferencesHelper;
import com.krohnjw.trustedunlock.util.Bluetooth;
import com.krohnjw.trustedunlock.util.Logger;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothDisconnectReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		BluetoothDevice device =  Bluetooth.getBluetoothDevice(intent);
		if (device != null) {
			String mac = device.getAddress();
			if (mac != null) {
				/* Check if this device exists in our trusted list */
				List<String> devices = PreferencesHelper.getTrustedDevices(context);
				if ((devices != null) && (devices.contains(mac))) {
					Logger.d("Found trusted device, unlocking");
					Intent lock = new Intent("com.krohnjw.LOCK_SCREEN_ACTION");
					lock.putExtra("ACTION", ActionReceiver.ACTION_ENABLE);
					context.sendBroadcast(lock, null);
				}
			}
		}
	}

}

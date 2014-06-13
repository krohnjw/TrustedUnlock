package com.krohnjw.trustedunlock.util;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

public class Bluetooth {

    public static BluetoothDevice getBluetoothDevice(Intent intent) {
        if (!intent.hasExtra(BluetoothDevice.EXTRA_DEVICE)) {
            return null;
        }
        
        return (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    }
    
}

package com.krohnjw.trustedunlock;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.krohnjw.trustedunlock.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ArrayList<String> mTrustedDevices;
    private ArrayAdapter<BluetoothDevice> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrustedDevices = PreferencesHelper.getTrustedDevices(this);

        int mode = 0;
        try {
            mode = Integer.parseInt(PreferencesHelper.getPref(this, PreferencesHelper.PREF_LOCK_TYPE));
        } catch (Exception e) {

        }
        switch (mode) {
            case PreferencesHelper.MODE_PASSWORD:
                ((RadioButton) findViewById(R.id.password)).setChecked(true);
                break;
            case PreferencesHelper.MODE_PIN:
                ((RadioButton) findViewById(R.id.pin)).setChecked(true);
                break;
            case PreferencesHelper.MODE_PATTERN:
                ((RadioButton) findViewById(R.id.pattern)).setChecked(true);
                break;
        }

        ((RadioGroup) findViewById(R.id.group)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                int mode = PreferencesHelper.MODE_NONE;
                switch (id) {
                    case R.id.password:
                        mode = PreferencesHelper.MODE_PASSWORD;
                        break;
                    case R.id.pin:
                        mode = PreferencesHelper.MODE_PIN;
                        break;
                    case R.id.pattern:
                        mode = PreferencesHelper.MODE_PATTERN;
                        break;
                }
                PreferencesHelper.storePref(MainActivity.this, PreferencesHelper.PREF_LOCK_TYPE, String.valueOf(mode));

            }

        });

        // Populate Bluetooth list
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            adapter.enable();
        }

        List<BluetoothDevice> existing = new ArrayList<BluetoothDevice>(adapter.getBondedDevices());
        ListView known = (ListView) findViewById(android.R.id.list);

        mAdapter = new ArrayAdapter<BluetoothDevice>(this, R.layout.list_item_device, existing) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_device, parent, false);
                }

                BluetoothDevice d = getItem(position);

                String name = d.getName();
                if (name != null) {
                    try {
                        if (name.substring(0, 1).equals("\"")) {
                            name = name.substring(1, name.length() - 1);
                        }
                    } catch (Exception e) {
                    }

                    ((TextView) convertView.findViewById(android.R.id.text1)).setText(name);

                    if (getTrustedDevices().contains(d.getAddress())) {
                        ((ImageView) convertView.findViewById(android.R.id.icon)).setVisibility(View.VISIBLE);
                    } else {
                        ((ImageView) convertView.findViewById(android.R.id.icon)).setVisibility(View.GONE);
                    }
                }
                return convertView;
            }
        };

        known.setAdapter(mAdapter);

        known.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                BluetoothDevice device = (BluetoothDevice) adapter.getItemAtPosition(position);
                if (!getTrustedDevices().contains(device.getAddress())) {
                    getTrustedDevices().add(device.getAddress());
                } else {
                    getTrustedDevices().remove(device.getAddress());
                }
                PreferencesHelper.storeTrustedDevices(MainActivity.this, getTrustedDevices());
                mAdapter.notifyDataSetChanged();

            }

        });

    }

    private ArrayList<String> getTrustedDevices() {
        if (mTrustedDevices == null) {
            mTrustedDevices = PreferencesHelper.getTrustedDevices(MainActivity.this);
        }
        Logger.d("Trusted devices is " + TextUtils.join(",", mTrustedDevices));
        return mTrustedDevices;
    }
}

package com.joyotime.qparking;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.ble.iBeaconClass;
import com.joyotime.qparking.ble.iBeaconClass.iBeacon;
import com.joyotime.qparking.view.SearchDevicesView;
import com.joyotime.qparking.widget.SimpleFooter;
import com.joyotime.qparking.widget.ZrcListView;
import com.joyotime.qparking.widget.ZrcListView.OnItemClickListener;
import com.joyotime.qparking.widget.ZrcListView.OnStartListener;

import java.util.ArrayList;

public class SearchCarLock extends Activity {
    private BluetoothAdapter mBluetoothAdapter;

    private ZrcListView listView;
    private Handler handler;
    private ArrayList<String> msgs;

    private MyAdapter adapter;

    private ImageView goback = null;

    String automaticBS;
    // String LockTaKen, _Token;
    SearchDevicesView search_device_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcarlock);
        search_device_view = (SearchDevicesView) findViewById(R.id.searchlock_device_view);
        search_device_view.setWillNotDraw(false);
        search_device_view.setSearching(true);

        Intent Data = getIntent();
        Bundle bunData = Data.getExtras();
        // LockTaKen = bunData.getString("locktaken");
        // _Token = bunData.getString("taken");

        goback = (ImageView) findViewById(R.id.search_goback);
        goback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        Intent SearchI = getIntent();
        Bundle SearchBu = SearchI.getExtras();
        automaticBS = SearchBu.getString("automatic");

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context
                .BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mBluetoothAdapter.enable();

        // 收到BLE终端数据交互的事�?
        // mBLE.setOnDataAvailableListener(mOnDataAvailable);

        listView = (ZrcListView) findViewById(R.id.zListView);
        handler = new Handler();

        // 设置默认偏移量，主要用于实现透明标题栏功能�?（可选）
        float density = getResources().getDisplayMetrics().density;
        listView.setFirstTopOffset((int) (50 * density));

        listView.setCacheColorHint(0xff33bbee);
        // 设置下拉刷新的样式（可�?，但如果没有Header则无法下拉刷新）
        // SimpleHeader header = new SimpleHeader(this);
        // header.setTextColor(0xff33bbee);
        // header.setCircleColor(0xff33bbee);
        // listView.setHeadable(header);

        // 设置加载更多的样式（可�?�?
        SimpleFooter footer = new SimpleFooter(this);
        footer.setCircleColor(0x000);
        listView.setFootable(footer);

        // 设置列表项出现动画（可�?�?
        listView.setItemAnimForTopIn(R.anim.topitem_in);
        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

        // 下拉刷新事件回调（可选）
        listView.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                // refresh();
                // scanLeDevice(true);
            }
        });

        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {

            }
        });

        adapter = new MyAdapter(this, automaticBS);
        listView.setAdapter(adapter);
        // listView.refresh(); // 主动下拉刷新
        // listView.startLoadMore();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(ZrcListView parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                final iBeacon device = adapter.getDevice(position);
                StartLock(device);
            }
        });
    }

    private void StartLock(iBeacon device) {
        String dataBlue = device.proximityUuid;
        if (dataBlue.length() >= 60)
        //&& (dataBlue.substring(10, 18)).equals("4a4f594f"))
        {
            String sSoftWare = dataBlue.substring(54, 56);
            String sHardWare = dataBlue.substring(52, 54);

            Intent data = new Intent();
            data.putExtra("macaddr", device.bluetoothAddress);
            data.putExtra("bluename", device.name);
            data.putExtra("software", sSoftWare);
            data.putExtra("hardware", sHardWare);
            setResult(100, data);
            finish();
        }
        // Intent intent = new Intent();
        // intent.setClass(SearchCarLock.this, LockSetting.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("macaddr", device.bluetoothAddress);
        // intent.putExtra("locktaken", LockTaKen);
        // intent.putExtra("taken", _Token);
        // startActivityForResult(intent, 2);
    }

    private void ShowToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, 1500);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @SuppressWarnings("unused")
    private void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
                // adapter.notifyDataSetChanged();
                // listView.setRefreshSuccess("加载成功"); // 通知加载成功
                // listView.startLoadMore(); // �?��LoadingMore功能

            }
        }, 1000);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback
			() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

            final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.addDevice(ibeacon);
                    adapter.notifyDataSetChanged();

                    // listView.setLoadMoreSuccess();
                    // listView.startLoadMore();
                    // listView.stopLoadMore();

                    // handler.postDelayed(new Runnable()
                    // {
                    // @Override
                    // public void run()
                    // {
                    // mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    // //listView.setRefreshSuccess("加载成功"); // 通知加载成功
                    // listView.setLoadMoreSuccess();
                    // }
                    // }, 10 * 1000);
                }
            });
        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        adapter.clear();
        finish();
        // mBLE.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanLeDevice(false);
        // finish();
        // mBLE.close();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        scanLeDevice(false);

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    private class MyAdapter extends BaseAdapter {
        private ArrayList<iBeacon> mLeDevices;
        private LayoutInflater mInflator;
        private Activity mContext;

        String automaticBSadp;

        public MyAdapter(Activity c, String autoBS) {
            super();
            mContext = c;
            mLeDevices = new ArrayList<iBeacon>();
            mInflator = mContext.getLayoutInflater();
            automaticBSadp = autoBS;
        }

        public void addDevice(iBeacon device) {
            if (device == null)
                return;

            Log.d("ibeacon", "bluetoothAddress = " + device.bluetoothAddress);
            for (int i = 0; i < mLeDevices.size(); i++) {
                String btAddress = mLeDevices.get(i).bluetoothAddress;
                if (btAddress.equals(device.bluetoothAddress)) {
                    mLeDevices.add(i + 1, device);
                    mLeDevices.remove(i);
                    return;
                }
            }
//			String dataBlue = device.proximityUuid;
//
//			Log.i("hzxlelp2", dataBlue.substring(10, 18));
//			if (dataBlue.length() >= 60 && (dataBlue.substring(10, 18)).equals("4a4f594f") &&
// !device.name.equals("UBO_000000000000"))
//			{
//				Log.i("hzxlelp2", device.name);
//
//				String activation = dataBlue.substring(48, 50);
//				Log.i("hzxlelp2", activation);
//				if (activation.equals("00"))
//				{
//					mLeDevices.add(device);
//				}
//			}
//			else if (dataBlue.length() >= 56 && dataBlue.substring(8, 24).equals
// ("4a4f594f54494d45") && !device.name.equals("UBO_000000000000"))
//			{
//				Log.i("hzxlelp1", device.name + "aaaaaaa");
//				String sn = dataBlue.substring(24, 40);
//				String stat = dataBlue.substring(40, 42);
//				String battery = dataBlue.substring(42, 44);
//				String time = dataBlue.substring(44, 54);
//				String activation = dataBlue.substring(54, 56);
//				mLeDevices.add(device);
//
//			}
            mLeDevices.add(device);
        }

        public iBeacon getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices == null ? 0 : mLeDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mLeDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TextView textView;
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id
						.device_address_my);
                viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name_my);
                viewHolder.deviceUUID = (TextView) convertView.findViewById(R.id.device_uuid_my);
                viewHolder.deviceMajor_Minor = (TextView) convertView.findViewById(R.id.device_Major_my);
                viewHolder.devicetxPower_RSSI = (TextView) convertView.findViewById(R.id.search_device_rssi_my);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // textView.setText(msgs.get(position));
            iBeacon device = mLeDevices.get(position);
            final String deviceName = device.name;
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            if (device.isIbeacon) {
                viewHolder.deviceName.append(" [iBeacon]");
            }
            if (device.isIbeacon) {
                viewHolder.deviceMajor_Minor.setText("major:" + device.major + ",minor:" + device.minor);
                viewHolder.devicetxPower_RSSI.setText("" + device.rssi);
            } else {
                viewHolder.devicetxPower_RSSI.setText("" + device.rssi);
            }
            viewHolder.deviceAddress.setText(device.bluetoothAddress);
            viewHolder.deviceUUID.setText(device.proximityUuid);

            // F4:B8:5E:E4:B6:5D
            // if
            // ((device.bluetoothAddress.equals("F4:B8:5E:E4:AF:FE")||device.bluetoothAddress.equals("F4:B8:5E:E4:B6:5D"))
            // && automaticBSadp.equals("true"))
            // {
            // adapter.notifyDataSetChanged();
            // StartLock(device);
            // }

            return convertView;
        }

        class ViewHolder {
            TextView deviceName;
            TextView deviceAddress;
            TextView deviceUUID;
            TextView deviceMajor_Minor;
            TextView devicetxPower_RSSI;
        }
    }

}

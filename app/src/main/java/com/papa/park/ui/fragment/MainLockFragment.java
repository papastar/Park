package com.papa.park.ui.fragment;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litesuits.bluetooth.LiteBleGattCallback;
import com.litesuits.bluetooth.LiteBluetooth;
import com.litesuits.bluetooth.conn.BleCharactCallback;
import com.litesuits.bluetooth.exception.BleException;
import com.litesuits.bluetooth.exception.hanlder.BleExceptionHandler;
import com.litesuits.bluetooth.exception.hanlder.DefaultBleExceptionHandler;
import com.litesuits.bluetooth.utils.BluetoothUtil;
import com.papa.libcommon.base.BaseFragment;
import com.papa.libcommon.util.Logger;
import com.papa.park.R;
import com.papa.park.app.Config;
import com.papa.park.ble.BleUtil;
import com.papa.park.ble.SimpleCrypto;
import com.papa.park.ble.iBeaconClass;
import com.papa.park.data.BleManager;
import com.papa.park.data.DbManager;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.adapter.PeriodAddressScanCallback;
import com.papa.park.entity.database.BleData;
import com.papa.park.utils.KeyConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLockFragment extends BaseFragment {

    private final String TAG = "blue";

    @Bind(R.id.locker_name_tv)
    TextView nameTv;
    @Bind(R.id.status_tv)
    TextView statusTv;
    @Bind(R.id.battery_tv)
    TextView batteryTv;
    @Bind(R.id.lock_img)
    ImageView lockImg;
    @Bind(R.id.lock_tv)
    TextView lockTv;
    @Bind(R.id.address_tv)
    TextView addressTv;
    @Bind(R.id.layout)
    RelativeLayout layout;
    @Bind(R.id.bottom_view)
    ImageView bottomView;
    @Bind(R.id.lock_bell_btn)
    Button lockBellBtn;
    @Bind(R.id.lock_list_btn)
    Button lockListBtn;
    @Bind(R.id.operation_container)
    LinearLayout operationContainer;

    LiteBluetooth mLiteBluetooth;
    BleExceptionHandler mBleExceptionHandler;
    private BleData mBleData;
    private boolean hasCollect = true;
    private final String UUID_CHART = "0000fff1-0000-1000-8000-00805f9b34fb";
    private String UUID_SERVICE;


    public static MainLockFragment newInstance() {
        return new MainLockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBleExceptionHandler = new DefaultBleExceptionHandler(getContext());
        mLiteBluetooth = BleManager.getInstance().getLiteBluetooth();
        mLiteBluetooth.addGattCallback(mCallback);
    }


    @Override
    protected void initViewsAndEvents() {
        setUnbindInfo();
        queryLockers();
    }

    private void queryLockers() {
        ArrayList<BleData> list = DbManager.getInstance().getBleData(UserInfoManager.getInstance
                ().getCellPhone());
        if (list != null && !list.isEmpty()) {
            startScan(list);
        } else {
//            Intent intent = new Intent(getActivity(), SearchLockActivity.class);
//            getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_SEARCH_LOCK);
        }
    }

    private void startScan(final ArrayList<BleData> list) {
        lockTv.setText("正在扫描设备");
        PeriodAddressScanCallback callback = new PeriodAddressScanCallback(list, Config
                .SCAN_TIME_OUT) {
            @Override
            public void onDeviceFound(BleData item, BluetoothDevice device, int rssi, byte[]
                    scanRecord) {
                Logger.d(TAG, "onDeviceFound:name=" + device.getName());
                mBleData = item;
                checkBeacon(device, rssi, scanRecord);
            }

            @Override
            public void onScanTimeout() {
                Logger.d(TAG, "onScanTimeout");
            }
        };
        mLiteBluetooth.startLeScan(callback);
    }


    private boolean checkBeacon(BluetoothDevice device, int rssi, byte[]
            scanRecord) {
        final iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
                scanRecord);
        if (ibeacon != null) {
            String dataBlue = ibeacon.proximityUuid;
            if (dataBlue.length() >= 56
                    && dataBlue.substring(8, 24).equals(
                    "4a4f594f54494d45")) {
                String battery = dataBlue.substring(42, 44);
                int batteryInt = Integer.parseInt(battery, 16);
                updateBattery(batteryInt);
//                String sSoftWare = dataBlue.substring(60, 62);
//                if (sSoftWare.equals("23")) {
//                    AppSetting.mSoftWare = 1;
//                    AppSetting.mHardWare = 10;
//                } else {
//                    AppSetting.mSoftWare = Integer.parseInt(sSoftWare,
//                            16);
//                    String sHardWare = dataBlue.substring(58, 60);
//                    AppSetting.mHardWare = Integer.parseInt(sHardWare,
//                            16);
//                    connectionBlue();
//                }
                connect(device);

            } else if (dataBlue.length() >= 60
                    && (dataBlue.substring(10, 18)).equals("4a4f594f")) {
                String battery = dataBlue.substring(36, 38);
                int batteryInt = Integer.parseInt(battery, 16);
                updateBattery(batteryInt);
//                String sSoftWare = dataBlue.substring(54, 56);
//                AppSetting.mSoftWare = Integer.parseInt(sSoftWare, 16);
//                String sHardWare = dataBlue.substring(52, 54);
//                AppSetting.mHardWare = Integer.parseInt(sHardWare, 16);
//                Message msgs = new Message();
//                msgs.what = 70000;
//
//                handler.sendMessage(msgs);
//                Message msg = new Message();
//                msg.what = batteryInt;
//                handler.sendMessage(msg);
                connect(device);
            }
        }
        return false;
    }

    private void connect(BluetoothDevice device) {
        lockTv.setText("正在连接车锁");
        mLiteBluetooth.connect(device, false, new LiteBleGattCallback() {
            @Override
            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                Logger.d(TAG, "onConnectSuccess:status=" + status);
                gatt.discoverServices();
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Logger.d(TAG, "onServicesDiscovered");
                BluetoothUtil.printServices(gatt);
                UUID_SERVICE = getServiceUUID(gatt);
                if (!TextUtils.isEmpty(UUID_SERVICE))
                    Logger.d(TAG, "onServicesDiscovered:UUID_SERVICE=" + UUID_SERVICE);
            }

            @Override
            public void onConnectFailure(BleException exception) {
                mBleExceptionHandler.handleException(exception);
            }

        });
    }

    private String getServiceUUID(BluetoothGatt gatt) {
        List<BluetoothGattService> services = gatt.getServices();
        for (BluetoothGattService service : services) {
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic character : characteristics) {
                if (TextUtils.equals(character.getUuid().toString(), UUID_CHART)) {
                    return service.getUuid().toString();
                }
            }
        }
        return null;
    }


    /**
     * 更新电池信息
     *
     * @param battery
     */
    private void updateBattery(int battery) {

    }

    private void setUnbindInfo() {
        nameTv.setText("车锁名称");
        statusTv.setText("未连接");
        batteryTv.setText("");
        lockTv.setText("还未绑定车锁");
        addressTv.setText("停车场地址");
    }

    public void updateLockInfo(Intent intent) {
        nameTv.setText(intent.getStringExtra(KeyConstant.KEY_NAME));
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main_lock;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick(R.id.lock_img)
    void onLockImgClick() {
        if (!hasCollect)
            return;
        String action = BleUtil.getCharAction("02", "F0", mBleData.key);
        final byte[] data = SimpleCrypto.hexStringToBytes(action);
        mLiteBluetooth.newBleConnector().withUUID(UUID.fromString(UUID_SERVICE), UUID.fromString
                (UUID_CHART), null).writeCharacteristic(data, new BleCharactCallback() {


            @Override
            public void onSuccess(BluetoothGattCharacteristic characteristic) {
                Logger.d(TAG, "write data success:value = " + Arrays.toString(characteristic
                        .getValue()));
            }

            @Override
            public void onFailure(BleException exception) {
                mBleExceptionHandler.handleException(exception);
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLiteBluetooth.isConnectingOrConnected()) {
            mLiteBluetooth.closeBluetoothGatt();
        }
        mLiteBluetooth.removeGattCallback(mCallback);
    }

    private LiteBleGattCallback mCallback = new LiteBleGattCallback() {
        @Override
        public void onConnectSuccess(BluetoothGatt gatt, int status) {
            Logger.d(TAG, "onConnectSuccess:status = " + status);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Logger.d(TAG, "onServicesDiscovered:status" + status);
        }

        @Override
        public void onConnectFailure(BleException exception) {
            Logger.d(TAG, "onConnectFailure:exception = %s", exception);
            mBleExceptionHandler.handleException(exception);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Logger.d(TAG, "onConnectionStateChange:status = " + status + ",newState=" + newState);
        }

    };
}

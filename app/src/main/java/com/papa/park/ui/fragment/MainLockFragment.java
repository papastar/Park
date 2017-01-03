package com.papa.park.ui.fragment;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.litesuits.bluetooth.conn.BleDescriptorCallback;
import com.litesuits.bluetooth.conn.LiteBleConnector;
import com.litesuits.bluetooth.exception.BleException;
import com.litesuits.bluetooth.exception.hanlder.BleExceptionHandler;
import com.litesuits.bluetooth.exception.hanlder.DefaultBleExceptionHandler;
import com.litesuits.bluetooth.utils.BluetoothUtil;
import com.papa.libcommon.base.BaseFragment;
import com.papa.libcommon.util.Logger;
import com.papa.libcommon.util.ToastUtils;
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
import com.papa.park.entity.event.BluetoothConnectEvent;
import com.papa.park.utils.KeyConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLockFragment extends BaseFragment {

    private final String TAG = "XXXX";
    private final String UUID_CHART = "0000fff1-0000-1000-8000-00805f9b34fb";
    private final String UUID_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
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
    LiteBleConnector mLiteBleConnector;
    private BleData mBleData;
    private boolean hasCollect = true;
    private String UUID_SERVICE;
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
            Logger.d(TAG, "onConnectFailure:exception = " + exception.toString());
            mBleExceptionHandler.handleException(exception);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Logger.d(TAG, "onConnectionStateChange:status = " + status + ",newState=" + newState);
        }

    };


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
        onConnectEvent();
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

                if (!TextUtils.isEmpty(UUID_SERVICE)) {
                    mLiteBleConnector = mLiteBluetooth.newBleConnector().withUUIDString
                            (UUID_SERVICE,
                                    UUID_CHART, UUID_DESCRIPTOR);
                    setDescriptorNotification();
                    Logger.d(TAG, "onServicesDiscovered:UUID_SERVICE=" + UUID_SERVICE);
                }

            }

            @Override
            public void onConnectFailure(BleException exception) {
                mBleExceptionHandler.handleException(exception);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic
                    characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                if (characteristic.getUuid().toString().equals(UUID_CHART)) {
                    characteristicChanged(characteristic);
                }
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                    characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                    characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }
        });
    }

    private void setDescriptorNotification() {
        LiteBleConnector connector = mLiteBleConnector;

        connector.withUUIDString(UUID_SERVICE, UUID_CHART, null)
                .enableCharacteristicNotification(new BleCharactCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        Logger.d(TAG, "Notification characteristic Success, DATA: " + Arrays
                                .toString(characteristic.getValue()));
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        Logger.d(TAG, "Notification characteristic failure: " + exception);
                        mBleExceptionHandler.handleException(exception);
                    }
                });


        connector.withUUIDString(UUID_SERVICE, UUID_CHART, UUID_DESCRIPTOR)
                .enableDescriptorNotification(new BleDescriptorCallback() {
                    @Override
                    public void onSuccess(BluetoothGattDescriptor descriptor) {
                        Logger.d(TAG,
                                "Notification descriptor Success, DATA: " + Arrays.toString
                                        (descriptor.getValue()));
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        Logger.d(TAG, "Notification descriptor failure : " + exception);
                        mBleExceptionHandler.handleException(exception);
                    }
                });
    }


    private void characteristicChanged(BluetoothGattCharacteristic characteristic) {
        Logger.d(TAG, "写入回调开始");
        String dataString = SimpleCrypto
                .bytesToHexString(characteristic.getValue());
        int dataStringCount = dataString.split(" ").length;
        Logger.d(TAG,
                "写入回调" + dataString.split(" ")[0] + "|"
                        + dataString.split(" ")[1] + "|"
                        + dataString.split(" ")[2]);
        if (dataStringCount == 3) {
            Logger.d(TAG, "写入回调1");
            writeCharacteristic(dataString.split(" ")[0],
                    dataString.split(" ")[1], dataString.split(" ")[2]);
            Logger.d(TAG, "写入回调2");
        }
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

    private void writeCharacteristic(String CTL, String isToken, String isAction) {
        logWriteCharacteristic(CTL, isToken, isAction);
        writeCharacteristicAction(CTL, isToken, isAction);
    }

    private void logWriteCharacteristic(String CTL, String isToken, String isAction) {
//        n_CTLString = CTL;
//        n_isToken = isToken;
//        n_isAction = isAction;

        if (CTL.equals("ff")) {
            if (isToken.equals("22")) {
                if (isAction.equals("ff")) {
                    Logger.d("操作指令 " + CTL + " ：" +
                            "Token 解析正确，写入flash正确");
                } else if (isAction.equals("aa")) {
                    Logger.d("操作指令 " + CTL + " ：" + "Token 解密失败");
                }
            } else if (isToken.equals("0a")) {
                Logger.d("操作指令 " + CTL + " ：" + "Toke解析正确，写入flash错误");
            }
        } else if (CTL.equals("01")) {
            Logger.d(TAG, "写入回调3" + CTL);
            if (isToken.equals("22")) {
                Logger.d(TAG, "写入回调4" + isToken);
                if (isAction.equals("aa")) {
                    Logger.d(TAG, "写入回调5" + isAction);
//                    Message msg = new Message();
//                    msg.what = 3005;
//                    handler.sendMessage(msg);
                    Logger.d("操作指令 " + CTL + " ：" + "Key解密失败");

//                    if (!isWriteOkBoolean) {
//                        lockBoolea = true;
//                        if (progressDialog != null) {
//                            progressDialog.hide();
//                            progressDialog.dismiss();
//                            // ShowToast("初始化失败，请重新连接车锁");
//                        }
//
//                        if (mBLE != null) {
//                            mBLE.disconnect();
//                            mBLE.close();
//                            gattCharacteristic_oadimgidentify = null;
//                            gattCharacteristic_oadimablock = null;
//                        }
//                        ISLIANJIE = false;
//
//                    }

                } else if (isAction.equals("0f")) {
                    Logger.d("操作指令 " + CTL + " ：" + "Key解析正确，车锁90°");
                } else if (isAction.equals("f0")) {
                    Logger.d("操作指令 " + CTL + " ：" + "Key解析正确，车锁0°");
                }
            }
        } else if (CTL.equals("02")) {
            if (isToken.equals("00")) {
                if (isAction.equals("aa")) {
                    Logger.d("操作指令 " + CTL + " ：" + "控制命令解密失败");
//                    if (!isWriteOkBoolean) {
//                        lockBoolea = true;
//                        progressDialog.hide();
//                        progressDialog.dismiss();
//                        ShowToast("指令非法");
//                    }
                } else if (isAction.equals("0f")) {
                    Logger.d("操作指令 " + CTL + " ：" + "控制命令下降解析正确");
                } else if (isAction.equals("f0")) {
                    Logger.d("操作指令 " + CTL + " ：" + "控制命令上升车锁正确");
                }
            }
        } else if (CTL.equals("0f")) {
            if (isToken.equals("b0")) {
                if (isAction.equals("aa")) {
                    Logger.d("操作指令 " + CTL + " ：" + "车锁未激活，状态不定");

                } else if (isAction.equals("0f")) {
                    Logger.d("操作指令 " + CTL + " ：" + "控制命令下降解析正确");

                } else if (isAction.equals("f0")) {
                    Logger.d("操作指令 " + CTL + " ：" + "控制命令下降解析正确");

                }
            } else if (isToken.equals("b1")) {
                if (isAction.equals("aa")) {
                    Logger.d("操作指令 " + CTL + " ：" + "车锁已经激活，状态不定");
                } else if (isAction.equals("0f")) {
                    Logger.d("操作指令 " + CTL + " ：" + "车锁已经激活，状态90°");
                } else if (isAction.equals("f0")) {
                    Logger.d("操作指令 " + CTL + " ：" + "车锁已经激活，状态0°");
                }
            }
        } else if (CTL.equals("aa")) {
            if (isToken.equals("ff")) {
                if (isAction.equals("ab")) {
                    Logger.d("操作指令 " + CTL + " ：" + "车锁被阻挡");
                }
            } else if (isToken.equals("00")) {
//                Message msg = new Message();
//                msg.what = 80002;
//                handler.sendMessage(msg);
                Logger.d("操作指令 " + CTL + " ：" + "车锁被认为扳动");
            } else {
//                Message msg = new Message();
//                msg.what = 80001;
//                handler.sendMessage(msg);
                Logger.d("操作指令 " + CTL + " ：" + "车锁被阻挡");
            }
        }

    }


    private void writeCharacteristicAction(String CTL, String isToken, String isAction) {
        if (CTL.equals("0f")) {
            // n_isToken="b0";
            if (isToken.equals("b0")) {
                String result = BleUtil.getCharTaken("FF", mBleData.lockToken, mBleData.sn);
                writeBlueData(result);
            } else {
                String result = BleUtil.getCharKey("01", mBleData.lockToken, mBleData.key);
                writeBlueData(result);
            }

        } else if (CTL.equals("ff")) {
            if (isAction.equals("ff")) {
                String result = BleUtil.getCharKey("01", mBleData.lockToken, mBleData.key);
                writeBlueData(result);
            } else {
//                int dqz = Integer.parseInt(n_isAction);
//                int sumz = dataSumStringArr.length;
//                if (dqz < sumz) {
//                    Log.i("bbbbbbb", "写 " + n_CTLString + dqz);
//                    boolean bRet = gattCharacteristic_char1
//                            .setValue(StringToByteArray(dataSumStringArr[dqz]));
//                    Boolean iswrite = mBLE
//                            .writeCharacteristic(gattCharacteristic_char1);
//                }
            }
        } else if (CTL.equals("01")) {
            Logger.d(TAG, isAction);
            if (isAction.equals("0f") || isAction.equals("f0")
                    || isAction.equals("aa")) {
                //isWriteOkBoolean = true;
                //Message msg = new Message();

                if (isAction.equals("0f")) {
                    updateLockerStatus(1);
                } else if (isAction.equals("f0")) {
                    updateLockerStatus(0);
                }
//                if (!lockBoolea) {
//                    handler.sendMessage(msg);
//                }
//                // if (dqz >= sumz)
//                // {
//                // Read_Char3();
//                // }
            } else {

                int dqz = Integer.parseInt(isAction);
//                int sumz = dataSumStringArr.length;
//                if (dqz < sumz) {
//                    Log.i("bbbbbbb", "写 " + n_CTLString + dqz);
//                    boolean bRet = gattCharacteristic_char1
//                            .setValue(StringToByteArray(dataSumStringArr[dqz]));
//                    Boolean iswrite = mBLE
//                            .writeCharacteristic(gattCharacteristic_char1);
//                }
//                Log.i("hzxlelp3", n_isAction + "||" + sumz);
            }
        } else if (CTL.equals("02")) {
            if (isAction.equals("f0") || isAction.equals("0f")
                    || isAction.equals("aa")) {
//                numberout = 0;
//                isclike = false;
//                if (listerTimer != null)
//                    listerTimer.cancel();
//                listerTimer = null;

                // actionHandler.removeCallbacks(actionRunnable);
                // actionRunnable=null;

            } else {

//                int dqz = Integer.parseInt(n_isAction);
//                int sumz = dataSumStringArr.length;
//                if (dqz < sumz) {
//                    Log.i("bbbbbbb", "写 " + n_CTLString + dqz);
//                    boolean bRet = gattCharacteristic_char1
//                            .setValue(StringToByteArray(dataSumStringArr[dqz]));
//                    Boolean iswrite = mBLE
//                            .writeCharacteristic(gattCharacteristic_char1);
//                }
            }

        } else if (CTL.equals("03")) {
            // lockmain_lockofforob.setEnabled(true);
            if (isAction.equals("f0") || isAction.equals("0f")) {
                // isclike = true;
                //if (!lockBoolea) {
                Message msg = new Message();
                if (isAction.equals("0f")) {
                    updateLockerStatus(1);
                    //msg.what = 30041;
                } else if (isAction.equals("f0")) {
                    updateLockerStatus(0);
                    //msg.what = 30031;
                }
                //handler.sendMessage(msg);
                //}
            }
//            actionHandler.removeCallbacks(actionRunnable);
//            actionRunnable = null;
//            numberout = 0;
//            isclike = false;
//            if (listerTimer != null)
//                listerTimer.cancel();
//            listerTimer = null;
            if (isAction.equals("aa")) {
                ToastUtils.getInstance().showToast("请重试");
//                Message msgMessage = new Message();
//                msgMessage.what = 70001;
//                handler.sendMessage(msgMessage);
            }
        }
    }


    private void writeBlueData(String data) {
        mLiteBleConnector.withUUID(UUID.fromString(UUID_SERVICE), UUID.fromString
                (UUID_CHART), null).writeCharacteristic(BleUtil.stringToByteArray(data), new
                BleCharactCallback() {
                    @Override
                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
                        Logger.d(TAG, "writeBlueData success");
                    }

                    @Override
                    public void onFailure(BleException exception) {
                        mBleExceptionHandler.handleException(exception);
                    }
                });
    }

    /**
     * @param status 0已降下，1已升起
     */
    private void updateLockerStatus(int status) {
        if (status == 1) {
            lockImg.setImageResource(R.drawable.lock_down_selector);
            lockTv.setText("车位锁已升起");
        } else {
            lockImg.setImageResource(R.drawable.lock_up_selector);
            lockTv.setText("车位锁已下降");
        }
    }


    private void onConnectEvent() {
        mRxManager.onEvent(BluetoothConnectEvent.class, new Action1<BluetoothConnectEvent>() {
            @Override
            public void call(BluetoothConnectEvent event) {
                //updateUserInfo();
            }
        });
    }
}

package com.papa.park.ui.fragment;


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

import com.papa.libcommon.base.BaseFragment;
import com.papa.park.R;
import com.papa.park.ble.BleUtil;
import com.papa.park.ble.SimpleCrypto;
import com.papa.park.ble.iBeaconClass;
import com.papa.park.data.BleManager;
import com.papa.park.data.DbManager;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.database.BleData;
import com.papa.park.ui.activity.MainActivity;
import com.papa.park.ui.activity.SearchLockActivity;
import com.papa.park.utils.KeyConstant;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.RxBleScanResult;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLockFragment extends BaseFragment {

    @Bind(R.id.name_tv)
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


    private RxBleDevice mRxBleDevice;
    private BleData mBleData;
    private RxBleClient mRxBleClient;
    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();
    private boolean hasCollect = true;
    private Observable<RxBleConnection> mConnectionObservable;
    private UUID mUUID;

    public static MainLockFragment newInstance() {
        return new MainLockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxBleClient = BleManager.getInstance().getRxBleClient();
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
            Intent intent = new Intent(getActivity(), SearchLockActivity.class);
            getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_SEARCH_LOCK);
        }
    }

    private void startScan(final ArrayList<BleData> list) {
        lockTv.setText("正在扫描设备");

//        addSubscription(mRxBleClient.scanBleDevices(), new
//                SubscriberCallBack<>(new ApiCallback<RxBleScanResult>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//
//            }
//
//            @Override
//            public void onSuccess(RxBleScanResult data) {
//                int index = checkLock(list, data.getBleDevice().getMacAddress());
//                if (index >= 0) {
//                    mBleData = list.get(index);
//                    checkBeacon(data);
//                }
//            }
//        }));

    }


    private int checkLock(final ArrayList<BleData> list, String blueAddress) {

        for (int index = 0; index < list.size(); index++) {
            if (TextUtils.equals(list.get(index).blueAddress, blueAddress))
                return index;
        }

        return -1;
    }

    private boolean checkBeacon(RxBleScanResult data) {
        final iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(data.getBleDevice()
                        .getBluetoothDevice(), data.getRssi(),
                data.getScanRecord());
        if (ibeacon != null) {
//            rxManager.unsubscrible();
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
                connect(data);

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
                connect(data);
            }
        }
        return false;
    }

    private void connect(RxBleScanResult data) {
        mRxBleDevice = mRxBleClient.getBleDevice(data.getBleDevice().getMacAddress());
        mConnectionObservable = getConnectionObservable(mRxBleDevice);
        setConnectStateChange(mRxBleDevice);

        connectionBlue();
    }


    private void connectionBlue() {
        lockTv.setText("正在连接车锁");
//        addSubscription(mConnectionObservable, new SubscriberCallBack
//                <>(new ApiCallback<RxBleConnection>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//
//            }
//
//            @Override
//            public void onSuccess(RxBleConnection data) {
//                lockTv.setText("车锁已连接");
//                hasCollect = true;
//                discovery();
//            }
//        }));
    }


    private Observable<RxBleConnection> getConnectionObservable(RxBleDevice rxBleDevice) {
        return rxBleDevice
                .establishConnection(getContext(), false)
                .takeUntil(disconnectTriggerSubject)
//                .doOnUnsubscribe(this::clearSubscription)
                .compose(new ConnectionSharingAdapter());
    }

    private void setConnectStateChange(RxBleDevice rxBleDevice) {
        addSubscription(rxBleDevice.observeConnectionStateChanges(), new
                Subscriber<RxBleConnection.RxBleConnectionState>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxBleConnection.RxBleConnectionState rxBleConnectionState) {

                    }
                });

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
//        addSubscription(mConnectionObservable.flatMap(new Func1<RxBleConnection,
//                Observable<byte[]>>() {
//            @Override
//            public Observable<byte[]> call(RxBleConnection rxBleConnection) {
//                return rxBleConnection.writeCharacteristic(mUUID, data);
//            }
//        }), new SubscriberCallBack<>(new ApiCallback<byte[]>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//
//            }
//
//            @Override
//            public void onSuccess(byte[] data) {
//
//            }
//        }));

    }


    private void discovery() {
        addSubscription(mConnectionObservable.flatMap(new Func1<RxBleConnection,
                Observable<RxBleDeviceServices>>
                () {
            @Override
            public Observable<RxBleDeviceServices> call(RxBleConnection rxBleConnection) {
                return rxBleConnection.discoverServices();
            }
        }).first(), new Subscriber<RxBleDeviceServices>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RxBleDeviceServices rxBleDeviceServices) {
                for (BluetoothGattService service : rxBleDeviceServices
                        .getBluetoothGattServices()) {
                    mUUID = service.getUuid();
                }
            }
        });
    }
}

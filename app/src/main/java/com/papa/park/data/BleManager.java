package com.papa.park.data;

import android.content.Context;

import com.litesuits.bluetooth.LiteBluetooth;
import com.papa.libcommon.util.AppUtils;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.internal.RxBleLog;

/**
 * Created by Administrator on 2016/11/13.
 */

public class BleManager {

    private static BleManager mInstance;
    private RxBleClient mRxBleClient;

    private LiteBluetooth mLiteBluetooth;


    private BleManager(Context context) {
        mRxBleClient = RxBleClient.create(context);
        RxBleClient.setLogLevel(RxBleLog.DEBUG);
        mLiteBluetooth = new LiteBluetooth(context);
    }

    public static BleManager getInstance() {
        if (mInstance == null) {
            synchronized (BleManager.class) {
                if (mInstance == null) {
                    mInstance = new BleManager(AppUtils.getAppContext());
                }
            }
        }
        return mInstance;
    }

    public RxBleClient getRxBleClient() {
        return mRxBleClient;
    }


    public LiteBluetooth getLiteBluetooth(){
        return mLiteBluetooth;
    }

}

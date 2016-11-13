package com.papa.park.data;

import android.content.Context;

import com.papa.libcommon.util.AppUtils;
import com.polidea.rxandroidble.RxBleClient;

/**
 * Created by Administrator on 2016/11/13.
 */

public class BleManager {

    private static BleManager mInstance;
    private RxBleClient mRxBleClient;


    private BleManager(Context context) {
        mRxBleClient = RxBleClient.create(context);
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

}

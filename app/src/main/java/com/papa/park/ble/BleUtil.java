package com.papa.park.ble;

import android.text.TextUtils;

import com.polidea.rxandroidble.RxBleScanResult;

/**
 * Created by Administrator on 2016/11/13.
 */

public class BleUtil {

    /**
     * 过滤器
     *
     * @param device
     * @return
     */
    public static boolean filter(iBeaconClass.iBeacon device) {
        String dataBlue = device.proximityUuid;
        if (TextUtils.isEmpty(dataBlue))
            return false;
        if (dataBlue.length() >= 60 && (dataBlue.substring(10, 18)).equals("4a4f594f") &&
                !device.name.equals("UBO_000000000000")) {
//            String activation = dataBlue.substring(48, 50);
//            if (activation.equals("00")) {
//                return true;
//            }
            return true;
        } else if (dataBlue.length() >= 56 && dataBlue.substring(8, 24).equals
                ("4a4f594f54494d45") && !device.name.equals("UBO_000000000000")) {
            return true;
        }
        return false;
    }

    public static iBeaconClass.iBeacon covert(RxBleScanResult rxBleScanResult) {
        iBeaconClass.iBeacon iBeacon = iBeaconClass.fromScanData(rxBleScanResult.getBleDevice()
                .getBluetoothDevice(), rxBleScanResult.getRssi(), rxBleScanResult.getScanRecord());
        return iBeacon;
    }

}

package com.papa.park.ble;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.papa.park.entity.bean.BleBean;
import com.polidea.rxandroidble.RxBleScanResult;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        return iBeaconClass.fromScanData(rxBleScanResult.getBleDevice()
                .getBluetoothDevice(), rxBleScanResult.getRssi(), rxBleScanResult.getScanRecord());
    }

    public static iBeaconClass.iBeacon covert(BluetoothDevice device, int rssi, byte[] scanRecord) {
        return iBeaconClass.fromScanData(device, rssi, scanRecord);
    }


    public static iBeaconClass.iBeacon covert(BleBean bean) {
        return iBeaconClass.fromScanData(bean.getDevice(), bean.getRssi(), bean.getScanRecord());
    }

    public static String getCharAction(String Type, String Action, String Key) {

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd-HH-mm");
        String date = sDateFormat.format(new Date());
        date = to16(date);
        String dataDW = SimpleCrypto.str2HexStr("CTL:") + " " + Action
                + " 0F 05 13 0a 20 " + date + " 0F 05 13 0a 25";
        byte[] Seed = stringToByteArray(SimpleCrypto.str2HexStr(SimpleCrypto
                .MD5(Key)));
        byte[] byteAction = stringToByteArray(dataDW);
        String encrypt = null;
        try {
            encrypt = SimpleCrypto.encrypt(Seed, byteAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dataWT = " BB BB BB BB " + encrypt;
        String dataSumString = "00 00 24" + dataWT;
        return getBlue(Type, dataSumString);
    }

    private static String getBlue(final String type, final String dataSumString) {
        int counts = dataSumString.length() / 48;
        counts++;
        String[] dataSumStringArr = new String[counts];
        for (int i = 0; i < counts; i++) {
            String minData;
            if (i < dataSumString.length() / 48) {
                minData = type + " 00 0" + Integer.toHexString(i + 1) + " "
                        + dataSumString.substring(i * 48, (i + 1) * 48);

            } else {
                minData = type
                        + " 00 0"
                        + Integer.toHexString(i + 1)
                        + " "
                        + dataSumString.substring(i * 48,
                        dataSumString.length());
            }
            dataSumStringArr[i] = minData;
        }

        return dataSumStringArr[0];

//        try {
//            Log.i("bbbbbbb", "写 " + type + "第一针");
//            boolean bRet = gattCharacteristic_char1
//                    .setValue(stringToByteArray(dataSumStringArr[0]));
//            Boolean iswrite = mBLE
//                    .writeCharacteristic(gattCharacteristic_char1);
//        } catch (Exception exception) {
//            String exString = exception.getMessage();
//        }

    }

    private static String to16(String data) {
        String newdataString = "";
        String[] daStrings = data.split("-");
        for (int i = 0; i < daStrings.length; i++) {
            String dt = Integer.toHexString(Integer.parseInt(daStrings[i]));
            if (dt.length() == 1) {
                dt = "0" + dt;
            }
            if (i < daStrings.length - 1) {
                newdataString += dt + " ";
            } else {
                newdataString += dt;
            }
        }

        return newdataString;
    }


    static byte[] stringToByteArray(String str) {
        String[] str_ary = str.split(" ");
        int n = str_ary.length;
        byte[] bt_ary = new byte[n];
        for (int i = 0; i < n; i++)
            bt_ary[i] = (byte) Integer.parseInt(str_ary[i], 16);
        return bt_ary;
    }
}

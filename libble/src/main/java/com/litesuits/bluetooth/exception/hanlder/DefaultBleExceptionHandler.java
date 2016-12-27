package com.litesuits.bluetooth.exception.hanlder;

import android.content.Context;
import android.util.Log;

import com.litesuits.bluetooth.LiteBluetooth;
import com.litesuits.bluetooth.exception.ConnectException;
import com.litesuits.bluetooth.exception.GattException;
import com.litesuits.bluetooth.exception.InitiatedException;
import com.litesuits.bluetooth.exception.OtherException;
import com.litesuits.bluetooth.exception.TimeoutException;

/**
 * Toast exception.
 *
 * @author MaTianyu @http://litesuits.com
 * @date 2015-11-21
 */
public class DefaultBleExceptionHandler extends BleExceptionHandler {
    private Context context;

    public DefaultBleExceptionHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onConnectException(ConnectException e) {
        Log.e(LiteBluetooth.TAG, "onConnectException:" + e.toString());
        //Toast.makeText(context, e.getDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onGattException(GattException e) {
        Log.e(LiteBluetooth.TAG, "onGattException:" + e.toString());
        //Toast.makeText(context, e.getDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onTimeoutException(TimeoutException e) {
        Log.e(LiteBluetooth.TAG, "onTimeoutException:" + e.toString());
        //Toast.makeText(context, e.getDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onInitiatedException(InitiatedException e) {
        Log.e(LiteBluetooth.TAG, "onInitiatedException:" + e.toString());
        //Toast.makeText(context, e.getDescription(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onOtherException(OtherException e) {
        Log.e(LiteBluetooth.TAG, "onOtherException:" + e.toString());
        //Toast.makeText(context, e.getDescription(), Toast.LENGTH_LONG).show();
    }
}

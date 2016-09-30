package com.papa.libcommon.util;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 * 
 *
 */
public class ToastUtils {

    private static Context mContext;
    private static ToastUtils mInstance;
    private Toast mToast;
    private ToastUtils(Context context){
        mContext=context;
    }
    public static ToastUtils getInstance(){
        if(mInstance==null){
            throw new IllegalArgumentException("Initialize ToastUtils First");
        }
        return mInstance;
    }

    public static void init(Context context){
        if (mInstance == null) {
            synchronized (ToastUtils.class) {
                if (mInstance == null) {
                    mInstance = new ToastUtils(context);
                }
            }
        }

    }


    public  void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void showToast(int resId){
        if(mToast == null) {
            mToast = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}

package com.ansai.uparking.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ansai.libcommon.rx.RxBus;
import com.ansai.libcommon.util.ToastUtils;
import com.ansai.uparking.app.Config;
import com.ansai.uparking.entity.event.RechargeEvent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Config.WX_APP_KEY);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                ToastUtils.getInstance().showToast("支付成功");
                RxBus.getInstance().post(new RechargeEvent(0));
            } else {
                ToastUtils.getInstance().showToast("支付失败");
                RxBus.getInstance().post(new RechargeEvent(-1));
            }
        }
        finish();
    }
}
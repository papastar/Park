package com.papa.park.data;

import android.content.Context;
import android.util.Log;

import com.papa.park.app.Config;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/9.
 */

public final class WechatPay {


    public static void startWechatPay(Context context, String content) {
        IWXAPI mWXAPi = WXAPIFactory.createWXAPI(context, Config.WX_APP_KEY);
        try {
            JSONObject json = new JSONObject(content);
            if (!json.has("retcode")) {
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
                req.extData = "app data"; // optional
                mWXAPi.sendReq(req);
            } else {
                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

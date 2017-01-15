package com.ansai.uparking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.ansai.libcommon.base.BaseAppCompatActivity;
import com.ansai.libcommon.base.ProgressDialogFragment;
import com.ansai.uparking.BuildConfig;
import com.ansai.uparking.R;
import com.ansai.uparking.api.ApiCallback;
import com.ansai.uparking.api.HttpManager;
import com.ansai.uparking.api.PayApi;
import com.ansai.uparking.api.SubscriberCallBack;
import com.ansai.uparking.data.UserInfoManager;
import com.ansai.uparking.data.WechatPay;
import com.ansai.uparking.entity.bean.UserInfo;
import com.ansai.uparking.entity.event.RechargeEvent;
import com.ansai.uparking.utils.JSONUtils;
import com.ansai.uparking.utils.KeyConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 *
 */
public class RechargeActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.recharge_value_gv)
    GridView mRechargeValueGv;
    @Bind(R.id.recharge_type_lv)
    ListView mRechargeTypeLv;
    @Bind(R.id.recharge_btn)
    Button mRechargeBtn;
    @Bind(R.id.activity_recharge)
    LinearLayout mActivityRecharge;

    QuickAdapter<String> mRechargeValueAdapter;
    QuickAdapter<String> mRechargeTypeAdapter;

    private int mValueIndex = 0;
    private int mTypeIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRechargeEvent();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "充值");
        mRechargeValueAdapter = new QuickAdapter<String>(this, R.layout.recharge_value_grid_item,
                getRechargeValueList()) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                TextView nameTv = helper.getView(R.id.locker_name_tv);
                nameTv.setText(item);
                if (mValueIndex == helper.getPosition()) {
                    nameTv.setBackgroundResource(R.drawable.bg_btn_normal);
                    nameTv.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    nameTv.setBackgroundResource(R.drawable.bg_common);
                    nameTv.setTextColor(ContextCompat.getColor(context, R.color.color_333333));
                }
            }
        };
        mRechargeValueGv.setAdapter(mRechargeValueAdapter);
        mRechargeValueGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mValueIndex = position;
                mRechargeValueAdapter.notifyDataSetChanged();
            }
        });


        mRechargeTypeAdapter = new QuickAdapter<String>(this, R.layout.recharge_type_list_item,
                getRechargeTypeList()) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.locker_name_tv, item);
                helper.setVisible(R.id.arrow_img, mTypeIndex == helper.getPosition());
                helper.setImageResource(R.id.logo_img, TextUtils.equals(item, getString(R.string
                        .pay_type_wechat)) ? R.drawable.img_pay_wechat : R.drawable.img_pay_alipay);
            }
        };
        mRechargeTypeLv.setAdapter(mRechargeTypeAdapter);
        mRechargeTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTypeIndex = position;
                mRechargeTypeAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private List<String> getRechargeValueList() {
        List<String> list = new ArrayList<>();
        list.add("50元");
        list.add("100元");
        list.add("200元");
        list.add("500元");
        list.add("1000元");
        list.add("其他金额");
        return list;
    }

    private List<String> getRechargeTypeList() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.pay_type_wechat));
        list.add(getString(R.string.pay_type_alipay));
        return list;
    }

    @OnClick(R.id.recharge_btn)
    void onRechargeClick() {
        String payType = mRechargeTypeAdapter.getItem(mTypeIndex);
        if (TextUtils.equals(payType, getString(R.string.pay_type_alipay)))
            payByAliPay();
        else
            payByWechatPay();
    }


    private void payByAliPay() {

    }


    private void payByWechatPay() {
        Map<String, String> map = new HashMap<>();
        map.put("payType", "wxpay");
        //map.put("cashnum", mRechargeValueAdapter.getItem(mValueIndex).replace("元", ""));
        map.put("cashnum", mRechargeValueAdapter.getItem(mValueIndex)
                .replace("元", ""));
        map.put("test", BuildConfig.DEV_MODE ? "true" : "false");
        map.put("userId", UserInfoManager.getInstance().getUserInfo()._id);

        PayApi api = HttpManager.getInstance().getApi(PayApi.class, HttpManager.PAY_URL);
        ProgressDialogFragment.showProgress(getSupportFragmentManager());
        addSubscription(api.getPayInfo(map), new SubscriberCallBack<>(new ApiCallback<String>() {

            @Override
            public void onCompleted() {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                showToast(message);
            }

            @Override
            public void onSuccess(String data) {
                String content = JSONUtils.getString(data, KeyConstant.KEY_DATA, "");
                if (!TextUtils.isEmpty(content)) {
                    WechatPay.startWechatPay(RechargeActivity.this, content);
                }
            }
        }));
    }


    private void onRechargeEvent() {
        mRxManager.onEvent(RechargeEvent.class, new Action1<RechargeEvent>() {
            @Override
            public void call(RechargeEvent rechargeEvent) {
                updateUserInfo();
            }
        });
    }


    private void updateUserInfo() {
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        addSubscription(HttpManager.getInstance().getUserApi().getUserInfo(userInfo.cellphone),
                new SubscriberCallBack<>(new ApiCallback<UserInfo>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onFailure(int code, String message, Exception e) {

                    }

                    @Override
                    public void onSuccess(UserInfo data) {
                        if (data != null) {
                            UserInfoManager.getInstance().saveUser(data);
                        }
                        setRechargeResult(0);
                    }
                }));
    }


    private void setRechargeResult(int result) {
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.KEY_DATA, result);
        setResult(RESULT_OK, intent);
        finish();
    }

}

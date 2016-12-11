package com.papa.park.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.LockerLBSListResponse;
import com.papa.park.entity.bean.LockerLBSResponse;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.utils.DialogUtil;
import com.papa.park.utils.KeyConstant;
import com.papa.park.utils.StringUtil;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

public class ReserveLockerActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.no_tv)
    TextView mNoTv;
    @Bind(R.id.time_tv)
    TextView mTimeTv;
    @Bind(R.id.name_tv)
    TextView mNameTv;
    @Bind(R.id.price_tv)
    TextView mPriceTv;
    @Bind(R.id.phone_tv)
    TextView mPhoneTv;
    @Bind(R.id.contact_phone_tv)
    TextView mContactPhoneTv;
    @Bind(R.id.payment_tv)
    TextView mPaymentTv;
    @Bind(R.id.reserve_btn)
    Button mReserveBtn;
    @Bind(R.id.activity_reserve_locker)
    LinearLayout mActivityReserveLocker;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_reserve_locker;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "预定车位");
        int id = getIntent().getIntExtra(KeyConstant.KEY_DATA, 0);
        if (id > 0)
            loadDetail(id);

    }


    private void loadDetail(int id) {
        showLoading();
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put("id", String.valueOf(id));
        Observable<LockerLBSResponse> detail = HttpManager.getInstance().getBaiduLBSApi().getDetail
                (commonParam);
        addSubscription(detail, new SubscriberCallBack<>(new ApiCallback<LockerLBSResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                showError();
            }

            @Override
            public void onSuccess(LockerLBSResponse data) {
                restore();
                if (data != null && data.getPoi() != null) {
                    bindInfo(data.getPoi());
                }
            }
        }));
    }

    private void bindInfo(LockerLBSListResponse.PoisBean content) {
        mNoTv.setText(content.getLockerParkName());
        mTimeTv.setVisibility(View.GONE);
        mNameTv.setText("车位主:" + content.getOwnerName());
        mPhoneTv.setText("车主电话:" + content.getOwnerPhone());
        mPriceTv.setText("收费标准:" + getString(R.string.rent_price_of, content.getRentFirstHourPrice
                (), content.getRentPerHourPrice()));
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        mContactPhoneTv.setText("联系电话:" + userInfo.cellphone);
    }


    @Override
    protected View getLoadingTargetView() {
        return mActivityReserveLocker;
    }


    @OnClick(R.id.reserve_btn)
    void onReserveClick() {
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        if (StringUtil.parseBigDecimal(userInfo.balance).compareTo(BigDecimal.valueOf(50)) > 0) {

        } else {
            showRechargeDialog();
        }

    }

    private void showRechargeDialog() {
        DialogUtil.showNormalDialog(this, "您的账户余额不足50元，无法预约；立刻充值余额并预约车位", "取消", "确定", new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(RechargeActivity.class, 1);
            }
        });
    }


    private void showConfirmDialog() {
        DialogUtil.showNormalDialog(this, "请确认是否开始租用，租用后开始计费", "取消", "确定", new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                int payResult = data.getIntExtra(KeyConstant.KEY_DATA, 0);
                if (payResult == 1) {

                }
            }
        }
    }
}

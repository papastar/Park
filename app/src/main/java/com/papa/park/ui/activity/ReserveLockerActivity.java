package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.LockerLBSListResponse;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.utils.DialogUtil;
import com.papa.park.utils.KeyConstant;
import com.papa.park.utils.StringUtil;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.OnClick;

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
        int uid = getIntent().getIntExtra(KeyConstant.KEY_DATA, 0);
        if (uid > 0)
            loadDetail(uid);

    }


    private void loadDetail(int uid) {
//        showLoading();
//        Observable<LockerLBSListResponse> detail = HttpManager.getInstance().getBaiduLBSApi().getDetail
//                (BaiduConfig.getCommonParam());
//        addSubscription(detail, new SubscriberCallBack<>(new ApiCallback<LockerLBSListResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                showError();
//            }
//
//            @Override
//            public void onSuccess(LockerLBSListResponse data) {
//                restore();
//                if (data != null) {
//                    List<LockerLBSListResponse.PoisBean> contents = data.getPois();
//                    if (contents != null && !contents.isEmpty()) {
//                        bindInfo(contents.get(0));
//                    }
//                }
//            }
//        }));
    }

    private void bindInfo(LockerLBSListResponse.PoisBean content) {
//        mNoTv.setText(content.getParkingNumber() + "号停车位");
//        mTimeTv.setText("发布时段:" + content.getStartTime());
//        mNameTv.setText("车位主:" + content.getContacts());
//        mPriceTv.setText("收费标准:首停一小时" + content.getFirstStopPrice() + "元");
//        mPhoneTv.setText("车主电话:" + content.getPhone());
//        mContactPhoneTv.setText("联系电话:" + content.getPhone());
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
            showDialog();
        }

    }

    private void showDialog() {
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


}

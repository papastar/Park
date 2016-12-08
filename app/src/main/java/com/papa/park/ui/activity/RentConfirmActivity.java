package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.libcommon.base.ProgressDialogFragment;
import com.papa.park.R;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.entity.bean.BaseBean;
import com.papa.park.entity.bean.LockerLBSListResponse;
import com.papa.park.entity.bean.LockerLBSResponse;
import com.papa.park.utils.KeyConstant;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

public class RentConfirmActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Bind(R.id.name_tv)
    TextView mNameTv;
    @Bind(R.id.phone_tv)
    TextView mPhoneTv;
    @Bind(R.id.park_tv)
    TextView mParkTv;
    @Bind(R.id.address_tv)
    TextView mAddressTv;
    @Bind(R.id.first_tv)
    TextView mFirstTv;
    @Bind(R.id.second_tv)
    TextView mSecondTv;
    @Bind(R.id.content_layout)
    LinearLayout mContentLayout;

    int mId = 0;

    private int mDefaultFirst = 5;
    private int mDefaultSecond = 1;

    @Override
    protected void getBundleExtras(Bundle extras) {
        mId = extras.getInt(KeyConstant.KEY_DATA);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_rent_confirm;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "发布车位");
        if (mId > 0)
            loadData(mId);
    }

    @Override
    protected View getLoadingTargetView() {
        return mContentLayout;
    }

    private void loadData(int id) {
        showLoading();

        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put("id", String.valueOf(id));
        Observable<LockerLBSResponse> locker = HttpManager.getInstance().getBaiduLBSApi().getDetail
                (commonParam);
        addSubscription(locker, new SubscriberCallBack<>(new ApiCallback<LockerLBSResponse>() {
            @Override
            public void onCompleted() {
                restore();
            }

            @Override
            public void onFailure(int code, String message) {
                showError();
            }

            @Override
            public void onSuccess(LockerLBSResponse data) {
                if (data != null && data.getPoi() != null ) {
                    updateInfo(data.getPoi());
                }
            }
        }));
    }

    private void updateInfo(LockerLBSListResponse.PoisBean bean) {
        mNameTv.setText("联系人    " + bean.getOwnerName());
        mPhoneTv.setText("手机号    " + bean.getOwnerPhone());
        mParkTv.setText("停车场    " + bean.getLockerParkName());
        mAddressTv.setText("详情地址  " + bean.getProvince() + bean.getCity() + bean.getDistrict() +
                bean.getAddress());
        updateFirst(String.valueOf(5));
        updateSecond(String.valueOf(1));
    }

    private void updateFirst(String price) {
        mFirstTv.setText("首次收费 " + price + " 元  1小时");
    }

    private void updateSecond(String price) {
        mSecondTv.setText("每增收费 " + price + " 元  1小时");
    }


    @OnClick(R.id.confirm_btn)
    void onConfirmClick() {
        updateState(mId, 2);
    }

    @OnClick(R.id.first_tv)
    void onFirstClick() {

    }

    @OnClick(R.id.second_tv)
    void onSecondClick() {

    }


    private void updateState(int id, int state) {
        ProgressDialogFragment.showProgress(getSupportFragmentManager());
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put("id", String.valueOf(id));
        commonParam.put("rentState", String.valueOf(state));
        commonParam.put("rentFirstHourPrice", String.valueOf(mDefaultFirst));
        commonParam.put("rentPerHourPrice", String.valueOf(mDefaultSecond));
        Observable<BaseBean> observable = HttpManager.getInstance().getBaiduLBSApi().updatePoi
                (commonParam);
        addSubscription(observable, new SubscriberCallBack<>(new ApiCallback<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message) {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
                showToast(message);
            }

            @Override
            public void onSuccess(BaseBean data) {
                if (data != null && data.status == 0) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }));

    }
}
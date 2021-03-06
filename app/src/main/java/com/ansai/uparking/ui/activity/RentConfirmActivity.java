package com.ansai.uparking.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ansai.libcommon.base.BaseAppCompatActivity;
import com.ansai.libcommon.base.ProgressDialogFragment;
import com.ansai.uparking.R;
import com.ansai.uparking.api.ApiCallback;
import com.ansai.uparking.api.BaiduConfig;
import com.ansai.uparking.api.HttpManager;
import com.ansai.uparking.api.SubscriberCallBack;
import com.ansai.uparking.entity.bean.BaseBean;
import com.ansai.uparking.entity.bean.LockerLBSListResponse;
import com.ansai.uparking.entity.bean.LockerLBSResponse;
import com.ansai.uparking.entity.event.RefreshEvent;
import com.ansai.uparking.utils.DialogUtil;
import com.ansai.uparking.utils.KeyConstant;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

public class RentConfirmActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Bind(R.id.locker_name_tv)
    TextView mNameTv;
    @Bind(R.id.locker_phone_tv)
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

    private String mDefaultFirst = "5";
    private String mDefaultSecond = "1";

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
            public void onFailure(int code, String message, Exception e) {
                showError();
            }

            @Override
            public void onSuccess(LockerLBSResponse data) {
                if (data != null && data.getPoi() != null) {
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
        final String[] array = getResources().getStringArray(R.array
                .first_price_array);
        DialogUtil.showListDialog(this, "首次收费", array, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDefaultFirst = array[position].replace("元", "");
                updateFirst(mDefaultFirst);
            }
        });
    }

    @OnClick(R.id.second_tv)
    void onSecondClick() {
        final String[] array = getResources().getStringArray(R.array
                .first_price_array);
        DialogUtil.showListDialog(this, "首次收费", array, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDefaultSecond = array[position].replace("元", "");
                updateSecond(mDefaultSecond);
            }
        });
    }


    private void updateState(int id, int state) {
        ProgressDialogFragment.showProgress(getSupportFragmentManager());
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put("id", String.valueOf(id));
        commonParam.put("rentState", String.valueOf(state));
        commonParam.put("rentFirstHourPrice", mDefaultFirst);
        commonParam.put("rentPerHourPrice", mDefaultSecond);
        Observable<BaseBean> observable = HttpManager.getInstance().getBaiduLBSApi().updatePoi
                (commonParam);
        addSubscription(observable, new SubscriberCallBack<>(new ApiCallback<BaseBean>() {
            @Override
            public void onCompleted() {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
                showToast(message);
            }

            @Override
            public void onSuccess(BaseBean data) {
                if (data != null && data.status == 0) {
                    mRxManager.post(new RefreshEvent());
                    finish();
                }
            }
        }));
    }
}
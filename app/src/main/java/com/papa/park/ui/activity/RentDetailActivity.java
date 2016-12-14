package com.papa.park.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.libcommon.base.ProgressDialogFragment;
import com.papa.park.R;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.ApiException;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.api.RentState;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.BaseBean;
import com.papa.park.entity.bean.LockerLBSListResponse;
import com.papa.park.entity.bean.LockerLBSResponse;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.entity.body.PoiBody;
import com.papa.park.utils.BdKeyConstant;
import com.papa.park.utils.DialogUtil;
import com.papa.park.utils.JSONUtils;
import com.papa.park.utils.KeyConstant;
import com.papa.park.utils.StringUtil;
import com.papa.park.utils.TimeUtils;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Func1;

public class RentDetailActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.no_tv)
    TextView mNoTv;
    @Bind(R.id.locker_name_tv)
    TextView mLockerNameTv;
    @Bind(R.id.locker_phone_tv)
    TextView mLockerPhoneTv;
    @Bind(R.id.locker_address_tv)
    TextView mLockerAddressTv;
    @Bind(R.id.price_tv)
    TextView mPriceTv;
    @Bind(R.id.tenant_name_tv)
    TextView mTenantNameTv;
    @Bind(R.id.tenant_phone_tv)
    TextView mTenantPhoneTv;
    @Bind(R.id.rent_state_tv)
    TextView mRentStateTv;
    @Bind(R.id.time_tv)
    TextView mTimeTv;
    @Bind(R.id.explain_tv)
    TextView mExplainTv;
    @Bind(R.id.content_layout)
    LinearLayout mContentLayout;
    @Bind(R.id.operate_btn)
    Button mOperateBtn;


    LockerLBSListResponse.PoisBean mPoisBean;

    private int mType;//0-车锁主，1-租客
    private int mId;

    @Override
    protected void getBundleExtras(Bundle extras) {
        mId = extras.getInt(KeyConstant.KEY_DATA);
        mType = extras.getInt(KeyConstant.KEY_TYPE);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_rent_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "");
        if (mId > 0)
            loadDetail(mId);
    }


    private void loadDetail(int id) {
        showLoading();
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put(BdKeyConstant.KEY_ID, String.valueOf(id));
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
                    mPoisBean = data.getPoi();
                    updateBaseInfo(mPoisBean);
                    updateInfo(mPoisBean);
                }
            }
        }));
    }

    private void updateBaseInfo(LockerLBSListResponse.PoisBean content) {
        mNoTv.setText(content.getLockerParkName());
        mLockerNameTv.setText("车位主:" + content.getOwnerName());
        mLockerPhoneTv.setText("车主电话:" + content.getOwnerPhone());
        mLockerAddressTv.setText("车锁地址:" + content.getLockerAddress());
        mPriceTv.setText("收费标准:" + getString(R.string.rent_price_of, content.getRentFirstHourPrice
                (), content.getRentPerHourPrice()));

    }


    private void updateInfo(LockerLBSListResponse.PoisBean bean) {
        int rentState = bean.getRentState();
        if (mType == 0) {
            mToolBar.setTitle("车锁详情");
            if (rentState == RentState.RENT_PUBLISH.getState()) {
                mRentStateTv.setText("状态:已发布");
                setTextAndShow(mOperateBtn, "取消发布");
                mOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消发布
                        cancelPublish(mId);
                    }
                });
            } else if (rentState == RentState.RENT_RESERVE.getState()) {
                mRentStateTv.setText("已预定");
                setTextAndShow(mTenantNameTv, "租客姓名:" + bean.getTenantName());
                setTextAndShow(mTenantPhoneTv, "租客号码:" + bean.getTenantPhone());
                setTextAndShow(mTimeTv, "预定时间:" + TimeUtils.millis2String(StringUtil.parseLong
                        (bean.getReserveStartTime())));
            } else if (rentState == RentState.RENT_PUBLISH.getState()) {
                mRentStateTv.setText("已租用");
                setTextAndShow(mTenantNameTv, "租客姓名:" + bean.getTenantName());
                setTextAndShow(mTenantPhoneTv, "租客号码:" + bean.getTenantPhone());
                setTextAndShow(mTimeTv, "起租时间:" + TimeUtils.millis2String(StringUtil.parseLong
                        (bean.getRentStartTime())));
            }
        } else {
            mExplainTv.setText(View.VISIBLE);
            if (rentState == RentState.RENT_PUBLISH.getState()) {
                mToolBar.setTitle("预定车锁");
                mRentStateTv.setText("已发布");
                setTextAndShow(mOperateBtn, "确认预定");
                mOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确认预定
                        reserveLock();
                    }
                });
            } else if (rentState == RentState.RENT_RESERVE.getState()) {
                mToolBar.setTitle("租用车锁");
                mRentStateTv.setText("已预定");
                setTextAndShow(mTenantNameTv, "租客姓名:" + bean.getTenantName());
                setTextAndShow(mTenantPhoneTv, "租客号码:" + bean.getTenantPhone());
                setTextAndShow(mTimeTv, "预定时间:" + TimeUtils.millis2String(StringUtil.parseLong
                        (bean.getReserveStartTime())));
                setTextAndShow(mOperateBtn, "确认租用");
                mOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确认预定
                        showConfirmRentDialog();
                    }
                });

            } else if (rentState == RentState.RENT_RENT.getState()) {
                mToolBar.setTitle("租用车锁");
                mRentStateTv.setText("已租用");
                setTextAndShow(mTenantNameTv, "租客姓名:" + bean.getTenantName());
                setTextAndShow(mTenantPhoneTv, "租客号码:" + bean.getTenantPhone());
                setTextAndShow(mTimeTv, "租用开始时间:" + TimeUtils.millis2String(StringUtil.parseLong
                        (bean.getReserveStartTime())));
                setTextAndShow(mOperateBtn, "控制车锁");
                mOperateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //确认预定
                        showConfirmRentDialog();
                    }
                });
            }
        }

    }

    private void setTextAndShow(TextView tv, String content) {
        tv.setVisibility(View.VISIBLE);
        tv.setText(content);
    }


    private void cancelPublish(int id) {
        ProgressDialogFragment.showProgress(getSupportFragmentManager());
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put(BdKeyConstant.KEY_ID, String.valueOf(id));
        commonParam.put(BdKeyConstant.KEY_RENT_STATE, String.valueOf(RentState.RENT_CAN.getState
                ()));
        commonParam.put(BdKeyConstant.KEY_RENT_FIRST_HOUR_PRICE, "");
        commonParam.put(BdKeyConstant.KEY_RENT_PER_HOUR_PRICE, "");
        Observable<BaseBean> observable = HttpManager.getInstance().getBaiduLBSApi().updatePoi
                (commonParam);
        addSubscription(observable, new SubscriberCallBack<>(new ApiCallback<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
                showToast(message);
            }

            @Override
            public void onSuccess(BaseBean data) {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
                if (data.status == 0) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showToast(data.message);
                }
            }
        }));
    }


    @Override
    protected View getLoadingTargetView() {
        return mContentLayout;
    }


    void reserveLock() {
        if (TextUtils.equals(mOperateBtn.getText(), "确认租用")) {
            showConfirmRentDialog();
        } else {
            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
            if (StringUtil.parseBigDecimal(userInfo.balance).compareTo(BigDecimal.valueOf(10)) >
                    0) {
                showConfirmReserveDialog();
            } else {
                showRechargeDialog();
            }
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


    private void showConfirmReserveDialog() {
        DialogUtil.showNormalDialog(this, "请确认是否预定，预定为您保留10分钟", "取消", "确定", new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveLocker();
            }
        });
    }


    private void showConfirmRentDialog() {
        DialogUtil.showNormalDialog(this, "请确认是否租用，租用开始计费", "取消", "确定", new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rentLocker();
            }
        });
    }

    private void reserveLocker() {
        if (mPoisBean == null)
            return;
        PoiBody body = new PoiBody(mPoisBean.getLockerId(), mPoisBean.getOwnerId());
        Observable<BaseBean> observable = HttpManager.getInstance().getUserApi().reserveLocker
                (body).flatMap(new Func1<String, Observable<BaseBean>>() {
            @Override
            public Observable<BaseBean> call(String s) {
                String lockerId = JSONUtils.getString(s, "_id", "");
                if (!TextUtils.isEmpty(lockerId)) {
                    Map<String, String> commonParam = BaiduConfig.getCommonParam();
                    commonParam.put(BdKeyConstant.KEY_ID, String.valueOf(mPoisBean.getId()));
                    commonParam.put(BdKeyConstant.KEY_RENT_STATE, String.valueOf(RentState
                            .RENT_RESERVE));
                    commonParam.put(BdKeyConstant.KEY_RENT_STATE, String.valueOf(RentState
                            .RENT_RESERVE));
                    commonParam.put(BdKeyConstant.KEY_RESERVE_START_TIME, String.valueOf(System
                            .currentTimeMillis()));
                    UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
                    if (userInfo != null) {
                        commonParam.put(BdKeyConstant.KEY_TENANT_ID, userInfo._id);
                        commonParam.put(BdKeyConstant.KEY_TENANT_PHONE, userInfo.cellphone);
                        commonParam.put(BdKeyConstant.KEY_TENANT_NAME, userInfo.name);
                    }
                    return HttpManager.getInstance().getBaiduLBSApi().updatePoi(commonParam);
                }
                return Observable.error(new ApiException("预定失败"));
            }
        });
        addSubscription(observable, new SubscriberCallBack<>(new ApiCallback<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                showToast(message);
            }

            @Override
            public void onSuccess(BaseBean data) {
                if (data.status == 0) {
                    showToast("预定成功");
                }
            }
        }));
    }

    private void rentLocker() {
        if (mPoisBean == null)
            return;
        PoiBody body = new PoiBody(mPoisBean.getLockerId(), mPoisBean.getOwnerId());
        Observable<BaseBean> observable = HttpManager.getInstance().getUserApi().rentLocker(body)
                .flatMap(new Func1<String, Observable<BaseBean>>() {
                    @Override
                    public Observable<BaseBean> call(String s) {
                        String lockerId = JSONUtils.getString(s, "_id", "");
                        if (!TextUtils.isEmpty(lockerId)) {
                            Map<String, String> commonParam = BaiduConfig.getCommonParam();
                            commonParam.put(BdKeyConstant.KEY_ID, String.valueOf(mPoisBean.getId
                                    ()));
                            commonParam.put(BdKeyConstant.KEY_RENT_STATE, String.valueOf(RentState
                                    .RENT_RESERVE));
                            commonParam.put(BdKeyConstant.KEY_RENT_STATE, String.valueOf(RentState
                                    .RENT_RESERVE));
                            commonParam.put(BdKeyConstant.KEY_RESERVE_START_TIME, String.valueOf
                                    (System
                                            .currentTimeMillis()));
                            UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
                            if (userInfo != null) {
                                commonParam.put(BdKeyConstant.KEY_TENANT_ID, userInfo._id);
                                commonParam.put(BdKeyConstant.KEY_TENANT_PHONE, userInfo.cellphone);
                                commonParam.put(BdKeyConstant.KEY_TENANT_NAME, userInfo.name);
                            }
                            return HttpManager.getInstance().getBaiduLBSApi().updatePoi
                                    (commonParam);
                        }
                        return Observable.error(new ApiException("预定失败"));
                    }
                });
        addSubscription(observable, new SubscriberCallBack<>(new ApiCallback<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                showToast(message);
            }

            @Override
            public void onSuccess(BaseBean data) {
                if (data.status == 0) {
                    showToast("预定成功");
                }
            }
        }));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {
                int payResult = data.getIntExtra(KeyConstant.KEY_DATA, 0);
                if (payResult == 0) {

                }
            }
        }
    }

}

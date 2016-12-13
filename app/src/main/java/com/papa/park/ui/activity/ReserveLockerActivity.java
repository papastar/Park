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

import java.math.BigDecimal;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Func1;

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

    LockerLBSListResponse.PoisBean mPoisBean;


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
                    mPoisBean = data.getPoi();
                    bindInfo(mPoisBean);
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
        if (TextUtils.equals(mReserveBtn.getText(), "确认租用")) {
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
                reserveLocker();
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
                    updateConfirmRentInfo();
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
                    updateConfirmRentInfo();
                }
            }
        }));


    }


    private void updateConfirmRentInfo() {
        mReserveBtn.setText("确认租用");
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

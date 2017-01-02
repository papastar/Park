package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.UserInfo;

import butterknife.Bind;

public class MyWalletActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.price_tv)
    TextView mPriceTv;
    @Bind(R.id.withdraw_tv)
    TextView mWithdrawTv;
    @Bind(R.id.trading_record_tv)
    TextView mTradingRecordTv;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "我的钱包");
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        if (userInfo != null)
            mPriceTv.setText(userInfo.balance);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


}
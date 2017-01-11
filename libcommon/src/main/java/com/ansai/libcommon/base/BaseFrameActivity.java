package com.ansai.libcommon.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;
import com.ansai.libcommon.util.TUtil;
import com.ansai.libcommon.util.ToastUtils;


/**
 * Created by quan on 16/9/1.
 */

public abstract class BaseFrameActivity<P extends BasePresenter, M extends BaseModel> extends
        BaseAppCompatActivity implements BaseView {

    public P mPresenter;

    public M mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.setVM(this, mModel);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onInternetError() {
//        showShortToast("网络异常");
    }

    @Override
    public void onRequestError(String msg) {
        ToastUtils.getInstance().showToast(msg);

    }

    @Override
    public void onRequestStart() {
        if (mVaryViewChange != null) {
            mVaryViewChange.showLoading();
        } else {
            ProgressDialogFragment.showProgress(getSupportFragmentManager());
        }
    }

    @Override
    public void onRequestEnd() {
        if (mVaryViewChange != null) {
            mVaryViewChange.restore();
        } else {
            ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
        }
    }

    protected void setToolbar(Toolbar toolbar,String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            if (getSupportActionBar() != null)
                getSupportActionBar().setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
        }
    }
}

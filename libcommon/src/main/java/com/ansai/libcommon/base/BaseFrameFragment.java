package com.ansai.libcommon.base;

import android.os.Bundle;

import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;
import com.ansai.libcommon.util.TUtil;


/**
 * Created by quan on 16/9/20.
 */

public abstract class BaseFrameFragment<P extends BasePresenter, M extends BaseModel> extends
        BaseFragment implements BaseView {

    public P mPresenter;

    public M mModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.setVM(this, mModel);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onInternetError() {
//        showShortToast("网络异常");
    }

    @Override
    public void onRequestError(String msg) {
//        showShortToast(msg);

    }


    @Override
    public void onRequestStart() {
        if (mVaryViewChange != null) {
            mVaryViewChange.showLoading();
        } else {
            ProgressDialogFragment.showProgress(getFragmentManager());
        }
    }

    @Override
    public void onRequestEnd() {
        if (mVaryViewChange != null) {
            mVaryViewChange.restore();
        } else {
            ProgressDialogFragment.dismissProgress(getFragmentManager());
        }
    }

}

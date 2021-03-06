package com.ansai.libcommon.control;

import android.view.View;

import com.ansai.libcommon.widget.loading.VaryViewHelperController;

/**
 * User: PAPA
 * Date: 2016-09-30
 */

public class VaryViewChangeControl implements OnVaryViewChange {

    private VaryViewHelperController mVaryViewHelperController;

    public VaryViewChangeControl(VaryViewHelperController controller) {
        mVaryViewHelperController = controller;
    }

    @Override
    public void showError() {
        mVaryViewHelperController.showError("error", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showEmpty() {
        mVaryViewHelperController.showEmpty("暂无数据", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showLoading() {
        mVaryViewHelperController.showLoading("Loading...");
    }

    @Override
    public void restore() {
        mVaryViewHelperController.restore();
    }
}

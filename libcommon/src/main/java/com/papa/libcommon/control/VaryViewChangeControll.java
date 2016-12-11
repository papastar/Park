package com.papa.libcommon.control;

import android.view.View;

import com.papa.libcommon.widget.loading.VaryViewHelperController;

/**
 * User: PAPA
 * Date: 2016-09-30
 */

public class VaryViewChangeControll implements OnVaryViewChange {

    private VaryViewHelperController mVaryViewHelperController;

    public VaryViewChangeControll(VaryViewHelperController controller) {
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

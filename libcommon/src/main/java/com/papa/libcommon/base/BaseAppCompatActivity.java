package com.papa.libcommon.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.papa.libcommon.control.OnVaryViewChange;
import com.papa.libcommon.control.VaryViewChangeControll;
import com.papa.libcommon.util.BaseAppManager;
import com.papa.libcommon.util.netstatus.NetChangeObserver;
import com.papa.libcommon.util.netstatus.NetStateReceiver;
import com.papa.libcommon.util.netstatus.NetUtils;
import com.papa.libcommon.widget.loading.VaryViewHelperController;

import butterknife.ButterKnife;


/**
 * Created by fangxiao on 15/12/18.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements OnVaryViewChange {


    /**
     * 联网状态
     */
    protected NetChangeObserver mNetChangeObserver = null;


    protected OnVaryViewChange mVaryViewChange;


    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // base setup
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        BaseAppManager.getInstance().addActivity(this);

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        }

        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        NetStateReceiver.registerObserver(mNetChangeObserver);
        initViewsAndEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
            mVaryViewChange = new VaryViewChangeControll(mVaryViewHelperController);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseAppManager.getInstance().removeActivity(this);
        ButterKnife.unbind(this);
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);

    }

    /**
     * get bundle data
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();


    /**
     * get loading target view
     */
    protected abstract View getLoadingTargetView();

    /**
     * network connected
     */
    protected void onNetworkConnected(NetUtils.NetType type) {

    }



    /**
     * network disconnected
     */
    protected void onNetworkDisConnected() {

    }



    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * show toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        //防止遮盖虚拟按键
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void showError() {
        mVaryViewChange.showError();
    }

    @Override
    public void showEmpty() {
        mVaryViewChange.showEmpty();
    }

    @Override
    public void showLoading() {
        mVaryViewChange.showLoading();
    }

    @Override
    public void restore() {
        mVaryViewChange.restore();
    }


}
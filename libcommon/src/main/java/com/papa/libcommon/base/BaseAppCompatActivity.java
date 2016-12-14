package com.papa.libcommon.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.papa.libcommon.control.OnVaryViewChange;
import com.papa.libcommon.control.VaryViewChangeControl;
import com.papa.libcommon.rx.RxManager;
import com.papa.libcommon.util.BaseAppManager;
import com.papa.libcommon.util.netstatus.NetChangeObserver;
import com.papa.libcommon.util.netstatus.NetStateReceiver;
import com.papa.libcommon.util.netstatus.NetUtils;
import com.papa.libcommon.widget.loading.VaryViewHelperController;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by fangxiao on 15/12/18.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements OnVaryViewChange {


    /**
     * 联网状态
     */
    protected NetChangeObserver mNetChangeObserver = null;


    protected OnVaryViewChange mVaryViewChange;
    protected RxManager mRxManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();
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
            VaryViewHelperController varyViewHelperController = new VaryViewHelperController
                    (getLoadingTargetView());
            mVaryViewChange = new VaryViewChangeControl(varyViewHelperController);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxManager != null) {
            mRxManager.unSubscribe();
        }
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
     * init all views and addSubscription events
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
        if (mVaryViewChange == null)
            return;
        mVaryViewChange.showError();
    }

    @Override
    public void showEmpty() {
        if (mVaryViewChange == null)
            return;
        mVaryViewChange.showEmpty();
    }

    @Override
    public void showLoading() {
        if (mVaryViewChange == null)
            return;
        mVaryViewChange.showLoading();
    }

    @Override
    public void restore() {
        if (mVaryViewChange == null)
            return;
        mVaryViewChange.restore();
    }

    protected void setToolbar(Toolbar toolbar, String title) {
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


    protected <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber) {
        mRxManager.addSubscription(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }

}

package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.libcommon.base.ProgressDialogFragment;
import com.papa.park.R;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.ApiException;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.entity.bean.BaseBean;
import com.papa.park.entity.bean.LockerLBSBean;
import com.papa.park.utils.StringUtil;

import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Func1;

public class LockersActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.list_view)
    ListView mListView;

    QuickAdapter<LockerLBSBean.ContentsBean> mAdapter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_lockers;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "我的车锁");
        mAdapter = new QuickAdapter<LockerLBSBean.ContentsBean>(this, R.layout.lockers_list_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, final LockerLBSBean.ContentsBean
                    item) {
                helper.setText(R.id.name_tv, item.getTitle() + item.getAddress());
                TextView statusTv = helper.getView(R.id.status_tv);
                int state = StringUtil.parseInt(item.getState());
                //可出租
                if (state == 0 || state == 1) {
                    statusTv.setText("出租");
                    statusTv.setBackgroundResource(R.drawable.bg_board);
                    statusTv.setTextColor(ContextCompat.getColor(context, R.color.color_ee6c2d));
                    statusTv.setEnabled(true);
                    statusTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateState(item, 2);
                        }
                    });
                } else if (state == 2) {
                    statusTv.setText("已出租");
                    statusTv.setBackgroundResource(R.color.color_dddddd);
                    statusTv.setTextColor(ContextCompat.getColor(context, R.color.white));
                    statusTv.setEnabled(false);
                } else if (state == 3) {
                    statusTv.setText("已预定");
                    statusTv.setBackgroundResource(R.color.color_dddddd);
                    statusTv.setTextColor(ContextCompat.getColor(context, R.color.white));
                    statusTv.setEnabled(false);
                }
            }
        };
        mListView.setAdapter(mAdapter);
        loadData();
    }

    private void loadData() {
        showLoading();
        Observable<LockerLBSBean> locker = HttpManager.getInstance().getBaiduLBSApi().getLocker
                (BaiduConfig.getCommonParam());
        addSubscription(locker, new SubscriberCallBack<>(new ApiCallback<LockerLBSBean>() {
            @Override
            public void onCompleted() {
                restore();
            }

            @Override
            public void onFailure(int code, String message) {
                showError();
            }

            @Override
            public void onSuccess(LockerLBSBean data) {
                if (data != null) {
                    mAdapter.replaceAll(data.getContents());
                }
            }
        }));
    }

    private void updateState(LockerLBSBean.ContentsBean bean, int state) {
        ProgressDialogFragment.showProgress(getSupportFragmentManager());
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put("id", String.valueOf(bean.getUid()));
        commonParam.put("state", String.valueOf(state));
        Observable<LockerLBSBean> observable = HttpManager.getInstance().getBaiduLBSApi().updatePoi
                (commonParam).flatMap(new Func1<BaseBean, Observable<LockerLBSBean>>() {
            @Override
            public Observable<LockerLBSBean> call(BaseBean baseBean) {
                if (baseBean.status == 0) {
                    return HttpManager.getInstance().getBaiduLBSApi().getLocker
                            (BaiduConfig.getCommonParam());
                }
                return Observable.error(new ApiException(baseBean.message));
            }
        });
        addSubscription(observable, new SubscriberCallBack<>(new ApiCallback<LockerLBSBean>() {
            @Override
            public void onCompleted() {
                ProgressDialogFragment.dismissProgress(getSupportFragmentManager());
            }

            @Override
            public void onFailure(int code, String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(LockerLBSBean data) {
                if (data != null) {
                    mAdapter.replaceAll(data.getContents());
                }
            }
        }));
    }


    @Override
    protected View getLoadingTargetView() {
        return mListView;
    }


}

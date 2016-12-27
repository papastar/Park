package com.papa.park.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.papa.libcommon.base.BaseFragment;
import com.papa.park.R;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.LockerLBSListResponse;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.entity.event.RefreshEvent;
import com.papa.park.ui.activity.RentConfirmActivity;
import com.papa.park.ui.activity.RentDetailActivity;
import com.papa.park.ui.view.LinearDividerItemDecoration;
import com.papa.park.utils.KeyConstant;

import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Action1;

/**
 * User: PAPA
 * Date: 2016-12-08
 */

public class LockerFragment extends BaseFragment {


    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;


    BaseQuickAdapter<LockerLBSListResponse.PoisBean> mAdapter;


    /**
     * @param type 0我的车锁,1已发布的车锁，2.已租用的车锁
     * @return
     */
    public static LockerFragment newInstance(int type) {
        LockerFragment fragment = new LockerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KeyConstant.KEY_DATA, type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initViewsAndEvents() {
        onRefreshEvent();
        mAdapter = new BaseQuickAdapter<LockerLBSListResponse.PoisBean>(R.layout.lockers_list_item,
                null) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, final LockerLBSListResponse
                    .PoisBean
                    contentsBean) {
                baseViewHolder.setText(R.id.locker_name_tv, contentsBean.getTitle());
                baseViewHolder.addOnClickListener(R.id.operate_tv);
                TextView stateTv = baseViewHolder.getView(R.id.state_tv);
                TextView infoTv = baseViewHolder.getView(R.id.info_tv);
                TextView operate_tv = baseViewHolder.getView(R.id.operate_tv);
                switch (contentsBean.getRentState()) {
                    case 0:
                    case 1:
                        stateTv.setText("可出租");
                        infoTv.setText(getTitle(contentsBean));
                        stateTv.setBackgroundColor(ContextCompat.getColor(mContext, R.color
                                .colorPrimaryDark));
                        operate_tv.setText("出租");
                        operate_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(KeyConstant.KEY_DATA, contentsBean.getId());
                                readyGo(RentConfirmActivity.class, bundle);
                            }
                        });
                        break;
                    case 2:
                        stateTv.setText("已发布");
                        infoTv.setText(getString(R.string.rent_price_of, contentsBean
                                .getRentFirstHourPrice(), contentsBean.getRentPerHourPrice()));
                        stateTv.setBackgroundColor(Color.parseColor("#28a3f9"));
                        operate_tv.setText("查看详情");
                        operate_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(KeyConstant.KEY_TYPE, 0);
                                bundle.putInt(KeyConstant.KEY_DATA, contentsBean.getId());
                                readyGo(RentDetailActivity.class, bundle);
                                //readyGoForResult(RentDetailActivity.class, 1, bundle);
                            }
                        });
                        break;
                    case 3:
                        stateTv.setText("已预定");
                        infoTv.setText(getString(R.string.rent_price_of, contentsBean
                                .getRentFirstHourPrice(), contentsBean.getRentPerHourPrice()));
                        stateTv.setBackgroundColor(Color.parseColor("#52b607"));
                        operate_tv.setText("查看详情");
                        operate_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(KeyConstant.KEY_TYPE, 0);
                                bundle.putInt(KeyConstant.KEY_DATA, contentsBean.getId());
                                readyGo(RentDetailActivity.class, bundle);
                            }
                        });
                        break;
                    case 4:
                        stateTv.setText("已被租");
                        infoTv.setText(getString(R.string.rent_price_of, contentsBean
                                .getRentFirstHourPrice(), contentsBean.getRentPerHourPrice()));
                        stateTv.setBackgroundColor(Color.parseColor("#52b607"));
                        operate_tv.setText("查看详情");
                        operate_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(KeyConstant.KEY_TYPE, 0);
                                bundle.putInt(KeyConstant.KEY_DATA, contentsBean.getId());
                                readyGo(RentDetailActivity.class, bundle);
                            }
                        });
                        break;
                }
            }

            private String getTitle(LockerLBSListResponse
                                            .PoisBean
                                            bean) {
                return bean.getProvince() + bean.getCity() +
                        bean.getDistrict
                                () +
                        bean.getAddress();

            }

        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        LinearDividerItemDecoration decoration = new LinearDividerItemDecoration(getContext(),
                LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL);
        decoration.setDivider(ContextCompat.getDrawable(mContext, R.drawable.bg_list_divide));
        mRecyclerView.addItemDecoration(decoration);

        loadData();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_locker;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return mRecyclerView;
    }


    private void loadData() {
        showLoading();
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        if (userInfo != null)
            commonParam.put("ownerPhone", userInfo.cellphone);
        Observable<LockerLBSListResponse> locker = HttpManager.getInstance().getBaiduLBSApi()
                .getLocker
                        (commonParam);
        addSubscription(locker, new SubscriberCallBack<>(new ApiCallback<LockerLBSListResponse>() {
            @Override
            public void onCompleted() {
                restore();
            }

            @Override
            public void onFailure(int code, String message, Exception e) {
                showError();
            }

            @Override
            public void onSuccess(LockerLBSListResponse data) {
                if (data != null && data.getPois() != null) {
                    mAdapter.setNewData(data.getPois());
                }
                if (mAdapter.getData().isEmpty()) {
                    showEmpty();
                }
            }
        }));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadData();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    private void onRefreshEvent() {
        rxManager.onEvent(RefreshEvent.class, new Action1<RefreshEvent>() {
            @Override
            public void call(RefreshEvent rechargeEvent) {
                loadData();
            }
        });
    }
}

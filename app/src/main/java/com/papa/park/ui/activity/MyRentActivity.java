package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.LockerLBSListResponse;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.ui.view.LinearDividerItemDecoration;
import com.papa.park.utils.BdKeyConstant;
import com.papa.park.utils.KeyConstant;
import com.papa.park.utils.StringUtil;
import com.papa.park.utils.TimeUtils;

import java.util.Map;

import butterknife.Bind;
import rx.Observable;

import static com.baidu.mapapi.BMapManager.getContext;

public class MyRentActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;


    BaseQuickAdapter<LockerLBSListResponse.PoisBean> mAdapter;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_rent;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "我的租用");
        mAdapter = new BaseQuickAdapter<LockerLBSListResponse.PoisBean>(R.layout.my_rent_list_item,
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
                    case 3:
                        stateTv.setText("已预定");
                        infoTv.setText("预定时间:" + TimeUtils.millis2String(StringUtil.parseLong
                                (contentsBean.getReserveStartTime())));
                        stateTv.setBackgroundColor(ContextCompat.getColor(mContext, R.color
                                .colorPrimaryDark));
                        operate_tv.setText("查看详情");
                        operate_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(KeyConstant.KEY_DATA, contentsBean.getId());
                                readyGo(RentDetailActivity.class, bundle);
                            }
                        });
                        break;
                    case 4:
                        stateTv.setText("已租");
                        infoTv.setText("起租时间:" + TimeUtils.millis2String(StringUtil.parseLong
                                (contentsBean.getRentStartTime())));
                        operate_tv.setText("查看详情");
                        operate_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(KeyConstant.KEY_DATA, contentsBean.getId());
                                readyGo(RentDetailActivity.class, bundle);
                            }
                        });
                        break;
                }
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                .VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        LinearDividerItemDecoration decoration = new LinearDividerItemDecoration(getContext(),
                LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL);
        decoration.setDivider(ContextCompat.getDrawable(this, R.drawable.bg_list_divide));
        mRecyclerView.addItemDecoration(decoration);

        loadData();
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
            commonParam.put(BdKeyConstant.KEY_TENANT_PHONE, userInfo.cellphone);
        Observable<LockerLBSListResponse> locker = HttpManager.getInstance().getBaiduLBSApi()
                .getLocker
                        (commonParam);
        addSubscription(locker, new SubscriberCallBack<>(new ApiCallback<LockerLBSListResponse>() {
            @Override
            public void onCompleted() {

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

}

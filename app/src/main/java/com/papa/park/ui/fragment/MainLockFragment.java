package com.papa.park.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseFrameFragment;
import com.papa.park.R;
import com.papa.park.mvp.MainFragmentContract;
import com.papa.park.mvp.model.MainFragmentModel;
import com.papa.park.mvp.presenter.MainFragmentPresenter;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLockFragment extends BaseFrameFragment<MainFragmentPresenter, MainFragmentModel>
        implements MainFragmentContract.View {

    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.status_tv)
    TextView statusTv;
    @Bind(R.id.battery_tv)
    TextView batteryTv;
    @Bind(R.id.lock_img)
    ImageView lockImg;
    @Bind(R.id.lock_tv)
    TextView lockTv;
    @Bind(R.id.address_tv)
    TextView addressTv;
    @Bind(R.id.layout)
    RelativeLayout layout;
    @Bind(R.id.bottom_view)
    ImageView bottomView;
    @Bind(R.id.lock_bell_btn)
    Button lockBellBtn;
    @Bind(R.id.lock_list_btn)
    Button lockListBtn;
    @Bind(R.id.operation_container)
    LinearLayout operationContainer;

    public static MainLockFragment newInstance() {
        MainLockFragment fragment = new MainLockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void initViewsAndEvents() {
        setUnbindInfo();
    }


    private void setUnbindInfo() {
        nameTv.setText("车锁名称");
        statusTv.setText("未连接");
        batteryTv.setText("");
        lockTv.setText("还未绑定车锁");
        addressTv.setText("停车场地址");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main_lock;
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
        return null;
    }


}

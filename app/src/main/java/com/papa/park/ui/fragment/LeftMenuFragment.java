package com.papa.park.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseFragment;
import com.papa.park.R;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.ui.activity.UserInfoActivity;
import com.papa.park.utils.CircleImageTransformation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * User: PAPA
 * Date: 2016-11-21
 */

public class LeftMenuFragment extends BaseFragment {
    @Bind(R.id.head_img)
    ImageView mHeadImg;
    @Bind(R.id.name_tv)
    TextView mNameTv;
    @Bind(R.id.phone_tv)
    TextView mPhoneTv;
    @Bind(R.id.order_tv)
    TextView mOrderTv;
    @Bind(R.id.wallet_tv)
    TextView mWalletTv;
    @Bind(R.id.my_lock_tv)
    TextView mMyLockTv;
    @Bind(R.id.lock_manager_tv)
    TextView mLockManagerTv;
    @Bind(R.id.release_park_tv)
    TextView mReleaseParkTv;
    @Bind(R.id.find_park_tv)
    TextView mFindParkTv;
    @Bind(R.id.message_center_tv)
    TextView mMessageCenterTv;
    @Bind(R.id.share_park_tv)
    TextView mShareParkTv;
    @Bind(R.id.navigation_view)
    LinearLayout mNavigationView;

    UserInfo mUserInfo;

    @Override
    protected void initViewsAndEvents() {
        mUserInfo = UserInfoManager.getInstance().getUserInfo();
        if (!TextUtils.isEmpty(mUserInfo.avatar))
            Picasso.with(getContext()).load(mUserInfo.avatar).placeholder(R.drawable.img_head)
                    .error(R.drawable
                            .img_head)
                    .transform(new CircleImageTransformation()).into(mHeadImg);
        else
            mHeadImg.setImageResource(R.drawable
                    .img_head);
        mNameTv.setText(mUserInfo.name);
        mPhoneTv.setText(mUserInfo.cellphone);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_left_menu;
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

    @OnClick(R.id.user_info_layout)
    void onUserClick() {
        readyGo(UserInfoActivity.class);
    }

}
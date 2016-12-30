package com.papa.park.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.papa.libcommon.base.BaseFragment;
import com.papa.park.R;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.ui.activity.LockerMapActivity;
import com.papa.park.ui.activity.LockersActivity;
import com.papa.park.ui.activity.MainActivity;
import com.papa.park.ui.activity.MyRentActivity;
import com.papa.park.ui.activity.MyWalletActivity;
import com.papa.park.ui.activity.SearchLockActivity;
import com.papa.park.ui.activity.UserInfoActivity;
import com.papa.park.utils.CircleImageTransformation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.OnClick;

import static com.papa.park.R.id.my_locker_tv;
import static com.papa.park.R.id.my_rent_tv;

/**
 * User: PAPA
 * Date: 2016-11-21
 */

public class LeftMenuFragment extends BaseFragment {
    @Bind(R.id.head_img)
    ImageView mHeadImg;
    @Bind(R.id.locker_name_tv)
    TextView mNameTv;
    @Bind(R.id.locker_phone_tv)
    TextView mPhoneTv;
    @Bind(R.id.order_tv)
    TextView mOrderTv;
    @Bind(R.id.wallet_tv)
    TextView mWalletTv;
    @Bind(my_locker_tv)
    TextView mRentLockerTv;
    @Bind(R.id.lock_manager_tv)
    TextView mLockManagerTv;
    @Bind(R.id.add_locker_tv)
    TextView mAddLockerTv;
    @Bind(my_rent_tv)
    TextView mMyRentTv;
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

    @OnClick(R.id.lock_manager_tv)
    void onManagerLocker() {
        readyGo(LockersActivity.class);
    }

    @OnClick(R.id.my_rent_tv)
    void onFindLockerTv() {
        readyGo(LockerMapActivity.class);
    }

    @OnClick(R.id.wallet_tv)
    void onMyWalletTv() {
        readyGo(MyWalletActivity.class);
    }

    @OnClick(R.id.my_locker_tv)
    void onMyRentTv() {
        readyGo(MyRentActivity.class);
    }

    @OnClick(R.id.add_locker_tv)
    void onAddLockTv() {
        Intent intent = new Intent(getActivity(), SearchLockActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.REQUEST_CODE_SEARCH_LOCK);
    }

}

package com.papa.park.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.api.HttpManager;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.CodeBean;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.mvp.LoginContract;
import com.papa.park.mvp.model.LoginModel;
import com.papa.park.mvp.presenter.LoginPresenter;
import com.papa.park.utils.KeyboardUtils;
import com.papa.park.utils.RegexUtils;

import butterknife.Bind;
import butterknife.OnClick;

import static com.papa.park.R.id.login_btn;

public class LoginActivity extends BaseFrameActivity<LoginPresenter, LoginModel> implements
        LoginContract.View {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.phone_edit)
    EditText mPhoneEdit;
    @Bind(R.id.code_btn)
    Button mCodeBtn;
    @Bind(R.id.code_edit)
    EditText mCodeEdit;
    @Bind(R.id.agreement_tv)
    TextView mAgressmentTv;
    @Bind(login_btn)
    Button mLoginBtn;

    CountDownTimer mCountDownTimer;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }


    @Override
    protected void initViewsAndEvents() {
        setToolBar(mToolBar);
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCodeBtn.setEnabled(RegexUtils.isMobileSimple(editable.toString()));
            }
        });
        mCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String code = editable.toString();
                boolean isMobile = RegexUtils.isMobileSimple(mPhoneEdit.getEditableText()
                        .toString());
                if (isMobile && !TextUtils.isEmpty(code) && TextUtils.isDigitsOnly(code) && code
                        .length() == 6) {
                    mLoginBtn.setEnabled(true);
                    KeyboardUtils.hideSoftInput(LoginActivity.this);
                } else {
                    mLoginBtn.setEnabled(false);
                }
            }
        });
    }

    private void setToolBar(Toolbar toolBar) {
        toolBar.setTitle(R.string.login_in);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.code_btn)
    void onCodeBtnClick() {
        mPresenter.getCode(mPhoneEdit.getEditableText().toString());
    }

    @OnClick(login_btn)
    void onLoginBtnClick() {
        mPresenter.login(mPhoneEdit.getEditableText().toString(), mCodeEdit.getEditableText()
                .toString());
    }

    private void countDown() {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                updateText(l);
            }

            @Override
            public void onFinish() {
                updateText(0);
            }
        };
        mCountDownTimer.start();
    }

    private void updateText(long time) {
        if (time > 0) {
            long count = time / 1000;
            mCodeBtn.setText(getString(R.string.second_of, String.valueOf(count)));
            mCodeBtn.setEnabled(false);
        } else {
            mCodeBtn.setEnabled(true);
            mCodeBtn.setText(R.string.get_code);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void onGetCodeSuccess(CodeBean response) {
        String raw = response.raw;
        //{"raw":"ok"}
        if (TextUtils.equals(raw, "ok"))
            countDown();
    }


    @Override
    public void onLoginSuccess(UserInfo userInfoEntity) {
        if (userInfoEntity != null && !TextUtils.isEmpty(userInfoEntity._id)) {
            UserInfoManager.getInstance().saveUser(userInfoEntity);
            HttpManager.setToken(userInfoEntity.token);
            readyGo(MainActivity.class);
        }
    }


}

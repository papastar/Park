package com.papa.park.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.papa.libcommon.util.AppUtils;
import com.papa.park.entity.bean.UserInfo;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public class UserInfoManager {

    private static final String PRE_USER = "pre_user";

    private static UserInfoManager sInfoManager;
    private UserInfo mUserInfo;
    private SharedPreferences mPreferences;


    private UserInfoManager() {
        mPreferences = AppUtils.getAppContext().getSharedPreferences(PRE_USER,
                Context.MODE_PRIVATE);
        String data = mPreferences.getString("data", "");
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            mUserInfo = gson.fromJson(data, UserInfo.class);
        } else {
            mUserInfo = new UserInfo();
        }
    }


    public static UserInfoManager getInstance() {
        if (sInfoManager == null) {
            synchronized (UserInfoManager.class) {
                if (sInfoManager == null) {
                    sInfoManager = new UserInfoManager();
                }
            }
        }
        return sInfoManager;
    }


    public void saveUser(UserInfo userInfo) {
        if (userInfo != null) {
            mUserInfo = userInfo;
            Gson gson = new Gson();
            mPreferences.edit().putString("data", gson.toJson(userInfo)).apply();
        }
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public String getToken() {
        if (mUserInfo != null)
            return mUserInfo.token;
        return null;
    }

    public String getCellPhone() {
        if (mUserInfo != null)
            return mUserInfo.cellphone;
        return "";
    }

    public boolean isValid() {
        return mUserInfo != null && !TextUtils.isEmpty(mUserInfo._id);
    }

    public void clearUser() {
        mUserInfo = new UserInfo();
        mPreferences.edit().clear().apply();
    }


}

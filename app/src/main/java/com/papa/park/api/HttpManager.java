/*
 * Copyright 2016 Freelander
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.papa.park.api;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.papa.libcommon.util.AppUtils;
import com.papa.park.BuildConfig;
import com.papa.park.data.UserInfoManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jun on 2016/7/27.
 */
public class HttpManager {

    private static final int DEFAULT_TIMEOUT = 15000;
    private static final String TAG = "HttpManager";
    private static final String BASE_URL = "http://120.24.4.26:7788/api/";
    private static HttpManager sManager;
    private OkHttpClient.Builder mOkHttpBuilder;
    private Retrofit.Builder mBuilder;
    private UserApi mUserApi;
    private LockerApi mLockerApi;

    public static HttpManager getInstance() {
        if (sManager == null) {
            sManager = new HttpManager(AppUtils.getAppContext());
        }
        return sManager;
    }

    private HttpManager(Context context) {
        mOkHttpBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEV_MODE) {
            Stetho.initializeWithDefaults(context);
            mOkHttpBuilder.addNetworkInterceptor(new StethoInterceptor());
            mOkHttpBuilder.addInterceptor(new LoggerInterceptor(TAG, true));
        }
        mOkHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }


    public UserApi getUserApi() {
        return mUserApi == null ? configRetrofit(UserApi.class, false) : mUserApi;
    }

    public LockerApi getLockerApi() {
        return mLockerApi == null ? configRetrofit(LockerApi.class, true) : mLockerApi;
    }

    private <T> T configRetrofit(Class<T> service, boolean withToken) {

        if (mBuilder == null) {
            mBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }

        if (withToken) {
            Map<String, String> head = new HashMap<>();
            head.put("Authorization", UserInfoManager.getInstance().getToken());
            mOkHttpBuilder.addInterceptor(new HttpHeadInterceptor(head));
        }
        mBuilder.client(mOkHttpBuilder.build());
        return mBuilder.build().create(service);
    }
}

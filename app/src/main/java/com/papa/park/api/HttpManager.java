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
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.papa.park.BuildConfig;

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
    private static OkHttpClient mOkHttpClient;
    private static UserApi mUserApi;
    private static HttpManager sManager;
    private static Retrofit.Builder mBuilder;

    public static HttpManager getInstance() {
        if (sManager == null) {
            sManager = new HttpManager();
        }
        return sManager;
    }

    public static void init(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEV_MODE) {
            Stetho.initializeWithDefaults(context);
            builder.addNetworkInterceptor(new StethoInterceptor());
            builder.addInterceptor(new LoggerInterceptor(TAG, true));
        }
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);

        mOkHttpClient = builder.build();
    }

    public static void setToken(String token) {
        if (mOkHttpClient != null && !TextUtils.isDigitsOnly(token)) {
            Map<String, String> map = new HashMap<>();
            map.put("Authorization", token);
            mBuilder.client(mOkHttpClient.newBuilder().addInterceptor(new HttpHeadInterceptor
                    (map)).build());
        }
    }

    public UserApi getUserApi() {
        return mUserApi == null ? configRetrofit(UserApi.class) : mUserApi;
    }

    private <T> T configRetrofit(Class<T> service) {
        if (mBuilder == null)
            mBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return mBuilder.build().create(service);
    }
}

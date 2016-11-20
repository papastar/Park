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
import com.papa.libcommon.util.AppUtils;
import com.papa.park.data.UserInfoManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Jun on 2016/7/27.
 */
public class HttpManager {

    private static final int DEFAULT_TIMEOUT = 15000;
    private static final String TAG = "HttpManager";
    private static final String BASE_URL = "http://120.24.4.26:7788/api/";
    private static HttpManager sManager;
    private OkHttpClient.Builder mOkHttpBuilder;
    private UserApi mUserApi;
    private LockerApi mLockerApi;

    private HttpManager(Context context) {
        mOkHttpBuilder = new OkHttpClient.Builder();
        //if (BuildConfig.DEV_MODE) {
            Stetho.initializeWithDefaults(context);
            mOkHttpBuilder.addNetworkInterceptor(new StethoInterceptor());
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpBuilder.addInterceptor(new HttpLoggingInterceptor());
       // }
        mOkHttpBuilder.addNetworkInterceptor(getAuthHeadInterceptor());
        mOkHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public static HttpManager getInstance() {
        if (sManager == null) {
            sManager = new HttpManager(AppUtils.getAppContext());
        }
        return sManager;
    }

    public UserApi getUserApi() {
        return mUserApi == null ? configRetrofit(UserApi.class) : mUserApi;
    }

    public LockerApi getLockerApi() {
        return mLockerApi == null ? configRetrofit(LockerApi.class) : mLockerApi;
    }


    private Interceptor getAuthHeadInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String token = UserInfoManager.getInstance().getToken();
                if (TextUtils.isEmpty(token))
                    return chain.proceed(originalRequest);
                Request.Builder builder = originalRequest.newBuilder();
                builder.addHeader("Authorization", token);
                return chain.proceed(builder.build());
            }
        };
    }


    private <T> T configRetrofit(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpBuilder.build())
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }
}

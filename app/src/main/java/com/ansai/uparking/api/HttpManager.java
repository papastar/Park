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
package com.ansai.uparking.api;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ansai.libcommon.util.AppUtils;
import com.ansai.libcommon.util.netstatus.NetUtils;
import com.ansai.uparking.data.UserInfoManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class HttpManager {

    private static final int DEFAULT_TIMEOUT = 15000;
    private static final String BASE_URL = "http://120.24.4.26:7788/api/";
    private static final String LBS_URL = "http://api.map.baidu.com/";
    public static final String PAY_URL = "http://api.ansaikeji.com:7001";
    private static HttpManager sManager;
    private OkHttpClient.Builder mOkHttpBuilder;
    private UserApi mUserApi;
    private LockerApi mLockerApi;
    private BaiduLBSApi mBaiduLBSApi;

    private HttpManager(Context context) {
        mOkHttpBuilder = new OkHttpClient.Builder();
        buildStethoInterceptor(context, mOkHttpBuilder);
        buildHttpLogInterceptor(mOkHttpBuilder);
        buildAuthHeadInterceptor(mOkHttpBuilder);

        //设置超时
        mOkHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mOkHttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //错误重连
        //mOkHttpBuilder.retryOnConnectionFailure(true);
    }

    public static HttpManager getInstance() {
        if (sManager == null) {
            sManager = new HttpManager(AppUtils.getAppContext());
        }
        return sManager;
    }

    public UserApi getUserApi() {
        return mUserApi == null ? configRetrofit(UserApi.class, BASE_URL) : mUserApi;
    }

    public LockerApi getLockerApi() {
        return mLockerApi == null ? configRetrofit(LockerApi.class, BASE_URL) : mLockerApi;
    }

    public BaiduLBSApi getBaiduLBSApi() {
        return mBaiduLBSApi == null ? configRetrofit(BaiduLBSApi.class, LBS_URL) : mBaiduLBSApi;
    }


    public <T> T getApi(Class<T> service, String url) {
        return configRetrofit(service, url);
    }

    private void buildStethoInterceptor(Context context, OkHttpClient.Builder builder) {
        Stetho.initializeWithDefaults(context);
        builder.addNetworkInterceptor(new StethoInterceptor());
    }

    private void buildHttpLogInterceptor(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
    }


    private void buildAuthHeadInterceptor(OkHttpClient.Builder builder) {
        Interceptor interceptor = new Interceptor() {
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
        builder.addInterceptor(interceptor);
    }

    private void buildCacheInterceptor(final Context context, OkHttpClient.Builder builder) {
        File cacheFile = new File(context.getExternalCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtils.isNetworkAvailable(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (!NetUtils.isNetworkAvailable(context)) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" +
                                    maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
    }


    private <T> T configRetrofit(Class<T> service, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(mOkHttpBuilder.build())
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }
}

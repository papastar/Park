package com.ansai.uparking.api;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Papa on 2016/6/1.
 */
public class HttpHeadInterceptor implements Interceptor {
    private Map<String, String> mHeadMaps;

    public HttpHeadInterceptor(Map<String, String> headMaps) {
        mHeadMaps = headMaps;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (mHeadMaps == null || mHeadMaps.isEmpty()) {
            return chain.proceed(originalRequest);
        }

        Request.Builder builder = originalRequest.newBuilder();
        for (String key : mHeadMaps.keySet())
            builder.addHeader(key, mHeadMaps.get(key));
//        .header("Content-Encoding", "gzip")
//        .method(originalRequest.method(), gzip(originalRequest.body()))

        return chain.proceed(builder.build());

    }
}

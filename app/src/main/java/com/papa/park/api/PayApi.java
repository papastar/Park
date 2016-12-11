package com.papa.park.api;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/9.
 */

public interface PayApi {

    @GET("/v1/pay/start")
    Observable<String> getPayInfo(@QueryMap Map<String, String> map);
}

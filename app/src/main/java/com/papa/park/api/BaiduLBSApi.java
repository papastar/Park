package com.papa.park.api;

import com.papa.park.entity.bean.BaseBean;
import com.papa.park.entity.bean.LockerLBSBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-12-01
 */

public interface BaiduLBSApi {


    @GET("geosearch/v3/local")
    Observable<LockerLBSBean> getLocker(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("geodata/v3/poi/update")
    Observable<BaseBean> updatePoi(@FieldMap Map<String,String> map);

}

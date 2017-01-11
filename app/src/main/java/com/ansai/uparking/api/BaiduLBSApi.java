package com.ansai.uparking.api;

import com.ansai.uparking.entity.bean.BaseBean;
import com.ansai.uparking.entity.bean.LockerLBSListResponse;
import com.ansai.uparking.entity.bean.LockerLBSResponse;

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


    @GET("geodata/v3/poi/list")
    Observable<LockerLBSListResponse> getLocker(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("geodata/v3/poi/update")
    Observable<BaseBean> updatePoi(@FieldMap Map<String,String> map);

    @GET("/geodata/v3/poi/detail")
    Observable<LockerLBSResponse> getDetail(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("geodata/v3/poi/create")
    Observable<BaseBean> creatPoi(@FieldMap Map<String,String> map);

}

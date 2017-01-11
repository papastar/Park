package com.ansai.uparking.api;

import com.ansai.uparking.entity.bean.LockerBean;
import com.ansai.uparking.entity.body.LockBody;
import com.ansai.uparking.entity.body.SaveBody;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/15.
 */

public interface LockerApi {

    @POST("lockers/activate")
    Observable<String> checkLocker(@Body LockBody body);

    @GET("lockers")
    Observable<List<LockerBean>> getLocker();

    @POST("lockers/save")
    Observable<String> saveLock(@Body SaveBody body);

    @POST("lockers/sn/{username}")
    Observable<String> updateLock(@Body SaveBody body);
}

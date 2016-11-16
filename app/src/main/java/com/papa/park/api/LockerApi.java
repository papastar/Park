package com.papa.park.api;

import com.papa.park.entity.bean.LockerBean;
import com.papa.park.entity.body.LockBody;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/15.
 */

public interface LockerApi {

    @POST("lockers/activate")
    Observable<String> checkLocker(@Body LockBody body);

    @POST("lockers")
    Observable<List<LockerBean>> getLocker();
}

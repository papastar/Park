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

import com.papa.park.entity.bean.CodeBean;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.entity.body.LoginBody;
import com.papa.park.entity.body.PoiBody;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface UserApi {

    @GET("sms/code")
    Observable<CodeBean> getCode(@Query("cellphone") String cellphone);

    @POST("signin/cellphone")
    Observable<UserInfo> login(@Body LoginBody body);

    @GET("user/cellphone/{cellphone}")
    Observable<UserInfo> getUserInfo(@Path("cellphone") String cellphone);


    @POST("rentsinfo/reservation")
    Observable<String> reserveLocker(@Body PoiBody body);

    @POST("rentsinfo/begin")
    Observable<String> rentLocker(@Body PoiBody body);

    @POST("rentsinfo/end")
    Observable<String> endRentLocker(@Body PoiBody body);
}

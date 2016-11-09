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

import com.papa.park.entity.TokenEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Jun on 2016/4/16.
 */
public interface UserApi {

    @FormUrlEncoded
    @POST("oauth/access_token")
    Observable<TokenEntity> getToken(@Field("grant_type") String authType,
                                     @Field("client_id") String clientId,
                                     @Field("client_secret") String clientSecret,
                                     @Field("username") String username,
                                     @Field("login_token") String loginToken);

}

package com.ansai.uparking.api;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public interface ApiCallback<T> {

      void onCompleted();

      void onFailure(int code,String message,Exception e);

      void onSuccess(T data);
}

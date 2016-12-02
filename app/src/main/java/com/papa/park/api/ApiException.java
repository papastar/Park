package com.papa.park.api;

import android.text.TextUtils;

/**
 * User: PAPA
 * Date: 2016-12-02
 */

public class ApiException extends RuntimeException {

    private String message;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(message))
            return message;
        return super.getMessage();
    }
}

package com.ansai.uparking.entity.event;

/**
 * Created by Administrator on 2016/12/12.
 */

public class RechargeEvent {

    private int code;

    public RechargeEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return code == 0;
    }
}

package com.papa.park.entity.bean;

/**
 * User: PAPA
 * Date: 2016-12-08
 */

public class LockerLBSResponse {

    private int status;
    private String message;
    private LockerLBSListResponse.PoisBean poi;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LockerLBSListResponse.PoisBean getPoi() {
        return poi;
    }

    public void setPoi(LockerLBSListResponse.PoisBean poi) {
        this.poi = poi;
    }
}

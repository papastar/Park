package com.ansai.uparking.entity.body;

/**
 * User: PAPA
 * Date: 2016-12-13
 */

public class PoiBody {
    private String lockerId;
    private String userId;
    private String firstPrice;
    private String secondPrice;

    public PoiBody(String lockerId, String userId) {
        this.lockerId = lockerId;
        this.userId = userId;
    }
    public PoiBody(String lockerId, String userId, String firstPrice, String secondPrice) {
        this.lockerId = lockerId;
        this.userId = userId;
        this.firstPrice = firstPrice;
        this.secondPrice = secondPrice;
    }

    public String getFirstPrice() {
        return firstPrice;
    }

    public String getSecondPrice() {
        return secondPrice;
    }

    public String getLockerId() {
        return lockerId;
    }

    public String getUserId() {
        return userId;
    }
}

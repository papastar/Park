package com.ansai.uparking.api;

/**
 * User: PAPA
 * Date: 2016-12-13
 */

public enum RentState {

    RENT_CAN(0),//已出租
    RENT_PUBLISH(1),//已发布
    RENT_RESERVE(2),//已预定
    RENT_RENT(3);//已被租

    private int state;

    private RentState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}

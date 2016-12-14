package com.papa.park.api;

/**
 * User: PAPA
 * Date: 2016-12-13
 */

public enum RentState {

    RENT_NOT(0),//未出租
    RENT_CAN(1),//已出租
    RENT_PUBLISH(2),//已发布
    RENT_RESERVE(3),//已预定
    RENT_RENT(4);//已被租

    private int state;

    private RentState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}

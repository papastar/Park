package com.papa.park.data;

/**
 * User: PAPA
 * Date: 2016-11-15
 */

public class LocationInfo {

    private String mAddressInfo;
    private double mLatitude;
    private double mLongitude;

    public LocationInfo(String addressInfo, double latitude, double longitude) {
        mAddressInfo = addressInfo;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getAddressInfo() {
        return mAddressInfo;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}

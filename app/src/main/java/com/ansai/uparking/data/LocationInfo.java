package com.ansai.uparking.data;

/**
 * User: PAPA
 * Date: 2016-11-15
 */

public class LocationInfo {

    private String mAddressInfo;
    private double mLatitude;
    private double mLongitude;
    private String mCityCode;
    private String mCityName;

    public LocationInfo(String addressInfo, double latitude, double longitude) {
        mAddressInfo = addressInfo;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
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

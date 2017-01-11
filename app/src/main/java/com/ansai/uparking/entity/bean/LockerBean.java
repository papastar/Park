package com.ansai.uparking.entity.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class LockerBean {
    public String _id;
    public ParkingLot parkingLot;
    public String owner;
    public String sn;
    public String bluetooth;
    public String bluetoothName;
    public String hardware;
    public String token;
    public String baseKey;
    public String key;
    public Location location;
    public String address;
    public String note;
    public String isAuto;
    public String _v;


    public static class ParkingLot {
        public String _id;
        public String cityCode;
        public String cityName;
        public String name;
        public String address;
    }


    public static class Location {
        public String type;
        public List<String> coordinates;
    }

}

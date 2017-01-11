package com.ansai.uparking.entity.database;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * User: PAPA
 * Date: 2016-11-16
 */

@Table("BleData")
public class BleData {
    @PrimaryKey(AssignType.BY_MYSELF)
    public int id;

    public String blueName;

    public String lockId;

    public String blueAddress;

    public String noteName;

    public String key;

    public String sn;

    public String lockToken;

    public String openId;

    public String isOwner;

    public String isuse;

    public String lockLong;

    public String lockLat;

    public String lockAddress;

    public String cityCode;

    public String cityName;

    public String parkingName;

    public String parkingAddress;

    public String betocellphone;

}

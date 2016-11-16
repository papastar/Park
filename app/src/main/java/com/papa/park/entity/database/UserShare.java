package com.papa.park.entity.database;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * User: PAPA
 * Date: 2016-11-16
 */

public class UserShare {
    @PrimaryKey(AssignType.BY_MYSELF)
    public int id;

    public String openId;

    public String shareId;

    public String guest;

    public String usableDate;

    public String sn;

    public String bluetooth;
}

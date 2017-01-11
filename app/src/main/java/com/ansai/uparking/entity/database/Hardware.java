package com.ansai.uparking.entity.database;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * User: PAPA
 * Date: 2016-11-16
 */

public class Hardware {

    @PrimaryKey(AssignType.BY_MYSELF)
    public int id;

    public String hardwareCode;

    public String hardwareSoftCode;

    public String openId;

    public String sn;
}

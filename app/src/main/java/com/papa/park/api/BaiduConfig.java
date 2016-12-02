package com.papa.park.api;

import java.util.HashMap;
import java.util.Map;

/**
 * User: PAPA
 * Date: 2016-12-01
 */

public final class BaiduConfig {

    public static final String ANDROID_AK = "keb4LGtsu2QG8XCN2LnDUsXUDmvPU2Fc";
    public static final String SERVER_AK = "l9AggG4hCdK2XN3PDSQtEfDG1aaVHTX8";

    public static final String GEOTABLE_ID = "158209";


    public static Map<String,String> getCommonParam(){
        Map<String,String> param = new HashMap<>();
        param.put("ak",SERVER_AK);
        param.put("geotable_id",GEOTABLE_ID);
        return param;
    }





}

package com.papa.park.data;

import com.litesuits.orm.LiteOrm;
import com.papa.libcommon.util.AppUtils;

/**
 * User: PAPA
 * Date: 2016-11-16
 */

public class DbManager {
    private static final String DB_NAME = "locker.db";
    private static DbManager instance = null;
    private LiteOrm mLiteOrm;

    private DbManager() {
        mLiteOrm = LiteOrm.newSingleInstance(AppUtils.getAppContext(), DB_NAME);
    }

    public static DbManager getInstance() {
        synchronized (DbManager.class) {
            if (instance == null) {
                instance = new DbManager();
            }
        }
        return instance;
    }

    public LiteOrm getLiteOrm() {
        return mLiteOrm;
    }

}

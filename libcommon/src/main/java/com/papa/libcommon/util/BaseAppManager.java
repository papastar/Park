package com.papa.libcommon.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Papa on 2016/5/27.
 */
public class BaseAppManager {

    private static BaseAppManager mInstance;
    private Stack<Activity> mActivityStack;

    private BaseAppManager() {
        mActivityStack = new Stack<>();
    }

    /**
     * 单例
     *
     * @return mInstace对象
     */
    public static BaseAppManager getInstance() {
        if (mInstance == null) {
            mInstance = new BaseAppManager();
        }
        return mInstance;
    }

    public int size() {
        return mActivityStack.size();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getActivity() {
        if (mActivityStack == null || mActivityStack.isEmpty()) {
            return null;
        }
        Activity activity = mActivityStack.lastElement();

        return activity;
    }

    /**
     * 将当前的Activity给到配置类
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        removeActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            // activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                removeActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 清除home之上的activity
     */
//    public void finishAllActivityNoHome() {
//        while (!(mActivityStack.peek() instanceof MainActivity)) {
//            Activity activity = mActivityStack.pop();
//            if (activity != null)
//                activity.finish();
//        }
//    }

    public boolean existActivity() {

        return false;
    }


    public void exit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * 获取最底层的Activity
     * @return
     */
    public Activity getFirstElement(){
        if(mActivityStack != null && !mActivityStack.isEmpty()){
            return  mActivityStack.firstElement();
        }else {
            return  null;
        }

    }
}

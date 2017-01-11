package com.ansai.libcommon.util;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;

/**
 * User: PAPA
 * Date: 2016-11-22
 */

public final class Logger {

    private Logger() {

    }

    public static void init(String tag, boolean debug) {
        LogConfiguration config = new LogConfiguration.Builder()
                .tag(tag)                                     // 指定 TAG，默认为 "X-LOG"
//                .t()                                                   // 允许打印线程信息，默认禁止
//                .st(2)                                                 // 允许打印深度为2的调用栈信息，默认禁止
//                .b()                                                   // 允许打印日志边框，默认禁止
//                .jsonFormatter(new MyJsonFormatter())                  // 指定 JSON 格式化器，默认为
//                // DefaultJsonFormatter
//                .xmlFormatter(new MyXmlFormatter())                    // 指定 XML 格式化器，默认为
//                // DefaultXmlFormatter
//                .throwableFormatter(new MyThrowableFormatter())        // 指定可抛出异常格式化器，默认为
//                // DefaultThrowableFormatter
//                .threadFormatter(new MyThreadFormatter())              // 指定线程信息格式化器，默认为
//                // DefaultThreadFormatter
//                .stackTraceFormatter(new MyStackTraceFormatter())      // 指定调用栈信息格式化器，默认为
//                // DefaultStackTraceFormatter
//                .borderFormatter(new MyBoardFormatter())               // 指定边框格式化器，默认为
//                // DefaultBorderFormatter
//                .addObjectFormatter(AnyClass.class,                    // 为指定类添加格式化器
//                        new AnyClassObjectFormatter())                 // 默认使用 Object.toString()
                .build();
//
//        Printer androidPrinter = new AndroidPrinter();             // 通过 android.util.Log 打印日志的打印器
//        Printer SystemPrinter = new SystemPrinter();               // 通过 System.out.println
// 打印日志的打印器
//        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
//                .Builder("/sdcard/xlog/")                              // 指定保存日志文件的路径
//                .fileNameGenerator(new DateFileNameGenerator())        // 指定日志文件名生成器，默认为
//                // ChangelessFileNameGenerator("log")
//                .backupStrategy(new MyBackupStrategy())                // 指定日志文件备份策略，默认为
//                // FileSizeBackupStrategy(1024 * 1024)
//                .logFlattener(new MyLogFlattener())                    // 指定日志平铺器，默认为
//                // DefaultLogFlattener
//                .build();

        XLog.init(debug ? LogLevel.ALL : LogLevel.NONE,                                    //
                // 指定日志级别，低于该级别的日志将不会被打印
                config);                                               // 指定日志配置，如果不指定，会默认使用 new
        // LogConfiguration.Builder().buil);
    }

    public static void d(String message, Object... objects) {
        XLog.d(message, objects);
    }

    public static void d(String tag, String message, Object... objects) {
        XLog.tag(tag).d(message);
    }

    public static void json(String json) {
        XLog.json(json);
    }

    public static void json(String tag, String json) {
        XLog.tag(tag).json(json);
    }
}

package com.taotao.cloud.agent.demo.common;

/**
 * Created by pphh on 2022/6/23.
 */
public class Logger {

    public static Boolean ENABLED_VERBOSE = Boolean.FALSE;

    public static void info(String format, Object... args) {
        String msg = String.format(format, args);
        Logger.info(msg);
    }

    public static void info(String msg) {
        logMessage(msg, "INFO");
    }

    public static void error(String format, Object... args) {
        String msg = String.format(format, args);
        Logger.error(msg);
    }

    public static void error(String msg) {
        logMessage(msg, "ERROR");
    }

    public static void error(String msg, Throwable throwable) {
        logMessage(msg, "ERROR");
    }

    public static void debug(String format, Object... args) {
        String msg = String.format(format, args);
        Logger.logMessage(msg, "DEBUG");
    }

    public static void verbose(String format, Object... args) {
        if (ENABLED_VERBOSE) {
            String msg = String.format(format, args);
            Logger.logMessage(msg, "VERBOSE");
        }
    }

    private static void logMessage(String msg, String type) {
        String threadName = Thread.currentThread().getName();
        String strTime = TimeDateUtil.getCurrentTimeString();

        String info = String.format("[%s][%s][%s] %s", strTime, threadName, type, msg);
        System.out.println(info);
    }

}

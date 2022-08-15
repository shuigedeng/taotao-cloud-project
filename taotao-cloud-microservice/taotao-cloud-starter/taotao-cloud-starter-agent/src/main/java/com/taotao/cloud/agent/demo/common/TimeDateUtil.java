package com.taotao.cloud.agent.demo.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pphh on 2022/6/23.
 */
public class TimeDateUtil {

    public static String getCurrentTimeString() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss-SSS");
        return sdFormatter.format(nowTime);
    }

}

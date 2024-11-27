package com.taotao.cloud.job.common.utils.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
public class MyNetUtil {
    public static String address;

    static {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        address = inetAddress.getHostAddress();
    }



}

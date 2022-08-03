package com.taotao.cloud.sys.biz.api.controller.tools.tcp;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

    /**
     * 获取所有网卡(up,noloop, novirtual)
     * @return
     */
    public static List<NetworkInterface> networkInterfaces() throws SocketException {
        List<NetworkInterface> canUse = new ArrayList<>();

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            final boolean up = networkInterface.isUp();
            if (!up) {
                continue;
            }

            final boolean loopback = networkInterface.isLoopback();
            if (loopback) {
                continue;
            }

            final String displayName = networkInterface.getDisplayName();
            if (networkInterface.isVirtual() || displayName.toLowerCase().contains("vm")){
                continue;
            }

            canUse.add(networkInterface);
        }
        return canUse;
    }

}

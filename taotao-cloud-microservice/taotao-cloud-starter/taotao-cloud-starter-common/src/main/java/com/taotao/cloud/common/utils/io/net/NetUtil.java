package com.taotao.cloud.common.utils.io.net;


import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.common.RegexUtil;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络工具类
 */
public final class NetUtil {

    private NetUtil(){}

    /**
     * 默认的本地地址
     */
    public static final String LOCALHOST = "127.0.0.1";

    /**
     * 本地服务地址
     */
    private static final String LOCAL_HOST;

    static {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new CommonRuntimeException(e);
        }

        LOCAL_HOST = address.getHostAddress();
    }

    /**
     * 获取本地 ip 地址
     * @return ip 地址
     */
    public static String getLocalHost() {
        return LOCAL_HOST;
    }

    /**
     * 传入需要连接的IP，返回是否连接成功
     *
     * @param remoteInetAddress 远程地址
     * @return {@code true} 是
     */
    public static boolean isReachable(String remoteInetAddress) {
        return isReachable(remoteInetAddress, 5000);
    }

    /**
     * 传入需要连接的IP，返回是否连接成功
     * 注意：如果出现异常，只是简单的进行错误信息忽略
     * @param remoteInetAddress 远程地址
     * @param timeoutInMills   超时时间(milliseconds)
     * @return {@code true} 是
     */
    public static boolean isReachable(String remoteInetAddress, int timeoutInMills) {
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(remoteInetAddress);
            reachable = address.isReachable(timeoutInMills);
        } catch (Exception e) {
            //ignore error
        }
        return reachable;
    }

    /**
     * 断言处于联网状态
     * 1. 这里可能会有一个问题，如果用户的 hosts 文件中有这个网址
     */
    public static void assertOnLine() {
        final String address = "translate.google.cn";
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            throw new RuntimeException("The net work is broken, " +
                    "check your network or set isCommentWhenNetworkBroken=true.");
        }
    }

    /**
     * 获取当前机器的 IP 地址
     * @return IP 地址
     */
    public static String getLocalIp() {
        InetAddress inetAddress = findLocalAddress();
        if(inetAddress == null) {
            return null;
        }

        String ip = inetAddress.getHostAddress();
        if(RegexUtil.isIp(ip)) {
            return ip;
        }

        return null;
    }

    /**
     * 查询本地地址
     * @return 地址
     */
    private static InetAddress findLocalAddress() {
        String preferNamePrefix = "bond0";
        String defaultNicList = "bond0,eth0,em0,en0,em1,br0,eth1,em2,en1,eth2,em3,en2,eth3,em4,en3";

        InetAddress resultAddress = null;
        Map<String, NetworkInterface> candidateInterfaces = new HashMap<>();

        // 遍历所有网卡，找出所有可用网卡，尝试找出符合prefer前缀的网卡
        try {
            for (Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces(); allInterfaces
                    .hasMoreElements(); ) {
                NetworkInterface nic = allInterfaces.nextElement();
                // 检查网卡可用并支持广播
                if (!nic.isUp() || !nic.supportsMulticast()) {
                    continue;
                }

                // 检查是否符合prefer前缀
                String name = nic.getName();
                if (name.startsWith(preferNamePrefix)) {
                    resultAddress = findAvailableAddress(nic);
                    if (resultAddress != null) {
                        return resultAddress;
                    }
                } else {
                    // 不是Prefer前缀，先放入可选列表
                    candidateInterfaces.put(name, nic);
                }
            }

            for (String nifName : defaultNicList.split(PunctuationConst.COMMA)) {
                NetworkInterface nic = candidateInterfaces.get(nifName);
                if (nic != null) {
                    resultAddress = findAvailableAddress(nic);
                    if (resultAddress != null) {
                        return resultAddress;
                    }
                }
            }

            return null;
        } catch (SocketException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 检查有否非ipv6，非127.0.0.1
     */
    private static InetAddress findAvailableAddress(NetworkInterface nic) {
        for (Enumeration<InetAddress> inetAddresses = nic.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
            InetAddress inetAddress = inetAddresses.nextElement();
            if (!(inetAddress instanceof Inet6Address) && !inetAddress.isLoopbackAddress()) {
                return inetAddress;
            }
        }
        return null;
    }

}

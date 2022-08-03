package com.taotao.cloud.sys.biz.api.controller.tools.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NetUtil {
    /**
     * 是否主机端口可以连接
     * @param host
     * @param port
     * @return
     */
    public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            log.error("isHostConnectable[{}:{}] connect fail : {}",host,port,e.getMessage());
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
        }
        return true;
    }
    /**
     * 判断 ip 是否可以 ping 通
     * @param host
     * @return
     */
    public static boolean isHostReachable460(String host){
        return isHostReachable(host,460);
    }
    /**
     * 主机是否可达
     * @param host
     * @param timeOut
     * @return
     */
    public static boolean isHostReachable(String host, Integer timeOut) {
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (IOException e) {
           log.error("isHostReachable () error : {}",e.getMessage(),e);
        }
        return false;
    }

    /**
     * IceWee 2013.07.19
     * 获取本地IP列表（针对多网卡情况）
     *
     * @return
     */
    public static List<String> getLocalIPs() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        if (!(ip.contains("127.0.0.1") || ip.contains("0::0::0"))){
                            ipList.add(ip);
                        }

                    }
                }
            }
        } catch (SocketException e) {
           log.error("getLocalIPs() error : {}",e.getMessage(),e);
        }
        return ipList;
    }

    /**
     * 获取本机 mac 地址
     * @return
     */
    public List<String> localMacs() throws SocketException {
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> tmpMacList = new ArrayList<>();
        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                sb.delete(0, sb.length());
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                tmpMacList.add(sb.toString());
            }
        }
        if (tmpMacList.size() <= 0) {
            return tmpMacList;
        }
        List<String> unique = tmpMacList.stream().distinct().collect(Collectors.toList());
        return unique;
    }

    public static HttpServletRequest request(){
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }
    /**
     * 远程地址
     * @return
     */
    public static String remoteAddr(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr;
    }
}

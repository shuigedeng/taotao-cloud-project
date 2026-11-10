package com.taotao.cloud.mq.remoting.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 远程通信辅助类
 */
public class RemotingHelper {

    public static final String OS_NAME = System.getProperty("os.name");
    private static final Logger log = LoggerFactory.getLogger(RemotingHelper.class);
    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains("windows")) {
            isWindowsPlatform = true;
        }
    }

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }

    public static boolean isWindowsPlatform() {
        return isWindowsPlatform;
    }

    /**
     * 获取通道的远程地址字符串
     */
    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }
        }
        return addr;
    }

    /**
     * 获取通道的本地地址字符串
     */
    public static String parseChannelLocalAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        SocketAddress local = channel.localAddress();
        final String addr = local != null ? local.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }
        }
        return addr;
    }

    /**
     * 获取通道的远程主机名
     */
    public static String parseChannelRemoteName(final Channel channel) {
        if (null == channel) {
            return "";
        }
        InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
        if (remote != null) {
            return remote.getAddress().getHostName();
        }
        return "";
    }

    /**
     * 关闭通道
     */
    public static void closeChannel(Channel channel) {
        final String addrRemote = parseChannelRemoteAddr(channel);
        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("closeChannel: close the connection to remote address[{}] result: {}",
                        addrRemote, future.isSuccess());
            }
        });
    }

    /**
     * 异常处理
     */
    public static void handleException(Throwable t, String remoteAddr) {
        if (t != null) {
            log.error("Exception occurred in channel[{}]", remoteAddr, t);
        }
    }

    /**
     * 解析Socket地址
     */
    public static InetSocketAddress string2SocketAddress(final String addr) {
        int split = addr.lastIndexOf(":");
        String host = addr.substring(0, split);
        String port = addr.substring(split + 1);
        return new InetSocketAddress(host, Integer.parseInt(port));
    }

    /**
     * 格式化Socket地址
     */
    public static String socketAddress2String(final InetSocketAddress addr) {
        return addr.getAddress().getHostAddress() + ":" + addr.getPort();
    }

    /**
     * 获取异常简单描述
     *
     * @param e 异常
     * @return 简单描述
     */
    public static String exceptionSimpleDesc(final Throwable e) {
        if (e == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName());
        if (e.getMessage() != null) {
            sb.append(" - ").append(e.getMessage());
        }
        return sb.toString();
    }
}

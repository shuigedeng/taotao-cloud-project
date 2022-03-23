package com.taotao.cloud.common.utils.system;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 系统工具类
 */
public final class OsUtil {

    private OsUtil(){}

    private static final String ARCH = System.getProperty("sun.arch.data.model");

    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * 查看指定的端口号是否空闲，若空闲则返回否则返回一个随机的空闲端口号
     * @param defaultPort 默认端口
     * @return 端口
     * @throws IOException 异常
     */
    public static int getFreePort(int defaultPort) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(defaultPort)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            return getFreePort();
        }
    }

    /**
     * 获取空闲端口号
     * @return 端口
     * @throws IOException 异常
     */
    public static int getFreePort() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        }
    }

    /**
     * 检查端口号是否被占用
     * @param port 端口号
     * @return 是否
     */
    public static boolean isBusyPort(int port) {
        boolean ret = true;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否为 windows
     * @return 是否
     */
    public static boolean isWindows() {
        return OS.contains("win");
    }

    /**
     * 是否为 windows xp
     * @return 是否
     */
    public static boolean isWindowsXP() {
        return OS.contains("win") && OS.contains("xp");
    }

    /**
     * 是否为 mac
     * @return 是否
     */
    public static boolean isMac() {
        return OS.contains("mac");
    }

    /**
     * 是否为 unix
     * @return 是否
     */
    public static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    /**
     * 是否为 sunos
     * @return 是否
     */
    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }

    /**
     * 是否为 64 位
     * @return 是否
     */
    public static boolean is64() {
        return "64".equals(ARCH);
    }

    /**
     * 是否为 32 位
     * @return 是否
     */
    public static boolean is32() {
        return "32".equals(ARCH);
    }

}

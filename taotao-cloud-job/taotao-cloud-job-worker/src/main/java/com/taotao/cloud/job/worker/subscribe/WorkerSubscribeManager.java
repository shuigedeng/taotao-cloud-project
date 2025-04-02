package com.taotao.cloud.job.worker.subscribe;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class WorkerSubscribeManager {

    private static boolean splitStatus;
    private static boolean changeServerStatus;
    @Getter
    private static String subAppName;
    @Getter
    private static List<String> serverIpList;
    @Getter
    private static AtomicLong scheduleTimes = new AtomicLong(0);
    @Getter
    private static String currentServerIp = "";

    public static void setCurrentServerIp(String currentServerIp) {
        WorkerSubscribeManager.currentServerIp = currentServerIp;
    }

    public static void setChangeServerStatus(boolean changeServerStatus) {
        WorkerSubscribeManager.changeServerStatus = changeServerStatus;
    }
    public static void setServerIpList(List<String> serverIpList) {
        WorkerSubscribeManager.serverIpList = serverIpList;
    }

    public static boolean isSplit() {
        return splitStatus;
    }
    public static boolean isChangeServer() {
        return changeServerStatus;
    }

    public static void setSplitStatus(boolean splitStatus) {
        WorkerSubscribeManager.splitStatus = splitStatus;
    }

    public static void setSubAppName(String subAppName) {
        WorkerSubscribeManager.subAppName = subAppName;
    }

    public static void addScheduleTimes() {
        scheduleTimes.incrementAndGet();
        scheduleTimes.incrementAndGet();
        scheduleTimes.incrementAndGet();
        scheduleTimes.incrementAndGet();
        scheduleTimes.incrementAndGet();
        scheduleTimes.incrementAndGet();
    }
    public static void resetScheduleTimes(){
        scheduleTimes.set(0);
    }
}

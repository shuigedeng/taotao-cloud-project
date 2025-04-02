package com.taotao.cloud.job.nameserver.module.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
@Getter
public class FullSyncInfo extends SyncInfo{
    public FullSyncInfo(Set<String> serverAddressSet, Set<String> workerIpAddressSet, Map<String, Integer> appName2WorkerNumMap, Map<String, Long> serverAddress2ScheduleTimesMap){
        super(null);
        this.serverAddressSet = serverAddressSet;
        this.workerIpAddressSet = workerIpAddressSet;
        this.appName2WorkerNumMap = appName2WorkerNumMap;
        this.serverAddress2ScheduleTimesMap = serverAddress2ScheduleTimesMap;
    }
    private Set<String> serverAddressSet;
    private Set<String> workerIpAddressSet;
    /**
     * for split group
     */
    private Map<String, Integer> appName2WorkerNumMap;
    /**
     * for dynamic change group
     */
    private  Map<String, Long> serverAddress2ScheduleTimesMap;
}

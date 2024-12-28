/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.job.server.nameserver.balance;

import com.google.common.collect.Maps;
import com.taotao.cloud.job.server.nameserver.module.ReBalanceInfo;
import com.taotao.cloud.remote.protos.RegisterCausa;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServerIpAddressManagerService {
    @Value("${ttcjob.name-server.max-worker-num}")
    private int maxWorkerNum;

    @Getter
    private final Set<String> serverAddressSet = new HashSet<>();

    private final Set<String> workerIpAddressSet = new HashSet<>();
    /**
     * for split group
     */
    private final Map<String, Integer> appName2WorkerNumMap = Maps.newHashMap();

    //    private final Map<String, Integer> serverIp2ConnectNumMap = Maps.newHashMap();

    /**
     * for dynamic change group
     */
    private final Map<String, Long> serverAddress2ScheduleTimesMap = Maps.newHashMap();

    public void add2ServerAddressSet(RegisterCausa.ServerRegisterReporter req) {
        serverAddressSet.add(req.getServerIpAddress());
        //        serverIp2ConnectNumMap.put(req.getServerIpAddress(), 0);
    }

    public void addScheduleTimes(String serverIpAddress, long scheduleTime) {
        if (!serverIpAddress.isEmpty()) {
            serverAddress2ScheduleTimesMap.put(
                    serverIpAddress, serverAddress2ScheduleTimesMap.getOrDefault(serverIpAddress, 0L) + scheduleTime);
        }
    }

    public void addAppName2WorkerNumMap(String workerIpAddress, String appName) {
        if (!workerIpAddressSet.contains(workerIpAddress)) {
            workerIpAddressSet.add(workerIpAddress);
            appName2WorkerNumMap.put(appName, appName2WorkerNumMap.getOrDefault(appName, 0) + 1);
        }
    }

    public ReBalanceInfo getServerAddressReBalanceList(String serverAddress, String appName) {
        // first req, serverAddress is empty
        if (serverAddress.isEmpty()) {
            ReBalanceInfo reBalanceInfo = new ReBalanceInfo();
            reBalanceInfo.setSplit(false);
            reBalanceInfo.setServerIpList(new ArrayList<String>(serverAddressSet));
            reBalanceInfo.setSubAppName("");
            return reBalanceInfo;
        }
        ReBalanceInfo reBalanceInfo = new ReBalanceInfo();
        // get sorted scheduleTimes serverList
        List<String> newServerIpList = serverAddress2ScheduleTimesMap.keySet().stream()
                .sorted(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return (int) (serverAddress2ScheduleTimesMap.get(o1) - serverAddress2ScheduleTimesMap.get(o2));
                    }
                })
                .collect(Collectors.toList());

        // see if split
        if (!appName2WorkerNumMap.isEmpty()
                && appName2WorkerNumMap.get(appName) > maxWorkerNum
                && appName2WorkerNumMap.get(appName) % maxWorkerNum == 1) {
            // return new serverIpList
            reBalanceInfo.setSplit(true);
            reBalanceInfo.setChangeServer(false);
            reBalanceInfo.setServerIpList(newServerIpList);
            reBalanceInfo.setSubAppName(appName + ":" + appName2WorkerNumMap.size());
            return reBalanceInfo;
        }
        // see if need change server
        Long lestScheduleTimes = serverAddress2ScheduleTimesMap.get(newServerIpList.get(newServerIpList.size() - 1));
        Long comparedScheduleTimes = lestScheduleTimes == 0 ? 1 : lestScheduleTimes;
        if (serverAddress2ScheduleTimesMap.get(serverAddress) / comparedScheduleTimes > 2) {
            reBalanceInfo.setSplit(false);
            reBalanceInfo.setChangeServer(true);
            // first server is target lest scheduleTimes server
            reBalanceInfo.setServerIpList(newServerIpList);
            reBalanceInfo.setSubAppName("");
            return reBalanceInfo;
        }
        // return default list
        reBalanceInfo.setSplit(false);
        reBalanceInfo.setServerIpList(new ArrayList<String>(serverAddressSet));
        reBalanceInfo.setSubAppName("");
        return reBalanceInfo;
    }
}

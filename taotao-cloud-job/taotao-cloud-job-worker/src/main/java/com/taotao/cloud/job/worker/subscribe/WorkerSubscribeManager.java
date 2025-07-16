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

package com.taotao.cloud.job.worker.subscribe;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;

public class WorkerSubscribeManager {

    private static boolean splitStatus;
    private static boolean changeServerStatus;
    @Getter private static String subAppName;
    @Getter private static List<String> serverIpList;
    @Getter private static AtomicLong scheduleTimes = new AtomicLong(0);
    @Getter private static String currentServerIp = "";

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

    public static void resetScheduleTimes() {
        scheduleTimes.set(0);
    }
}

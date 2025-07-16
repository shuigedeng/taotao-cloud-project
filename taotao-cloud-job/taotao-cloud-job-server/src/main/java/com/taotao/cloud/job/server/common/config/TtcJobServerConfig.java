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

package com.taotao.cloud.job.server.common.config;

import com.taotao.cloud.job.common.constant.RemoteConstant;
import com.taotao.cloud.job.common.utils.net.MyNetUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "ttcjob.server")
public class TtcJobServerConfig {

    private String address = MyNetUtil.address;
    private Integer workerPort = RemoteConstant.DEFAULT_WORKER_GRPC_PORT;
    private String nameServerAddress;
    private Integer serverPort = RemoteConstant.DEFAULT_SERVER_GRPC_PORT;

    // 为了让单例池拿到
    public static String staticNameServerAddress;
    public static Integer staticServerPort;
    public static Integer staticWorkerPort;

    @PostConstruct
    public void initStaticFields() {
        staticNameServerAddress = this.nameServerAddress;
        staticServerPort = this.serverPort;
        staticWorkerPort = this.workerPort;
    }

    public void setNameServerAddress(String nameServerAddress) {
        this.nameServerAddress = nameServerAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWorkerPort(int workerPort) {
        this.workerPort = workerPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }
}

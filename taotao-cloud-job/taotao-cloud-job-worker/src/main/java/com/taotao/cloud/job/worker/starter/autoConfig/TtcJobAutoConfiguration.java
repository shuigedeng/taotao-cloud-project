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

package com.taotao.cloud.job.worker.starter.autoConfig;

// import org.kjob.common.utils.NetUtils;

import com.taotao.cloud.job.worker.TtcJobSpringWorker;
import com.taotao.cloud.job.worker.common.TtcJobWorkerConfig;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TtcJobProperties.class)
@ConditionalOnProperty(
        prefix = "ttcjob.worker",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class TtcJobAutoConfiguration {

    @Autowired TtcJobProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public TtcJobSpringWorker initKJobWorker() {

        TtcJobProperties.Worker worker = properties.getWorker();

        List<String> serverAddress = Arrays.asList(worker.getServerAddress().split(","));

        TtcJobWorkerConfig config = new TtcJobWorkerConfig();

        if (worker.getPort() != null) {
            config.setPort(worker.getPort());
        }
        if (worker.getServerPort() != null) {
            config.setServerPort(worker.getServerPort());
        }

        config.setAppName(worker.getAppName());
        config.setServerAddress(serverAddress);
        config.setNameServerAddress(worker.getNameServerAddress());
        config.setMaxHeavyweightTaskNum(worker.getMaxHeavyweightTaskNum());
        config.setMaxLightweightTaskNum(worker.getMaxLightweightTaskNum());
        config.setHealthReportInterval(worker.getHealthReportInterval());
        return new TtcJobSpringWorker(config);
    }
}

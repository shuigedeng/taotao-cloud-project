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

package com.taotao.cloud.sys.biz.event.application;

import com.alibaba.cloud.nacos.event.NacosDiscoveryInfoChangedEvent;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class NacosEventListener {

    @Component
    public static class NacosDiscoveryInfoChangedEventListener
            implements ApplicationListener<NacosDiscoveryInfoChangedEvent> {
        @Override
        public void onApplicationEvent(NacosDiscoveryInfoChangedEvent event) {
            LogUtils.info(
                    "NacosEventListener ----- NacosDiscoveryInfoChangedEvent onApplicationEvent {}",
                    event);
        }
    }

    // @Autowired
    // private NacosRefresher nacosRefresher;
    //
    // @NacosConfigListener(dataId = "config")
    // private void onMessage(String msg) {
    // 	nacosRefresher.refresh(msg, ConfigFileTypeEnum.YAML);
    // 	System.out.println("配置变动" + msg);
    // }
    //
    // @Component
    // public static class NacosRefresher extends AbstractRefresher {
    // }

}

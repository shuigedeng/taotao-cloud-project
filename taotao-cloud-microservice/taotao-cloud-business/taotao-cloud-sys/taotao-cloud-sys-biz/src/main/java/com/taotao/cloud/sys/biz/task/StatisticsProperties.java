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

package com.taotao.cloud.sys.biz.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 在线人数统计
 *
 * @author Chopper
 * @version v4.0
 * @since 2021/2/21 10:19
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "taotao.statistics")
public class StatisticsProperties {

    /** 在线人数统计 X 小时 */
    private Integer onlineMember = 48;

    /** 当前在线人数 刷新时间间隔 */
    private Integer currentOnlineUpdate = 600;

    public Integer getOnlineMember() {
        if (onlineMember == null) {
            return 48;
        }
        return onlineMember;
    }

    public Integer getCurrentOnlineUpdate() {
        if (currentOnlineUpdate == null) {
            return 600;
        }
        return currentOnlineUpdate;
    }
}

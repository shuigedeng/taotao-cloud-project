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

package com.taotao.cloud.auth.biz.management.compliance.processor.loginrisk;

import lombok.Data;
import lombok.experimental.*;

/**
 * RiskRule
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
public class RiskRule {

    private Integer id;

    /**
     * 风险名称
     */
    private String riskName;

    /**
     * 白名单ip
     */
    private String acceptIp;

    /**
     * 触发次数
     */
    private Integer triggerNumber;

    /**
     * 触发时间
     */
    private Integer triggerTime;

    /**
     * 触发时间类型
     */
    private Integer triggerTimeType;

    /**
     * 异常登录时间 （json）
     */
    private String unusualLoginTime;

    /**
     * 采取的操作措施 1：提示 2：发送短信  3：阻断登录  4：封号
     */
    private Integer operate;
}

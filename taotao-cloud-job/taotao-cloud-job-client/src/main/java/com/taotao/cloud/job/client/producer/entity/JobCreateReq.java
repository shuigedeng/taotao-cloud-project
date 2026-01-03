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

package com.taotao.cloud.job.client.producer.entity;

import com.taotao.cloud.job.common.enums.TimeExpressionType;
import com.taotao.cloud.job.common.module.LifeCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JobCreateReq
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreateReq {

    /**
     * 以appName为分组，被该app下的某个subApp所调度
     */
    private String appName;

    private String jobName;
    private String jobDescription;
    private String jobParams;

    private TimeExpressionType timeExpressionType;
    private String timeExpression;
    private LifeCycle lifeCycle;

    private String processorInfo;
    private int maxInstanceNum;
}

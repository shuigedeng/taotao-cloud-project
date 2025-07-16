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

package com.taotao.cloud.job.worker.common.module;

import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 *
 * @TableName instance_info
 */
@Getter
@Setter
public class InstanceInfo implements Serializable {
    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long id;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long actualTriggerTime;

    // 处理器类型（JavaBean、Jar、脚本等）
    private String processorType;
    // 处理器信息
    private String processorInfo;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long appId;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long expectedTriggerTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long finishedTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Date gmtCreate;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Date gmtModified;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long instanceId;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String instanceParams;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long jobId;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String jobParams;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long lastReportTime;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String result;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Long runningTimes;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Integer status;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private String taskTrackerAddress;

    /**
     *
     * -- GETTER --
     *
     *
     */
    private Integer type;

    /**
     *
     * -- GETTER --
     *
     *
     */
}

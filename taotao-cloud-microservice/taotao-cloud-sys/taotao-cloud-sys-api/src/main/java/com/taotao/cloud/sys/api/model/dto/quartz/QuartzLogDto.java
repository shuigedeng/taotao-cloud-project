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
package com.taotao.cloud.sys.api.model.dto.quartz;


import java.io.Serializable;
import java.sql.Timestamp;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class QuartzLogDto implements Serializable {

    /** 定时任务名称 */
    private String beanName;

    /** Bean名称  */
    private Timestamp createTime;

    /** cron表达式 */
    private String cronExpression;

    /** 异常详细  */
    private String exceptionDetail;

    /** 状态 */
    private Boolean isSuccess;

    /** 任务名称 */
    private String jobName;

    /** 方法名称 */
    private String methodName;

    /** 参数 */
    private String params;

    /** 耗时（毫秒） */
    private Long time;

}

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

package com.taotao.cloud.sys.api.model.vo;

//import com.taotao.boot.job.quartz.enums.QuartzJobCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 定时任务 */
@Data
@Schema(title = "定时任务")
public class QuartzJobVO {

    /** 任务名称 */
    private String name;

    /** 任务类名 */
    private String jobClassName;

    /** cron表达式 */
    private String cron;

    /** 参数 */
    private String parameter;

    /**
     * 状态
     */
    private Integer state;

    /** 备注 */
    private String remark;
}

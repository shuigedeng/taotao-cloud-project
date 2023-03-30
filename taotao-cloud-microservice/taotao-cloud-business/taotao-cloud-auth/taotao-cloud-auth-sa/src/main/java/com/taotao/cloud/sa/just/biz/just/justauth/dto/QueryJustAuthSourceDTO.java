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

package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户第三方登录信息配置表
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "JustAuthSource对象", description = "租户第三方登录信息配置表")
public class QueryJustAuthSourceDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String sourceName;

    @Schema(description = "登录类型")
    private String sourceType;

    @Schema(description = "状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "开始时间")
    private String beginDateTime;

    @Schema(description = "结束时间")
    private String endDateTime;
}

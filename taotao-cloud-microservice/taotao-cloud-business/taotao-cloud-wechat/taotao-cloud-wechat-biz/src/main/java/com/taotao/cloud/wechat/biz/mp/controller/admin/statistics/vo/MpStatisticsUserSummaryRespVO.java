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

package com.taotao.cloud.wechat.biz.mp.controller.admin.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@ApiModel("管理后台 - 某一天的粉丝增减数据 Response VO")
@Data
public class MpStatisticsUserSummaryRespVO {

    @ApiModelProperty(value = "日期", required = true)
    private Date refDate;

    @ApiModelProperty(value = "粉丝来源", required = true, example = "0")
    private Integer userSource;

    @ApiModelProperty(value = "新关注的粉丝数量", required = true, example = "10")
    private Integer newUser;

    @ApiModelProperty(value = "取消关注的粉丝数量", required = true, example = "20")
    private Integer cancelUser;
}

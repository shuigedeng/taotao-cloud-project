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

@ApiModel("管理后台 - 某一天的接口分析数据 Response VO")
@Data
public class MpStatisticsInterfaceSummaryRespVO {

    @ApiModelProperty(value = "日期", required = true)
    private Date refDate;

    @ApiModelProperty(value = "通过服务器配置地址获得消息后，被动回复粉丝消息的次数", required = true, example = "10")
    private Integer callbackCount;

    @ApiModelProperty(value = "上述动作的失败次数", required = true, example = "20")
    private Integer failCount;

    @ApiModelProperty(value = "总耗时，除以 callback_count 即为平均耗时", required = true, example = "30")
    private Integer totalTimeCost;

    @ApiModelProperty(value = "最大耗时", required = true, example = "40")
    private Integer maxTimeCost;
}

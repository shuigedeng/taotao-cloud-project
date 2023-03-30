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

package com.taotao.cloud.wechat.biz.mp.controller.admin.account.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.*;
import lombok.*;

@ApiModel("管理后台 - 公众号账号分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MpAccountPageReqVO extends PageParam {

    @ApiModelProperty(value = "公众号名称", notes = "模糊匹配")
    private String name;

    @ApiModelProperty(value = "公众号账号", notes = "模糊匹配")
    private String account;

    @ApiModelProperty(value = "公众号 appid", notes = "模糊匹配")
    private String appId;
}

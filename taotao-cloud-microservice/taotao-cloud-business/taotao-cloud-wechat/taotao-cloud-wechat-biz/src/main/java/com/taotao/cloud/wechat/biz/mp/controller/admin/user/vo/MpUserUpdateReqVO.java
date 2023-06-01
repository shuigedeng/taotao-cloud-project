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

package com.taotao.cloud.wechat.biz.mp.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@ApiModel("管理后台 - 公众号粉丝更新 Request VO")
@Data
public class MpUserUpdateReqVO {

    @ApiModelProperty(value = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "昵称", example = "芋道")
    private String nickname;

    @ApiModelProperty(value = "备注", example = "你是一个芋头嘛")
    private String remark;

    @ApiModelProperty(value = "标签编号数组", example = "1,2,3")
    private List<Long> tagIds;
}

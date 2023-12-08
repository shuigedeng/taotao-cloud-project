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

package com.taotao.cloud.workflow.biz.app.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 
 * 
 *  
 * @since 2021/3/12 15:31
 */
@Data
public class AppUserInfoVO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "户名")
    private String realName;

    @ApiModelProperty(value = "部门名称")
    private String organizeName;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "岗位名称")
    private String positionName;

    @ApiModelProperty(value = "办公电话")
    private String telePhone;

    @ApiModelProperty(value = "办公座机")
    private String landline;

    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

    @ApiModelProperty(value = "用户头像")
    private String headIcon;

    @ApiModelProperty(value = "邮箱")
    private String email;
}

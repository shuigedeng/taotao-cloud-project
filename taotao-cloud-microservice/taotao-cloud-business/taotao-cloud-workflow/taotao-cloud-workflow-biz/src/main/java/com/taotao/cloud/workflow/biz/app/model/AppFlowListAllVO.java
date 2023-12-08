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
import java.util.List;
import lombok.Data;

/**
 * app常用数据
 *
 * @author 
 * 
 *  
 * @since 2021-08-08
 */
@Data
public class AppFlowListAllVO {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "图标")
    private String fullName;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "图标背景色")
    private String iconBackground;

    @ApiModelProperty(value = "表单类型 1-系统表单、2-动态表单")
    private Integer formType;

    @ApiModelProperty(value = "编码")
    private String enCode;

    @ApiModelProperty(value = "是否常用")
    private Boolean isData;

    @ApiModelProperty(value = "子节点")
    private List<AppFlowListAllVO> children;
}

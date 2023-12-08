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
public class AppDataListAllVO {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "是否有下级菜单")
    private Boolean hasChildren;

    @ApiModelProperty(value = "菜单名称")
    private String fullName;

    @ApiModelProperty(value = " 图标")
    private String icon;

    @ApiModelProperty(value = "链接地址")
    private String urlAddress;

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "菜单类型", example = "1")
    private Integer type;

    @ApiModelProperty(value = "扩展字段")
    private String propertyJson;

    @ApiModelProperty(value = "是否常用")
    private Boolean isData;

    @ApiModelProperty(value = "下级菜单列表")
    private List<AppDataListAllVO> children;
}

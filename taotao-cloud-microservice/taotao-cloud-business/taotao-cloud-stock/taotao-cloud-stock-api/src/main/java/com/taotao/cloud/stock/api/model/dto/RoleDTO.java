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

package com.taotao.cloud.stock.api.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 角色DTO
 *
 * @author shuigedeng
 * @since 2021-02-18
 */
@Data
public class RoleDTO implements Serializable {

    public RoleDTO() {}

    public RoleDTO(String id, String roleCode, String roleName, String remarks, List<String> permissionIdList) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.remarks = remarks;
        this.permissionIdList = permissionIdList;
    }

    /** id */
    private String id;

    /** 角色编码 */
    private String roleCode;

    /** 角色名称 */
    private String roleName;

    /** 状态 */
    private String status;

    /** 备注 */
    private String remarks;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 权限 */
    private List<String> permissionIdList;
}

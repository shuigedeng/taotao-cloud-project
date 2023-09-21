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
import java.util.List;
import java.util.Set;
import lombok.Data;

/**
 * 用户DTO
 *
 * @author shuigedeng
 * @since 2021-02-23
 */
@Data
public class UserDTO implements Serializable {

    /** id */
    private String id;

    /** 用户名 */
    private String userName;

    /** email */
    private String email;

    /** mobile */
    private String mobile;

    /** status */
    private String status;

    /** 当前租户 */
    private String tenantName;

    /** 角色列表 */
    private List<String> roleIdList;

    /** 权限编码 */
    private Set<String> permissionCodes;

    /** 权限id */
    private Set<String> permissionIds;

    /** 所有租户列表 */
    private List<TenantDTO> tenants;

    public UserDTO() {}

    public UserDTO(String id, String userName, String email, String mobile, String status, List<String> roleIdList) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.roleIdList = roleIdList;
    }
}

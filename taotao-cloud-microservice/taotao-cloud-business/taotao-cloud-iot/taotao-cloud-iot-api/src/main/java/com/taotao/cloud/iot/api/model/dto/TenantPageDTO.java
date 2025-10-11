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

package com.taotao.cloud.iot.api.model.dto;

import com.taotao.boot.common.model.request.PageQuery;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author
 * @version 0.0.1
 * @since 2022/11/26 15:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantPageDTO extends PageQuery implements Serializable {

    private static final long serialVersionUID = -1L;

    /** 主键 */
    private Long id;

    /** 租户名 */
    private String name;

    /** 当前租户管理员id */
    private Long tenantAdminId;

    /** 当前租户管理员姓名 */
    private String tenantAdminName;

    /** 当前租户管理员手机号 */
    private String tenantAdminMobile;

    /**
     * 租户状态
     *
     * <p>枚举 {@link GlobalStatusEnum}
     */
    private Integer status;

    /** 租户套餐id */
    private Long packageId;

    /** 租户过期时间 */
    private LocalDateTime expireTime;

    /** 账号数量 */
    private Integer accountCount;

    /** 系统套餐id */
    public static final Long PACKAGE_ID_SYSTEM = 0L;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 逻辑删除标志 */
    private Boolean delFlag;
}

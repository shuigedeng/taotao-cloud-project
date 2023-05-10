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

package com.taotao.cloud.tenant.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.GlobalStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import lombok.Data;

@TableName("tenant")
@Data
public class Tenant extends BaseSuperEntity<Tenant, Long> {

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
}

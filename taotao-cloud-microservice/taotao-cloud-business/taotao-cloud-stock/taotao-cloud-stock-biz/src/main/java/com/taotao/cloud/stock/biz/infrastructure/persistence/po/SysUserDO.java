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

package com.taotao.cloud.stock.biz.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户DO
 *
 * @author shuigedeng
 * @since 2021-02-09
 */
@Data
@TableName("sys_user")
public class SysUserDO extends BaseDO {

    /** 用户名 */
    private String userName;

    /** 帐户 */
    private String accountId;

    /** 用户类型 */
    private String userType;

    /** 关联id */
    private String linkId;

    /** 状态 */
    private String status;

    /** 备注 */
    private String remarks;

    /** 租户ID */
    private String tenantId;

    /** 租户编码 */
    @TableField(exist = false)
    private String tenantCode;

    /** 租户名称 */
    @TableField(exist = false)
    private String tenantName;

    /** 手机号 */
    @TableField(exist = false)
    private String mobile;

    /** 邮箱 */
    @TableField(exist = false)
    private String email;
}

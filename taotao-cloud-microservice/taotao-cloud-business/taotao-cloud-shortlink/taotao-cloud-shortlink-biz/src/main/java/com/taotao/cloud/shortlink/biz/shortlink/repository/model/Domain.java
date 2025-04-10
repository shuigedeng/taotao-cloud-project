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

package com.taotao.cloud.shortlink.biz.shortlink.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is Description
 *
 * @since 2022/05/03
 */

/** domain */
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "short_link.`domain`")
public class Domain implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户自己绑定的域名 */
    @TableField(value = "account_no")
    private Long accountNo;

    /**
     * 域名类型，0=系统自带, 1=用户自建
     *
     * @see com.zc.shortlink.api.enums.ShortLinkDomainTypeEnum
     */
    @TableField(value = "domain_type")
    private Boolean domainType;

    /** value */
    @TableField(value = "`value`")
    private String value;

    /** 创建时间 */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /** 逻辑删除：0=否、1=是 */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_ACCOUNT_NO = "account_no";

    public static final String COL_DOMAIN_TYPE = "domain_type";

    public static final String COL_VALUE = "value";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}

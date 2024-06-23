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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.shortlink.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 短链表 - 实体类
 *
 * @since 2022/05/03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "short_link.short_link")
public class ShortLink implements Serializable {

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 分组ID */
    @TableField(value = "group_id")
    private Long groupId;

    /** 短链标题 */
    @TableField(value = "title")
    private String title;

    /** 原始URL */
    @TableField(value = "origin_url")
    private String originUrl;

    /** 短链域名 */
    @TableField(value = "domain")
    private String domain;

    /** 短链码 */
    @TableField(value = "code")
    private String code;

    /** TODO 长链MD5的意义： 如果要判断某个长链是否存在code，通过长链创建索引，这样索引空间开销很大， 可以将长链进行md5，为md5创建索引，这样索引会小很多 */
    @TableField(value = "sign")
    private String sign;

    /** 账户编码 */
    @TableField(value = "account_no")
    private Long accountNo;

    /** 状态：0=无锁、1=锁定 */
    @TableField(value = "state")
    private Integer state;

    /** 过期时间 */
    @TableField(value = "expired")
    private LocalDate expired;

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

    public static final String COL_GROUP_ID = "group_id";

    public static final String COL_TITLE = "title";

    public static final String COL_ORIGIN_URL = "origin_url";

    public static final String COL_DOMAIN = "domain";

    public static final String COL_CODE = "code";

    public static final String COL_SIGN = "sign";

    public static final String COL_ACCOUNT_NO = "account_no";

    public static final String COL_STATE = "state";

    public static final String COL_EXPIRED = "expired";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_IS_DELETED = "is_deleted";
}

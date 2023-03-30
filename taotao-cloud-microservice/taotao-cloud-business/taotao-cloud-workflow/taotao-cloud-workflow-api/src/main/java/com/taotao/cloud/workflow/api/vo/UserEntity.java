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

package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class UserEntity {

    /** 用户主键 */
    private Long id;
    /** 账户 */
    private String account;

    /** 姓名 */
    private String realName;

    /** 快速查询 */
    private String quickQuery;

    /** 呢称 */
    private String nickName;

    /** 头像 */
    private String headIcon;

    /** 性别 */
    private Integer gender;

    /** 生日 */
    private Date birthday;

    /** 手机 */
    private String mobilePhone;

    /** 电话 */
    private String telePhone;

    /** F_Landline */
    private String landline;

    /** 邮箱 */
    private String email;

    /** 民族 */
    private String nation;

    /** 籍贯 */
    private String nativePlace;

    /** 入职日期 */
    private Date entryDate;

    /** 证件类型 */
    private String certificatesType;

    /** 证件号码 */
    private String certificatesNumber;

    /** 文化程度 */
    private String education;

    /** F_UrgentContacts */
    private String urgentContacts;

    /** 紧急电话 */
    private String urgentTelePhone;

    /** 通讯地址 */
    private String postalAddress;

    /** 自我介绍 */
    private String signature;

    /** 密码 */
    private String password;

    /** 秘钥 */
    private String secretkey;

    /** 首次登录时间 */
    private Date irstLogTime;

    /** 首次登录IP */
    private String firstLogIp;

    /** 前次登录时间 */
    private Date prevLogTime;

    /** 前次登录IP */
    private String prevLogIp;

    /** 最后登录时间 */
    private Date lastLogTime;

    /** 最后登录IP */
    private String lastLogIp;

    /** 登录成功次数 */
    private Integer logSuccessCount;

    /** 登录错误次数 */
    private Integer logErrorCount;

    /** 最后修改密码时间 */
    private Date changePasswordDate;

    /** 系统语言 */
    private String language;

    /** 系统样式 */
    private String theme;

    /** 常用菜单 */
    private String commonMenu;

    /** 是否管理员 */
    private Integer isAdministrator;

    /** 扩展属性 */
    private String propertyJson;

    /** 描述 */
    private String description;

    /** 排序码 */
    private Long sortCode;

    /** 有效标志 */
    private Integer enabledMark;

    /** 创建时间 */
    private Date creatorTime;

    /** 创建用户 */
    private String creatorUserId;

    /** 修改时间 */
    private Date lastModifyTime;

    /** 修改用户 */
    private String lastModifyUserId;

    /** 删除时间 */
    private Date deleteTime;

    /** 删除用户 */
    private String deleteUserId;

    /** 删除标志 */
    private Integer deleteMark;

    /** 主管主键 */
    private String managerId;

    /** 组织主键 */
    private String organizeId;

    /** 岗位主键 */
    private String positionId;

    /** 角色主键 */
    private String roleId;

    /** 门户主键 */
    private String portalId;

    //    /**
    //     * 锁定标志
    //     */
    //    @TableField("F_LOCKMARK")
    //    private Integer lockMark;

    /** 解锁时间 */
    private Date unlockTime;

    private String groupId;
}

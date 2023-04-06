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

package com.taotao.cloud.wechat.biz.niefy.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.common.validator.group.AddGroup;
import com.github.niefy.common.validator.group.UpdateGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId
    private Long userId;

    /** 用户名 */
    @NotBlank(
            message = "用户名不能为空",
            groups = {AddGroup.class, UpdateGroup.class})
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    private String password;

    /** 盐 */
    private String salt;

    /** 邮箱 */
    @NotBlank(
            message = "邮箱不能为空",
            groups = {AddGroup.class, UpdateGroup.class})
    @Email(
            message = "邮箱格式不正确",
            groups = {AddGroup.class, UpdateGroup.class})
    private String email;

    /** 手机号 */
    private String mobile;

    /** 状态 0：禁用 1：正常 */
    private Integer status;

    /** 角色ID列表 */
    @TableField(exist = false)
    private List<Long> roleIdList;

    /** 创建者ID */
    private Long createUserId;

    /** 创建时间 */
    private Date createTime;
}

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
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_menu")
public class SysMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    @TableId
    private Long menuId;

    /** 父菜单ID，一级菜单为0 */
    private Long parentId;

    /** 父菜单名称 */
    @TableField(exist = false)
    private String parentName;

    /** 菜单名称 */
    private String name;

    /** 菜单URL */
    private String url;

    /** 授权(多个用逗号分隔，如：user:list,user:create) */
    private String perms;

    /** 类型 0：目录 1：菜单 2：按钮 */
    private Integer type;

    /** 菜单图标 */
    private String icon;

    /** 排序 */
    private Integer orderNum;

    /** ztree属性 */
    @TableField(exist = false)
    private Boolean open;

    @TableField(exist = false)
    private List<?> list;
}

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

package com.taotao.cloud.sys.api.dubbo.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:27:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuartzJobQueryRpcRequest implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    /** id */
    private Long id;
    /** 菜单名称 */
    private String name;
    /** 菜单类型 1：目录 2：菜单 3：按钮 */
    private int type;
    /** 权限标识 */
    private String perms;
    /** 前端path / 即跳转路由 */
    private String path;
    /** 菜单组件 */
    private String component;
    /** 父菜单ID */
    private long parentId;
    /** 图标 */
    private String icon;
    /** 是否缓存页面: 0:否 1:是 (默认值0) */
    private boolean keepAlive;
    /** 是否隐藏路由菜单: 0否,1是（默认值0） */
    private boolean hidden;
    /** 聚合路由 0否,1是（默认值0） */
    private boolean alwaysShow;
    /** 重定向 */
    private String redirect;
    /** 是否为外链 0否,1是（默认值0） */
    private boolean isFrame;
    /** 排序值 */
    private int sortNum;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后修改时间 */
    private LocalDateTime lastModifiedTime;
}

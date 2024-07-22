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

package com.taotao.cloud.sys.api.feign.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;

/**
 * 菜单查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:27:42
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "菜单查询对象")
public class MenuQueryApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单类型 1：目录 2：菜单 3：按钮")
    private Integer type;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "前端path / 即跳转路由")
    private String path;

    @Schema(description = "菜单组件")
    private String component;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否缓存页面: 0:否 1:是 (默认值0)")
    private Boolean keepAlive;

    @Schema(description = "是否隐藏路由菜单: 0否;1是（默认值0）")
    private Boolean hidden;

    @Schema(description = "聚合路由 0否;1是（默认值0）")
    private Boolean alwaysShow;

    @Schema(description = "重定向")
    private String redirect;

    @Schema(description = "是否为外链 0否;1是（默认值0）")
    private Boolean isFrame;

    @Schema(description = "排序值")
    private Integer sortNum;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;
}

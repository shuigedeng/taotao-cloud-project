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
import java.util.Set;
import lombok.*;

/**
 * 用户查询VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:19:37
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户查询VO")
public class UserQueryApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 5126530068827085130L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "真实用户名")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别 1男 2女 0未知")
    private Integer sex;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "岗位ID")
    private Long jobId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "是否锁定 1-正常，2-锁定")
    private Integer lockFlag;

    @Schema(description = "角色列表")
    private Set<String> roles;

    @Schema(description = "权限列表")
    private Set<String> permissions;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;
}

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

package com.taotao.cloud.sys.biz.model.bo;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:19:37
 */
@RecordBuilder
public record UserBO(
        /** id */
        Long id,
        /** 昵称 */
        String nickname,
        /** 真实用户名 */
        String username,
        /** 手机号 */
        String phone,
        /** 用户类型 1前端用户 2商户用户 3后台管理用户 */
        Integer type,
        /** 性别 1男 2女 0未知 */
        Integer sex,
        /** 邮箱 */
        String email,
        /** 部门ID */
        Long deptId,
        /** 岗位ID */
        Long jobId,
        /** 头像 */
        String avatar,
        /** 是否锁定 1-正常，2-锁定 */
        Integer lockFlag,
        /** 角色列表 */
        Set<String> roles,
        /** 权限列表 */
        Set<String> permissions,
        /** 创建时间 */
        LocalDateTime createTime,
        /** 最后修改时间 */
        LocalDateTime lastModifiedTime)
        implements Serializable {

    public static final long serialVersionUID = 5126530068827085130L;
}

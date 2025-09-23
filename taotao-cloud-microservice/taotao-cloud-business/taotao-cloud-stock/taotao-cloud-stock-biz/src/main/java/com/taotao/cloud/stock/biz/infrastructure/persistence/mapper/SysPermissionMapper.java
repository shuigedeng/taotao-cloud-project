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

package com.taotao.cloud.stock.biz.infrastructure.persistence.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 权限Mapper
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
@Mapper
public interface SysPermissionMapper extends MpSuperMapper<SysPermissionDO> {

    /**
     * 查询权限
     *
     * @param params
     * @return
     */
    List<SysPermissionDO> queryList(@Param("params") Map<String, Object> params);

    /**
     * 查询角色的所有权限
     *
     * @param roleId 角色ID
     */
    List<SysPermissionDO> queryPermissionByRoleId(String roleId);

    /**
     * 查询管理员权限
     *
     * @return
     */
    List<SysPermissionDO> queryPermissionByRoleCode(String roleCode);

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    List<SysPermissionDO> queryPermissionByUserId(String userId);
}

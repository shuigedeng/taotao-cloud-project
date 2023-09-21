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

package com.taotao.cloud.stock.biz.domain.permission.repository;

import com.taotao.cloud.stock.biz.domain.model.permission.Permission;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionId;
import com.taotao.cloud.stock.biz.domain.model.permission.PermissionName;
import com.taotao.cloud.stock.biz.domain.permission.model.entity.Permission;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionId;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionName;
import java.util.List;
import java.util.Map;

/**
 * 权限-Repository接口
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
public interface PermissionRepository {

    /**
     * 查找菜单
     *
     * @param params
     * @return
     */
    List<com.taotao.cloud.stock.biz.domain.model.permission.Permission> queryList(Map<String, Object> params);

    /**
     * 角色编码获取权限
     *
     * @param rolecode
     * @return
     */
    List<com.taotao.cloud.stock.biz.domain.model.permission.Permission> queryList(RoleCode rolecode);

    /**
     * 获取权限
     *
     * @param permissionId
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.permission.Permission find(
            com.taotao.cloud.stock.biz.domain.model.permission.PermissionId permissionId);

    /**
     * 获取权限
     *
     * @param permissionName
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.permission.Permission find(PermissionName permissionName);

    /**
     * 保存
     *
     * @param permission
     */
    com.taotao.cloud.stock.biz.domain.model.permission.PermissionId store(Permission permission);

    /**
     * 删除
     *
     * @param permissionId
     */
    void remove(PermissionId permissionId);
}

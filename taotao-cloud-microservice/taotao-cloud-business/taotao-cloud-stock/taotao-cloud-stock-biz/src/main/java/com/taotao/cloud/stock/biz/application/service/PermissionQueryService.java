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

package com.taotao.cloud.stock.biz.application.service;

import java.util.List;
import java.util.Set;

/**
 * 权限查询服务接口
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
public interface PermissionQueryService {

    /**
     * 所有权限
     *
     * @return
     */
    List<PermissionDTO> listAllPermission();

    /**
     * 所有菜单（不保存按钮）
     *
     * @return
     */
    List<PermissionDTO> listAllMenu();

    /**
     * 通过ID获取
     *
     * @param id
     * @return
     */
    PermissionDTO getById(String id);

    /**
     * 获取权限树
     *
     * @param userId
     * @return
     */
    List<PermissionDTO> getUserMenuTree(String userId);

    /**
     * 获取权限编码
     *
     * @param userId
     * @return
     */
    Set<String> getPermissionCodes(String userId);

    /**
     * 获取权限id
     *
     * @param userId
     * @return
     */
    Set<String> getPermissionIds(String userId);
}

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

package com.taotao.cloud.stock.biz.domain.permission.model.specification;

import com.taotao.cloud.stock.api.common.domain.AbstractSpecification;
import com.taotao.cloud.stock.biz.domain.permission.model.vo.PermissionId;
import com.taotao.cloud.stock.biz.domain.permission.repository.PermissionRepository;

/**
 * 权限删除Specification
 *
 * @author shuigedeng
 * @since 2021-02-20
 */
public class PermissionDeleteSpecification extends AbstractSpecification<PermissionId> {

    private PermissionRepository permissionRepository;

    public PermissionDeleteSpecification(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean isSatisfiedBy(PermissionId permissionId) {
        Permission permission = permissionRepository.find(permissionId);
        if (permission.hasSub()) {
            throw new RuntimeException("请先删除子菜单或按钮");
        }
        return true;
    }
}

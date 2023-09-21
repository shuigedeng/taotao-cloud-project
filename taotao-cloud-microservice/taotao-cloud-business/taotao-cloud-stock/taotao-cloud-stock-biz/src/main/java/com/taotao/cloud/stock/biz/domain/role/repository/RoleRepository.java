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

package com.taotao.cloud.stock.biz.domain.role.repository;

import com.taotao.cloud.stock.biz.domain.model.role.Role;
import com.taotao.cloud.stock.biz.domain.model.role.RoleCode;
import com.taotao.cloud.stock.biz.domain.model.role.RoleId;
import com.taotao.cloud.stock.biz.domain.model.role.RoleName;
import com.taotao.cloud.stock.biz.domain.role.model.entity.Role;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleCode;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleId;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleName;
import java.util.List;

/**
 * 角色-Repository接口
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
public interface RoleRepository {

    /**
     * 获取角色
     *
     * @param roleId
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.role.Role find(com.taotao.cloud.stock.biz.domain.model.role.RoleId roleId);

    /**
     * 获取角色
     *
     * @param roleName
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.role.Role find(RoleName roleName);

    /**
     * 获取角色
     *
     * @param roleCode
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.role.Role find(RoleCode roleCode);

    /**
     * 保存
     *
     * @param role
     */
    com.taotao.cloud.stock.biz.domain.model.role.RoleId store(Role role);

    /**
     * 删除
     *
     * @param roleIds
     */
    void remove(List<RoleId> roleIds);
}

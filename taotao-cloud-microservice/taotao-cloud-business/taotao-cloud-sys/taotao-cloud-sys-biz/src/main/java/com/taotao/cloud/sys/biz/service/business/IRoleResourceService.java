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

package com.taotao.cloud.sys.biz.service.business;

import com.taotao.cloud.sys.biz.model.entity.system.RoleResource;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.Set;

/**
 * 角色-菜单服务类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:01:39
 */
public interface IRoleResourceService extends BaseSuperService<RoleResource, Long> {

    /**
     * 添加角色-菜单对应关系
     *
     * @param roleId 角色id
     * @param menuIds 菜单id列表
     * @return 是否成功
     * @since 2020/10/21 09:20
     */
    Boolean saveRoleMenu(Long roleId, Set<Long> menuIds);
}

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

import com.taotao.cloud.sys.biz.model.entity.system.UserRelation;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.Set;

/**
 * 用户-角色服务类
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:33:42
 */
public interface IUserRelationService extends BaseSuperService<UserRelation, Long> {

    /**
     * 添加用户-角色对应关系
     *
     * @param userId 用户id
     * @param roleIds 角色id列表
     * @return 修改结果
     */
    Boolean saveUserRoles(Long userId, Set<Long> roleIds);
}

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
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联Mapper
 *
 * @author shuigedeng
 * @since 2021-02-14
 */
@Mapper
public interface SysUserRoleMapper extends MpSuperMapper<SysUserRoleDO> {

    /** 根据角色ID，批量删除 */
    int deleteByRoleIds(List<String> roleIds);

    /** 根据用户ID，批量删除 */
    int deleteByUserIds(List<String> userIds);
}

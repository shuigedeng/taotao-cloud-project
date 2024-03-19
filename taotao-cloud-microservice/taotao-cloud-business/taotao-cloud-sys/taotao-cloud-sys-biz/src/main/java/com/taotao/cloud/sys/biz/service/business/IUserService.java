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

import com.taotao.cloud.sys.biz.model.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.web.base.service.BaseSuperService;

import java.util.List;
import java.util.Set;

/**
 * ISysUserService
 *
 * @author shuigedeng
 * @version 2023.01
 * @since 2023-02-15 14:43:45
 */
public interface IUserService extends BaseSuperService<User, Long> {

    /**
     * 保存用户
     *
     * @param user 用户
     * @return {@link User }
     * @since 2023-02-15 14:43:47
     */
    User saveUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户
     * @return {@link User }
     * @since 2023-02-15 14:43:50
     */
    User updateUser(User user);

    /**
     * 重置密码
     *
     * @param restPasswordDTO 重置密码对象
     * @return 重置结果
     * @since 2021-10-09 20:49:02
     */
    Boolean restPass(Long userId, RestPasswordUserDTO restPasswordDTO);

    /**
     * 更新用户角色信息
     *
     * @param userId userId
     * @param roleIds roleIds
     * @return 更新结果
     * @since 2021-10-09 20:49:19
     */
    Boolean updateUserRoles(Long userId, Set<Long> roleIds);

    /**
     * 根据手机号码查询用户是否存在
     *
     * @param phone phone
     * @return 是否存在
     * @since 2021-10-09 20:49:35
     */
    Boolean existsByPhone(String phone);

    /**
     * 根据用户id查询用户是否存在
     *
     * @param id id
     * @return 是否存在
     * @since 2021-10-09 20:49:40
     */
    Boolean existsById(Long id);

    User registe(User user);

    List<User> getPersonList(String phoneVal);

}

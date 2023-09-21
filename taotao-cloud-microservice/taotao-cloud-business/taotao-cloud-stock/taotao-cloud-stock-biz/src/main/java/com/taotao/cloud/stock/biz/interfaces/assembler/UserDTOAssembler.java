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

package com.taotao.cloud.stock.biz.interfaces.assembler;

import com.taotao.cloud.stock.api.model.dto.UserDTO;
import com.taotao.cloud.stock.biz.domain.role.model.vo.RoleId;
import com.taotao.cloud.stock.biz.domain.user.model.vo.UserId;
import com.taotao.cloud.stock.biz.interfaces.command.UserCommand;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户Assembler
 *
 * @author shuigedeng
 * @since 2021-02-23
 */
public class UserDTOAssembler {

    public static User toUser(final UserCommand userCommand) {
        List<String> roleIds = userCommand.getRoleIdList();
        List<RoleId> roleIdList = null;
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIdList = new ArrayList<>();
            for (String roleId : roleIds) {
                roleIdList.add(new RoleId(roleId));
            }
        }
        UserName userName = null;
        if (userCommand.getUserName() != null) {
            userName = new UserName(userCommand.getUserName());
        }
        return new User(new UserId(userCommand.getId()), userName, StatusEnum.ENABLE, null, null, roleIdList);
    }

    public static UserDTO fromUser(final User user) {
        List<String> roleIdList = new ArrayList<>();
        if (user.getRoleIds() != null) {
            user.getRoleIds().forEach(roleId -> {
                roleIdList.add(roleId.getId());
            });
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserId() == null ? null : user.getUserId().getId());
        userDTO.setUserName(
                user.getUserName() == null ? null : user.getUserName().getName());
        userDTO.setEmail(
                user.getAccount().getEmail() == null
                        ? null
                        : user.getAccount().getEmail().getEmail());
        userDTO.setMobile(
                user.getAccount().getMobile() == null
                        ? null
                        : user.getAccount().getMobile().getMobile());
        userDTO.setRoleIdList(roleIdList);
        userDTO.setStatus(user.getStatus() == null ? null : user.getStatus().getValue());
        return userDTO;
    }
}

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

package com.taotao.cloud.stock.biz.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 用户-Repository实现类
 *
 * @author shuigedeng
 * @since 2021-02-02
 */
@Repository
public class UserRepositoryImpl extends ServiceImpl<SysUserMapper, SysUserDO>
        implements UserRepository, IService<SysUserDO> {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Override
    public User find(UserId userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId.getId());
        SysUserDO sysUserDO = baseMapper.queryUser(params);
        if (sysUserDO == null) {
            return null;
        }
        User user = UserConverter.toUser(
                sysUserDO, getUserAccount(sysUserDO.getAccountId()), getUserRoleIds(sysUserDO.getId()));
        return user;
    }

    @Override
    public List<User> find(Mobile mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile.getMobile());
        List<SysUserDO> sysUserDOList = baseMapper.queryUserNoTenant(params);
        if (sysUserDOList == null || sysUserDOList.isEmpty()) {
            return null;
        }
        List<User> users = new ArrayList<>();
        for (SysUserDO sysUserDO : sysUserDOList) {
            User user = UserConverter.toUser(
                    sysUserDO, getUserAccount(sysUserDO.getAccountId()), getUserRoleIds(sysUserDO.getId()));
            users.add(user);
        }
        return users;
    }

    @Override
    public UserId store(User user) {
        SysAccountDO sysAccountDO = UserConverter.getSysAccountDO(user);
        String accountId = null;
        if (sysAccountDO != null) {
            if (sysAccountDO.getId() != null) {
                sysAccountMapper.updateById(sysAccountDO);
            } else {
                sysAccountMapper.insert(sysAccountDO);
            }
            accountId = sysAccountDO.getId();
        }
        if (TenantContext.getTenantId() != null) {
            SysUserDO sysUserDO = UserConverter.fromUser(user, accountId);
            this.saveOrUpdate(sysUserDO);
            String userId = sysUserDO.getId();
            // 先删除用户与角色关系
            List<String> userIds = new ArrayList<>();
            userIds.add(userId);
            sysUserRoleMapper.deleteByUserIds(userIds);
            List<RoleId> roleIds = user.getRoleIds();
            if (roleIds != null && !roleIds.isEmpty()) {
                // 保存角色与菜单关系
                for (RoleId roleId : roleIds) {
                    SysUserRoleDO sysUserRoleDO = new SysUserRoleDO();
                    sysUserRoleDO.setUserId(userId);
                    sysUserRoleDO.setRoleId(roleId.getId());
                    sysUserRoleMapper.insert(sysUserRoleDO);
                }
            }
            return new UserId(sysUserDO.getId());
        } else {
            return null;
        }
    }

    @Override
    public void remove(List<UserId> userIds) {
        List<String> ids = new ArrayList<>();
        userIds.forEach(userId -> {
            ids.add(userId.getId());
        });
        this.removeByIds(ids);
        sysUserRoleMapper.deleteByUserIds(ids);
    }

    /**
     * 添加账号
     *
     * @param accountId
     */
    private Account getUserAccount(String accountId) {
        SysAccountDO sysAccountDO = sysAccountMapper.selectById(accountId);
        if (sysAccountDO == null) {
            return null;
        }
        Account account = UserConverter.toAccount(sysAccountDO);
        return account;
    }

    /**
     * 获取管理角色Id
     *
     * @param userId
     */
    private List<RoleId> getUserRoleIds(String userId) {
        List<RoleId> roleIdList = null;
        // 如果是超级管理员
        List<SysRoleDO> sysRoleDOList = sysRoleMapper.queryUserRole(userId);
        if (sysRoleDOList != null && !sysRoleDOList.isEmpty()) {
            roleIdList = new ArrayList<>();
            for (SysRoleDO sysRoleDO : sysRoleDOList) {
                roleIdList.add(new RoleId(sysRoleDO.getId()));
            }
        }
        return roleIdList;
    }
}

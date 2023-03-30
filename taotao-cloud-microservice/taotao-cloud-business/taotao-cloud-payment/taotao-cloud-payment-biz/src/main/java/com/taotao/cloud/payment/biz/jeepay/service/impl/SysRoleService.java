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

package com.taotao.cloud.payment.biz.jeepay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.payment.biz.jeepay.core.entity.SysRole;
import com.taotao.cloud.payment.biz.jeepay.core.entity.SysRoleEntRela;
import com.taotao.cloud.payment.biz.jeepay.core.entity.SysUserRoleRela;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import com.taotao.cloud.payment.biz.jeepay.service.mapper.SysRoleMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统角色表 服务实现类
 *
 * @author [mybatis plus generator]
 * @since 2020-06-13
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    @Autowired private SysUserRoleRelaService sysUserRoleRelaService;

    @Autowired private SysRoleEntRelaService sysRoleEntRelaService;

    /** 根据用户查询全部角色集合 * */
    public List<String> findListByUser(Long sysUserId) {
        List<String> result = new ArrayList<>();
        sysUserRoleRelaService
                .list(SysUserRoleRela.gw().eq(SysUserRoleRela::getUserId, sysUserId))
                .stream()
                .forEach(r -> result.add(r.getRoleId()));

        return result;
    }

    @Transactional
    public void removeRole(String roleId) {

        if (sysUserRoleRelaService.count(
                        SysUserRoleRela.gw().eq(SysUserRoleRela::getRoleId, roleId))
                > 0) {
            throw new BizException("当前角色已分配到用户， 不可删除！");
        }

        // 删除当前表
        removeById(roleId);

        // 删除关联表
        sysRoleEntRelaService.remove(SysRoleEntRela.gw().eq(SysRoleEntRela::getRoleId, roleId));
    }
}

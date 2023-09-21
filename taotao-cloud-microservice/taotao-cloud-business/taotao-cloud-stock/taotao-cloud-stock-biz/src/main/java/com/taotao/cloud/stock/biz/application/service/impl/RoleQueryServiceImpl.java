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

package com.taotao.cloud.stock.biz.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色查询服务实现类
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
@Service
public class RoleQueryServiceImpl implements RoleQueryService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Page queryPage(Map<String, Object> params) {
        IPage<SysRoleDO> page = sysRoleMapper.queryList(new Query().getPage(params), params);
        return PageAssembler.toPage(page);
    }

    @Override
    public List<RoleDTO> listAll() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("status", StatusEnum.ENABLE.getValue());
        return RoleDTOAssembler.getRoleDTOList(sysRoleMapper.queryList(param));
    }

    @Override
    public RoleDTO getById(String id) {
        return RoleDTOAssembler.fromRole(roleRepository.find(new RoleId(id)));
    }
}

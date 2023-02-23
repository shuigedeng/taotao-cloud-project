package com.taotao.cloud.stock.biz.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色应用服务实现类
 *
 * @author shuigedeng
 * @date 2021-02-18
 */
@Service
public class RoleApplicationServiceImpl implements RoleApplicationService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RoleCommand roleCommand) {
        Role role = RoleDTOAssembler.toRole(roleCommand);
        RoleCreateSpecification roleCreateSpecification = new RoleCreateSpecification(roleRepository);
        roleCreateSpecification.isSatisfiedBy(role);
        roleRepository.store(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        RoleUpdateSpecification roleUpdateSpecification = new RoleUpdateSpecification();
        List<RoleId> roleIds = new ArrayList<>();
        ids.forEach(id -> {
            Role role = roleRepository.find(new RoleId(id));
            roleUpdateSpecification.isSatisfiedBy(role);
            roleIds.add(new RoleId(id));
        });
        roleRepository.remove(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        Role role = roleRepository.find(new RoleId(id));
        RoleUpdateSpecification roleUpdateSpecification = new RoleUpdateSpecification();
        roleUpdateSpecification.isSatisfiedBy(role);
        role.disable();
        roleRepository.store(role);
    }
}

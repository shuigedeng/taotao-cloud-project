package com.taotao.cloud.uc.biz.service.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.core.utils.BeanUtil;
import com.taotao.cloud.uc.api.dto.role.RoleDTO;
import com.taotao.cloud.uc.api.dto.role.RoleResourceDTO;
import com.taotao.cloud.uc.api.query.role.RolePageQuery;
import com.taotao.cloud.uc.biz.entity.QSysRole;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.repository.SysRoleRepository;
import com.taotao.cloud.uc.biz.service.ISysRoleResourceService;
import com.taotao.cloud.uc.biz.service.ISysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 角色表服务实现类
 *
 * @author dengtao
 * @date 2020-10-16 16:23:05
 * @since 1.0
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl implements ISysRoleService {
    private final SysRoleRepository roleRepository;
    private final ISysRoleResourceService sysRoleResourceService;

    private final static QSysRole SYS_ROLE = QSysRole.sysRole;

    @Override
    public SysRole findRoleById(Long id) {
        Optional<SysRole> optionalSysRole = roleRepository.findById(id);
        return optionalSysRole.orElseThrow(() -> new BusinessException(ResultEnum.ROLE_NOT_EXIST));
    }

    @Override
    public List<SysRole> findRoleByUserIds(Set<Long> userIds) {
        return roleRepository.findRoleByUserIds(userIds);
    }

    @Override
    public Boolean existRoleByCode(String code) {
		BooleanExpression predicate = SYS_ROLE.code.eq(code);
		return roleRepository.exists(predicate);
    }

    @Override
    public SysRole findRoleByCode(String code) {
        BooleanExpression predicate = SYS_ROLE.delFlag.eq(false).and(SYS_ROLE.code.eq(code));
        return roleRepository.fetchOne(predicate);
    }

    @Override
    public Boolean saveRole(RoleDTO roleDTO) {
        SysRole role = SysRole.builder().build();
        BeanUtil.copyIgnoredNull(roleDTO, role);
        roleRepository.saveAndFlush(role);
        return true;
    }

    @Override
    public Boolean updateRole(Long id, RoleDTO roleDTO) {
        SysRole role = findRoleById(id);
        BeanUtil.copyIgnoredNull(roleDTO, role);
        roleRepository.saveAndFlush(role);
        return true;
    }

    @Override
    public Boolean deleteRole(Long id) {
        roleRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<SysRole> findRolePage(Pageable pageable, RolePageQuery roleQuery) {
        BooleanExpression expression = SYS_ROLE.delFlag.eq(false);
        OrderSpecifier<LocalDateTime> desc = SYS_ROLE.createTime.desc();
        return roleRepository.findAll(expression, pageable, desc);
    }

    @Override
    public List<SysRole> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleResources(RoleResourceDTO roleResourceDTO) {
        Long roleId = roleResourceDTO.getRoleId();
        findRoleById(roleId);
        return sysRoleResourceService.saveRoleResource(roleId, roleResourceDTO.getResourceIds());
    }

    @Override
    public List<SysRole> findRoleByCodes(Set<String> codes) {
        return roleRepository.findRoleByCodes(codes);
    }

}

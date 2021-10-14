/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.service.ISysRoleService;
import com.taotao.cloud.uc.biz.entity.SysRole;
import com.taotao.cloud.uc.biz.mapper.SysRoleMapper;
import com.taotao.cloud.uc.biz.repository.SysRoleRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

/**
 * SysRoleServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:46:26
 */
@Service
public class SysRoleServiceImpl extends
	BaseSuperServiceImpl<SysRoleMapper, SysRole, SysRoleRepository, Long>
	implements ISysRoleService<SysRole, Long> {
	//
	//private final SysRoleRepository roleRepository;
	//private final ISysRoleResourceService sysRoleResourceService;
	//
	//public SysRoleServiceImpl(SysRoleRepository roleRepository,
	//	ISysRoleResourceService sysRoleResourceService) {
	//	this.roleRepository = roleRepository;
	//	this.sysRoleResourceService = sysRoleResourceService;
	//}
	//
	//private final static QSysRole SYS_ROLE = QSysRole.sysRole;
	//
	//@Override
	//public SysRole findRoleById(Long id) {
	//	Optional<SysRole> optionalSysRole = roleRepository.findById(id);
	//	return optionalSysRole.orElseThrow(() -> new BusinessException(ResultEnum.ROLE_NOT_EXIST));
	//}
	//
	//@Override
	//public List<SysRole> findRoleByUserIds(Set<Long> userIds) {
	//	return roleRepository.findRoleByUserIds(userIds);
	//}
	//
	//@Override
	//public Boolean existRoleByCode(String code) {
	//	BooleanExpression predicate = SYS_ROLE.code.eq(code);
	//	return roleRepository.exists(predicate);
	//}
	//
	//@Override
	//public SysRole findRoleByCode(String code) {
	//	BooleanExpression predicate = SYS_ROLE.delFlag.eq(false).and(SYS_ROLE.code.eq(code));
	//	return roleRepository.fetchOne(predicate);
	//}
	//
	//@Override
	//public Boolean saveRole(RoleDTO roleDTO) {
	//	SysRole role = SysRole.builder().build();
	//	BeanUtil.copyIgnoredNull(roleDTO, role);
	//	roleRepository.saveAndFlush(role);
	//	return true;
	//}
	//
	//@Override
	//public Boolean updateRole(Long id, RoleDTO roleDTO) {
	//	SysRole role = findRoleById(id);
	//	BeanUtil.copyIgnoredNull(roleDTO, role);
	//	roleRepository.saveAndFlush(role);
	//	return true;
	//}
	//
	//@Override
	//public Boolean deleteRole(Long id) {
	//	roleRepository.deleteById(id);
	//	return true;
	//}
	//
	//@Override
	//public Page<SysRole> findRolePage(Pageable pageable, RoleQuery roleQuery) {
	//	BooleanExpression expression = SYS_ROLE.delFlag.eq(false);
	//	OrderSpecifier<LocalDateTime> desc = SYS_ROLE.createTime.desc();
	//	return roleRepository.findPageable(expression, pageable, desc);
	//}
	//
	//@Override
	//public List<SysRole> findAllRoles() {
	//	return roleRepository.findAll();
	//}
	//
	//@Override
	//@Transactional(rollbackFor = Exception.class)
	//public Boolean saveRoleResources(RoleResourceDTO roleResourceDTO) {
	//	Long roleId = roleResourceDTO.getRoleId();
	//	findRoleById(roleId);
	//	return sysRoleResourceService.saveRoleResource(roleId, roleResourceDTO.getResourceIds());
	//}
	//
	//@Override
	//public List<SysRole> findRoleByCodes(Set<String> codes) {
	//	return roleRepository.findRoleByCodes(codes);
	//}

}

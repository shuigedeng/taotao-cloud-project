/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.system.api.dto.RoleDto;
import com.taotao.cloud.system.api.dto.RoleQueryCriteria;
import com.taotao.cloud.system.api.dto.RoleSmallDto;
import com.taotao.cloud.system.api.dto.UserDto;
import com.taotao.cloud.system.biz.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
public interface RoleService extends IService<Role> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(RoleQueryCriteria criteria, Pageable pageable);


    /**
     * 查询数据分页
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Object queryAlls(RoleQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<RoleDto>
     */
    List<Role> queryAll(RoleQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RoleDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<Role> roles);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    RoleDto findById(long id);

    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDto /
     */
    void updateMenu(Role resources, RoleDto roleDto);

    /**
     * 创建
     * @param resources /
     * @return /
     */
    RoleDto create(Role resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Role resources);

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    Collection<SimpleGrantedAuthority> mapToGrantedAuthorities(UserDto user);

    void delete(Set<Long> ids);
}

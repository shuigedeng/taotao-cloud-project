package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysRoleDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysRoleQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysRole;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 角色Service接口
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param query 查询条件
     * @return 角色分页列表
     */
    IPage<SysRole> selectRolePage(SysRoleQuery query);

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    SysRole selectRoleById(Long id);

    /**
     * 新增角色
     *
     * @param dto 角色信息
     * @return 是否成功
     */
    boolean insertRole(SysRoleDTO dto);

    /**
     * 修改角色
     *
     * @param dto 角色信息
     * @return 是否成功
     */
    boolean updateRole(SysRoleDTO dto);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteRoleById(Long id);

    /**
     * 批量删除角色
     *
     * @param ids 角色ID数组
     * @return 是否成功
     */
    boolean deleteRoleByIds(Long[] ids);

    /**
     * 给角色绑定资源（菜单/按钮/接口）
     *
     * @param roleId 角色ID
     * @param resourceIds 资源ID数组
     * @return 是否成功
     */
    boolean bindRoleResources(Long roleId, Long[] resourceIds);

    /**
     * 解除角色资源
     *
     * @param roleId 角色ID
     * @param resourceIds 资源ID数组
     * @return 是否成功
     */
    boolean unbindRoleResources(Long roleId, Long[] resourceIds);

    /**
     * 查询角色的资源ID列表
     *
     * @param roleId 角色ID
     * @return 资源ID列表
     */
    List<Long> selectRoleResourceIds(Long roleId);

    /**
     * 查询当前用户的角色ID列表
     *
     * @return 角色ID列表
     */
    List<Long> selectCurrentUserRoleIds();
}

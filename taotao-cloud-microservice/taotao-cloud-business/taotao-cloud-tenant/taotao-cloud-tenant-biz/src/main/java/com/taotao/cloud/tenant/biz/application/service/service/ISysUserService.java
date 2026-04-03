package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysUserDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysUserQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUser;

import java.util.List;

/**
 * 用户Service接口
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     *
     * @param query 查询条件
     * @return 用户分页列表
     */
    IPage<SysUser> selectUserPage( SysUserQuery query);

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    SysUser selectUserById(Long id);

    /**
     * 新增用户
     *
     * @param dto 用户信息
     * @return 是否成功
     */
    boolean insertUser(SysUserDTO dto);

    /**
     * 修改用户
     *
     * @param dto 用户信息
     * @return 是否成功
     */
    boolean updateUser( SysUserDTO dto);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUserById(Long id);

    /**
     * 批量删除用户
     *
     * @param ids 用户ID数组
     * @return 是否成功
     */
    boolean deleteUserByIds(Long[] ids);

    /**
     * 给用户绑定角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID数组
     * @return 是否成功
     */
    boolean bindUserRoles(Long userId, Long[] roleIds);

    /**
     * 解除用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID数组
     * @return 是否成功
     */
    boolean unbindUserRoles(Long userId, Long[] roleIds);

    /**
     * 给用户绑定组织
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @param isMain 是否主组织
     * @return 是否成功
     */
    boolean bindUserOrg(Long userId, Long orgId, Integer isMain);

    /**
     * 解除用户组织
     *
     * @param userId 用户ID
     * @param orgId 组织ID
     * @return 是否成功
     */
    boolean unbindUserOrg(Long userId, Long orgId);

    /**
     * 查询用户的角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectUserRoleIds(Long userId);

    /**
     * 查询用户的组织ID列表
     *
     * @param userId 用户ID
     * @return 组织ID列表
     */
    List<Long> selectUserOrgIds(Long userId);
    
    /**
     * 批量绑定用户组织
     *
     * @param userId 用户ID
     * @param orgIds 组织ID列表
     * @param mainOrgId 主组织ID
     * @return 是否成功
     */
    boolean bindUserOrgs(Long userId, List<Long> orgIds, Long mainOrgId);
    
    /**
     * 用户解封
     *
     * @param userId 用户ID
     */
    void doUntieDisable(Long userId);

    /**
     * 重置用户密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 更新用户基本资料
     *
     * @param dto 用户信息
     * @return 是否成功
     */
    boolean updateUserProfile(SysUserDTO dto);
}

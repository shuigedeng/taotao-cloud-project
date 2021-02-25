package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.uc.api.dto.role.RoleDTO;
import com.taotao.cloud.uc.api.dto.role.RoleResourceDTO;
import com.taotao.cloud.uc.api.query.role.RolePageQuery;
import com.taotao.cloud.uc.biz.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * 角色表服务接口
 *
 * @author dengtao
 * @date 2020-10-16 16:23:05
 * @since 1.0
 */
public interface ISysRoleService {

    /**
     * 根据id获取角色信息
     *
     * @param id 角色id
     * @return com.taotao.cloud.uc.biz.entity.SysRole
     * @author dengtao
     * @date 2020/10/20 15:59
     * @since v1.0
     */
    SysRole findRoleById(Long id);

    /**
     * 根据用户id列表获取角色列表
     *
     * @param userIds 用户id列表
     * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysRole>
     * @author dengtao
     * @date 2020/10/20 16:46
     * @since v1.0
     */
    List<SysRole> findRoleByUserIds(Set<Long> userIds);

    /**
     * 根据code查询角色是否存在
     *
     * @param code code
     * @return com.taotao.cloud.uc.biz.entity.SysRole
     * @author dengtao
     * @date 2020/10/20 17:33
     * @since v1.0
     */
    Boolean existRoleByCode(String code);

    /**
     * 根据code获取角色信息
     *
     * @param code code
     * @return com.taotao.cloud.uc.biz.entity.SysRole
     * @author dengtao
     * @date 2020/10/20 17:33
     * @since v1.0
     */
    SysRole findRoleByCode(String code);

    /**
     * 添加角色
     *
     * @param roleDTO roleDTO
     * @return java.lang.Boolean
     * @author dengtao
     * @date 2020/10/20 17:45
     * @since v1.0
     */
    Boolean saveRole(RoleDTO roleDTO);

    /**
     * 修改角色
     *
     * @param id
     * @param roleDTO
     * @return java.lang.Boolean
     * @author dengtao
     * @date 2020/10/21 09:06
     * @since v1.0
     */
    Boolean updateRole(Long id, RoleDTO roleDTO);

    /**
     * 根据id删除角色
     *
     * @param id
     * @return java.lang.Boolean
     * @author dengtao
     * @date 2020/10/21 09:07
     * @since v1.0
     */
    Boolean deleteRole(Long id);

    /**
     * 分页查询角色集合
     *
     * @param pageable
     * @param roleQuery
     * @return org.springframework.data.domain.Page<com.taotao.cloud.uc.biz.entity.SysRole>
     * @author dengtao
     * @date 2020/10/21 09:09
     * @since v1.0
     */
    Page<SysRole> findRolePage(Pageable pageable, RolePageQuery roleQuery);

    /**
     * 查询所有角色列表
     *
     * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysRole>
     * @author dengtao
     * @date 2020/10/21 09:12
     * @since v1.0
     */
    List<SysRole> findAllRoles();

    /**
     * 根据角色id更新资源信息(角色分配资源)
     *
     * @param roleResourceDTO
     * @return java.lang.Boolean
     * @author dengtao
     * @date 2020/10/21 09:45
     * @since v1.0
     */
    Boolean saveRoleResources(RoleResourceDTO roleResourceDTO);

    /**
     * 根据code列表获取角色信息
     *
     * @param codes
     * @return java.util.List<com.taotao.cloud.uc.biz.entity.SysRole>
     * @author dengtao
     * @date 2020/10/21 10:36
     * @since v1.0
     */
    List<SysRole> findRoleByCodes(Set<String> codes);
}

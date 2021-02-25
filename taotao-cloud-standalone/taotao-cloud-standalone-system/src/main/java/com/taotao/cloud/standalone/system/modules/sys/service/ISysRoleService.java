package com.taotao.cloud.standalone.system.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysMenu;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysRole;
import com.taotao.cloud.standalone.system.modules.sys.dto.RoleDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 保存角色和菜单
     * @param roleDto
     * @return
     */
    boolean saveRoleMenu(RoleDTO roleDto);

    /**
     * 更新角色和菜单
     * @param roleDto
     * @return
     */
    boolean updateRoleMenu(RoleDTO roleDto);

    /**
     * 根据主键删除角色
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 获取角色列表
     * @return
     */
    List<SysRole> selectRoleList(String roleName);

    /**
     * 根据角色id获取菜单
     * @param roleId
     * @return
     */
    List<SysMenu> findMenuListByRoleId(int roleId);

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> findRolesByUserId(Integer userId);
}

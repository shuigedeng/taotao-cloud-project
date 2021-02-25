// package com.taotao.cloud.uc.biz.service;
//
// import com.taotao.cloud.uc.api.dto.RoleDTO;
// import com.taotao.cloud.uc.biz.entity.SysRole;
//
// import java.io.Serializable;
// import java.util.List;
//
//
// /**
//  * 系统角色表 服务类
//  *
//  * @author taotao
//  * @date 2020-05-14 14:36:39
//  */
// public interface ISysRoleService {
//
//     /**
//      * 保存角色和菜单
//      *
//      * @param roleDto
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:47
//      */
//     boolean saveRole(RoleDTO roleDto);
//
//     /**
//      * 更新角色和菜单
//      *
//      * @param roleDto
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:47
//      */
//     boolean updateRole(RoleDTO roleDto);
//
//     /**
//      * 获取角色列表
//      *
//      * @param roleName
//      * @return java.util.List<com.taotao.cloud.uc.api.entity.SysRole>
//      * @author dengtao
//      * @date 2020/4/30 11:48
//      */
//     List<SysRole> selectRoleList(String roleName);
//
//     /**
//      * 通过用户ID，查询角色信息
//      *
//      * @param userId
//      * @return java.util.List<com.taotao.cloud.uc.api.entity.SysRole>
//      * @author dengtao
//      * @date 2020/4/30 11:48
//      */
//     List<SysRole> findRolesByUserId(Integer userId);
//
//
//     /**
//      * 批量删除角色
//      *
//      * @param ids
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:48
//      */
//     boolean batchDeleteRoleByIds(List<Integer> ids);
//
//     /**
//      * 功能描述
//      *
//      * @param roleDto
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:48
//      */
//     boolean updateRolePermission(RoleDTO roleDto);
// }

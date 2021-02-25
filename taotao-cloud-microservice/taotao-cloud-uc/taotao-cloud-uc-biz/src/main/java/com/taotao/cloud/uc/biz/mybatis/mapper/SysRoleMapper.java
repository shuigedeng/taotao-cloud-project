package com.taotao.cloud.uc.biz.mybatis.mapper;// package com.taotao.cloud.uc.biz.mapper;
//
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperMapper;
// import com.taotao.cloud.uc.biz.entity.SysMenu;
// import com.taotao.cloud.uc.biz.entity.SysRole;
// import org.apache.ibatis.annotations.Insert;
// import org.apache.ibatis.annotations.Options;
// import org.apache.ibatis.annotations.Select;
// import org.springframework.stereotype.Repository;
//
// import java.util.List;
//
//
// /**
//  * 系统角色表 Mapper 接口
//  *
//  * @author taotao
//  * @date 2020-05-14 14:36:39
//  */
// @Repository
// public interface SysRoleMapper extends SuperMapper<SysRole> {
//
//     @Insert("INSERT into sys_role (role_name,role_code,role_desc,ds_type,ds_scope) " +
//             "VALUES (#{roleName}, #{roleCode},#{roleDesc},#{dsType},#{dsScope})")
//     @Options(useGeneratedKeys = true, keyProperty = "roleId", keyColumn = "role_id")
//     Boolean insertRole(SysRole sysRole);
//
//     @Select("SELECT m.menu_id," +
//             "       m.name,m.type," +
//             "       m.parent_id," +
//             "       m.sort," +
//             "       m.perms " +
//             "FROM sys_menu m, sys_role_menu rm " +
//             "WHERE rm.role_id = #{roleId} and m.menu_id = rm.menu_id")
//     List<SysMenu> findMenuListByRoleId(int roleId);
//
//     @Select("SELECT r.* " +
//             "FROM sys_role r, sys_user_role ur " +
//             "WHERE r.role_id = ur.role_id AND r.del_flag = 0 and  ur.user_id IN (#{userId})")
//     List<SysRole> listRolesByUserId(Integer userId);
// }

package com.taotao.cloud.uc.biz.mybatis.mapper;// package com.taotao.cloud.uc.biz.mapper;
//
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperMapper;
// import com.taotao.cloud.uc.biz.entity.SysMenu;
// import org.apache.ibatis.annotations.Param;
// import org.apache.ibatis.annotations.Select;
//
// import java.util.List;
//
// /**
//  * 菜单权限表 Mapper 接口
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:43
//  */
// public interface SysMenuMapper extends SuperMapper<SysMenu> {
//
//     @Select("SELECT m.perms " +
//             "FROM sys_menu m, sys_user u, sys_user_role ur, sys_role_menu rm " +
//             "WHERE u.id = #{userId} and u.id = ur.user_id and ur.role_id = rm.role_id and rm.menu_id = m.id")
//     List<String> findPermsByUserId(@Param(value = "userId") Long userId);
//
// }

package com.taotao.cloud.uc.biz.mybatis.mapper;// package com.taotao.cloud.uc.biz.mapper;
//
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperMapper;
// import com.taotao.cloud.uc.biz.entity.SysRoleMenu;
// import org.apache.ibatis.annotations.Select;
//
// import java.util.List;
//
//
// /**
//  * 角色菜单表 Mapper 接口
//  *
//  * @author taotao
//  * @date 2020-05-14 14:36:39
//  */
// public interface SysRoleMenuMapper extends SuperMapper<SysRoleMenu> {
//
//     /**
//      * 根据userId获取菜单id
//      *
//      * @param userId
//      * @return java.util.List<java.lang.Integer>
//      * @author dengtao
//      * @date 2020/6/15 11:33
//     */
//     @Select("SELECT rm.menu_id " +
//             "FROM sys_role_menu rm,sys_user_role ur,sys_user u " +
//             "WHERE u.user_id = #{userId} AND u.user_id = ur.user_id AND rm.role_id = ur.role_id")
//     List<Integer> getMenuIdByUserId(Integer userId);
//
// }

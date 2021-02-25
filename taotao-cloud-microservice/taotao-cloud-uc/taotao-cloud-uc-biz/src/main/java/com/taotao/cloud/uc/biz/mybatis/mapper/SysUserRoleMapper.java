package com.taotao.cloud.uc.biz.mybatis.mapper;// package com.taotao.cloud.uc.biz.mapper;
//
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperMapper;
// import com.taotao.cloud.uc.biz.entity.SysUserRole;
// import org.apache.ibatis.annotations.Param;
// import org.apache.ibatis.annotations.Select;
// import org.springframework.stereotype.Repository;
//
// import java.util.List;
//
//
// /**
//  * 用户角色表 Mapper 接口
//  *
//  * @author dengtao
//  * @date 2020/4/30 13:24
//  */
// @Repository
// public interface SysUserRoleMapper extends SuperMapper<SysUserRole> {
//
//     @Override
//     int insert(SysUserRole entity);
//
//     @Select("SELECT r.role_name,ur.role_id " +
//             "FROM (sys_role r LEFT JOIN sys_user_role ur ON r.id = ur.role_id ) " +
//             "LEFT JOIN sys_user u ON u.id = ur.user_id " +
//             "WHERE u.id = #{userId}")
//     List<SysUserRole> selectUserRoleListByUserId(@Param(value = "userId") Long userId);
// }

package com.taotao.cloud.uc.biz.mybatis.mapper;// package com.taotao.cloud.uc.biz.mapper;
//
// import com.baomidou.mybatisplus.core.conditions.Wrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.core.toolkit.Constants;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.data.mybatis.plus.datascope.DataScope;
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperMapper;
// import com.taotao.cloud.uc.biz.entity.SysUser;
// import org.apache.ibatis.annotations.Param;
// import org.apache.ibatis.annotations.Select;
// import org.springframework.stereotype.Repository;
//
// /**
//  * 用户表 Mapper 接口
//  *
//  * @author dengtao
//  * @date 2020/4/30 13:24
//  */
// @Repository
// public interface SysUserMapper extends SuperMapper<SysUser> {
//
//     @Select("SELECT " +
//             "   user.id, " +
//             "   user.username, " +
//             "   user.mobile, " +
//             "   user.email, " +
//             "   user.avatar, " +
//             "   user.dept_id, " +
//             "   user.del_flag, " +
//             "   user.lock_flag, " +
//             "   dept.name AS deptName, " +
//             "   job.id AS job_id, " +
//             "   job.job_name as jobName " +
//             " FROM " +
//             "   sys_user AS user " +
//             " LEFT JOIN sys_dept AS dept ON dept.id = user.dept_id " +
//             " LEFT JOIN sys_job AS job ON job.id = user.job_id " +
//             "   ${ew.customSqlSegment}")
//     IPage<SysUser> getUserVoListPage(Page<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> wrapper, DataScope dataScope);
//
//
//     @Select("SELECT su.* FROM sys_user su LEFT JOIN sys_user_social sus ON su.user_id = sus.user_id WHERE sus.provider_id = #{providerId} AND sus.provider_user_id = #{providerUserId} and su.type = 1")
//     SysUser getUserBySocial(@Param("providerId") String providerId, @Param("providerUserId") int providerUserId);
//
// }

// package com.taotao.cloud.uc.biz.service;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.cloud.core.model.SecurityUser;
// import com.taotao.cloud.uc.api.dto.RepeatCheckDTO;
// import com.taotao.cloud.uc.api.dto.UserAddDTO;
// import com.taotao.cloud.uc.api.dto.UserUpdateDTO;
// import com.taotao.cloud.uc.api.query.UserListQuery;
// import com.taotao.cloud.uc.api.vo.UserAddVO;
// import com.taotao.cloud.uc.biz.entity.SysUser;
//
// import java.util.Set;
//
// /**
//  * 用户表 服务类
//  *
//  * @author dengtao
//  * @date 2020/4/30 13:20
//  */
// public interface ISysUserService {
//
//     /**
//      * 分页查询用户信息（含有角色信息）
//      *
//      * @param userListQuery
//      * @return com.baomidou.mybatisplus.core.metadata.IPage<com.taotao.cloud.uc.api.entity.SysUser>
//      * @author dengtao
//      * @date 2020/4/30 13:20
//      */
//     IPage<SysUser> getUsersWithRolePage(UserListQuery userListQuery);
//
//     /**
//      * 保存用户以及角色部门等信息
//      *
//      * @param userAddDto userAddDto
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:20
//      */
//     UserAddVO insertUser(UserAddDTO userAddDto);
//
//     /**
//      * 更新用户以及角色部门等信息
//      *
//      * @param updateDTO updateDTO
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:20
//      */
//     boolean updateUser(UserUpdateDTO updateDTO);
//
//     /**
//      * 删除用户信息
//      *
//      * @param userId
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:20
//      */
//     boolean removeUser(Integer userId);
//
//     /**
//      * 重置密码
//      *
//      * @param userId
//      * @param password
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:20
//      */
//     boolean restPass(Integer userId, String password);
//
//     /**
//      * 获取授权用户信息
//      *
//      * @param sysUser
//      * @return com.taotao.cloud.auth.model.SecurityUser
//      * @author dengtao
//      * @date 2020/4/30 13:21
//      */
//     SecurityUser findUserInfo(SysUser sysUser);
//
//     /**
//      * 通过用户名查找用户个人信息
//      *
//      * @param username
//      * @return com.taotao.cloud.uc.api.entity.SysUser
//      * @author dengtao
//      * @date 2020/4/30 13:21
//      */
//     SysUser findUserInByName(String username);
//
//     /**
//      * 根据用户id查询权限
//      *
//      * @param userId
//      * @return java.util.Set<java.lang.String>
//      * @author dengtao
//      * @date 2020/4/30 13:21
//      */
//     Set<String> findPermsByUserId(Long userId);
//
//     /**
//      * 通过用户id查询角色集合
//      *
//      * @param userId
//      * @return java.util.Set<java.lang.String>
//      * @author dengtao
//      * @date 2020/4/30 13:21
//      */
//     Set<String> findRoleIdByUserId(Long userId);
//
//     /**
//      * 注册用户
//      *
//      * @param userAddDTO
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:21
//      */
//     boolean register(UserAddDTO userAddDTO);
//
//     /**
//      * 修改用户信息
//      *
//      * @param sysUser
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:22
//      */
//     boolean updateUserInfo(SysUser sysUser);
//
//     /**
//      * 根据用户id / 用户名 / 手机号查询用户
//      *
//      * @param userIdOrUserNameOrMobileOrEmail
//      * @return com.taotao.cloud.uc.api.entity.SysUser
//      * @author dengtao
//      * @date 2020/4/30 13:22
//      */
//     SysUser findUserByUserIdOrUserNameOrMobile(String userIdOrUserNameOrMobileOrEmail);
//
//     /**
//      * 数据校验
//      *
//      * @param repeatCheckDTO
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 13:22
//      */
//     boolean repeatCheck(RepeatCheckDTO repeatCheckDTO);
//
//     /**
//      * 根据社交类型和社交id查询社交用户信息
//      *
//      * @param providerId
//      * @param providerUserId
//      * @return com.taotao.cloud.uc.api.entity.SysUser
//      * @author dengtao
//      * @date 2020/4/30 13:22
//      */
//     SysUser getUserBySocial(String providerId, int providerUserId);
//
//
// }

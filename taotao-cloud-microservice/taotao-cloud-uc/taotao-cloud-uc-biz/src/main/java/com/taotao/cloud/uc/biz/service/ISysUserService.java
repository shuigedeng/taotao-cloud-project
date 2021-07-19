package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.uc.api.dto.user.RestPasswordUserDTO;
import com.taotao.cloud.uc.api.dto.user.UserDTO;
import com.taotao.cloud.uc.api.dto.user.UserRoleDTO;
import com.taotao.cloud.uc.api.query.user.UserPageQuery;
import com.taotao.cloud.uc.api.query.user.UserQuery;
import com.taotao.cloud.uc.api.vo.user.UserVO;
import com.taotao.cloud.uc.biz.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户表 服务类
 *
 * @author shuigedeng
 * @since 2020/4/30 13:20
 */
public interface ISysUserService {
	/**
	 * 添加用户
	 *
	 * @param sysUser sysUser
	 * @return com.taotao.cloud.uc.biz.entity.SysUser
	 * @author shuigedeng
	 * @since 2020/11/11 15:27
	 * @version 1.0.0
	 */
	SysUser saveUser(SysUser sysUser);

	/**
	 * 更新用户
	 *
	 * @param id      id
	 * @param userDTO userDTO
	 * @author shuigedeng
	 * @since 2020/9/30 14:01
	 * @version 1.0.0
	 */
	SysUser updateUser(SysUser sysUser);

	/**
	 * 根据用户id删除用户
	 *
	 * @param id id
	 * @author shuigedeng
	 * @since 2020/9/30 14:07
	 * @version 1.0.0
	 */
	Boolean removeUser(Long id);

	/**
	 * 查询用户集合
	 *
	 * @param userQuery userListQuery
	 * @author shuigedeng
	 * @since 2020/9/30 14:10
	 * @version 1.0.0
	 */
	Page<SysUser> findUserPage(Pageable page, UserPageQuery userQuery);

	/**
	 * 重置密码
	 *
	 * @param restPasswordDTO restPasswordDTO
	 * @author shuigedeng
	 * @since 2020/9/30 14:22
	 * @version 1.0.0
	 */
	Boolean restPass(Long id, RestPasswordUserDTO restPasswordDTO);

	/**
	 * 根据用户id查询用户信息
	 *
	 * @param userId userId
	 * @author shuigedeng
	 * @since 2020/9/30 14:36
	 * @version 1.0.0
	 */
	SysUser findUserInfoById(Long userId);

	/**
	 * 查询用户集合
	 *
	 * @param userQuery userQuery
	 * @return java.util.List<com.taotao.cloud.uc.api.vo.user.SysUserVO>
	 * @author shuigedeng
	 * @since 2020/10/14 21:40
	 * @version 1.0.0
	 */
	List<SysUser> findUserList(UserQuery userQuery);

	/**
	 * 更新角色信息
	 *
	 * @param userRoleDTO
	 * @return java.lang.Boolean
	 * @author shuigedeng
	 * @since 2020/10/21 09:24
	 * @version 1.0.0
	 */
	Boolean updateUserRoles(UserRoleDTO userRoleDTO);

	/**
	 * 根据username获取用户信息
	 *
	 * @param username
	 * @return com.taotao.cloud.uc.api.vo.user.UserVO
	 * @author shuigedeng
	 * @since 2020/10/21 15:03
	 * @version 1.0.0
	 */
	SysUser findUserInfoByUsername(String username);

	/**
	 * 根据手机号码查询用户是否存在
	 *
	 * @param phone 手机
	 * @return java.lang.Boolean
	 * @author shuigedeng
	 * @since 2020/11/11 15:22
	 * @version 1.0.0
	 */
	Boolean existsByPhone(String phone);

	/**
	 * 根据用户id查询用户是否存在
	 *
	 * @param id 用户id
	 * @return java.lang.Boolean
	 * @author shuigedeng
	 * @since 2020/11/11 15:40
	 * @version 1.0.0
	 */
	Boolean existsById(Long id);
}

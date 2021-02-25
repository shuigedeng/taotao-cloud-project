package com.taotao.cloud.standalone.system.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUser;
import com.taotao.cloud.standalone.system.modules.sys.dto.UserDTO;

import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    IPage<SysUser> getUsersWithRolePage(Page page, UserDTO userDTO);

    /**
     * 保存用户以及角色部门等信息
     * @param userDto
     * @return
     */
    boolean insertUser(UserDTO userDto);

    /**
     * 更新用户以及角色部门等信息
     * @param userDto
     * @return
     */
    boolean updateUser(UserDTO userDto);

    /**
     * 删除用户信息
     * @param userId
     * @return
     */
    boolean removeUser(Integer userId);

    /**
     * 重置密码
     * @param userId
     * @return
     */
    boolean restPass(Integer userId);

    /**
     * 通过用户名查找用户个人信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findByUserInfoName(String username);

    /**
     * 根据用户id查询权限
     * @param userId
     * @return
     */
    Set<String> findPermsByUserId(Integer userId);

    /**
     * 通过用户id查询角色集合
     * @param userId
     * @return
     */
    Set<String> findRoleIdByUserId(Integer userId);

    /**
     * 账户密码登录
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);
    /**
     * 注册用户
     * @return
     */
    boolean register(UserDTO userDTO);

    /**
     * 修改用户信息
     * @param sysUser
     * @return
     */
    boolean updateUserInfo(SysUser sysUser);

    /**
     * 通过用户去查找用户(id/用户名/手机号)
     * @param sysUser
     * @return
     */
    SysUser findSecurityUserByUser(SysUser sysUser);


    /**
     * 第三方账号信息与系统账号进行绑定
     * @param sysUser
     * @return
     */
    boolean doPostSignUp(SysUser sysUser);


}

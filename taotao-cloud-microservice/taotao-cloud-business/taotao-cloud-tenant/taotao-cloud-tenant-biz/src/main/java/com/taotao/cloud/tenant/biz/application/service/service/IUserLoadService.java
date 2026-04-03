package com.taotao.cloud.tenant.biz.application.service.service;

import com.mdframe.forge.starter.core.session.LoginUser;

/**
 * 用户加载服务接口
 * 提供给认证策略使用，避免循环依赖
 */
public interface IUserLoadService {

    /**
     * 根据用户名加载用户信息（包含角色、权限、组织）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 登录用户信息
     */
    LoginUser loadUserByUsername(String username, Long tenantId);

    /**
     * 根据手机号加载用户信息
     *
     * @param phone    手机号
     * @param tenantId 租户ID
     * @return 登录用户信息
     */
    LoginUser loadUserByPhone(String phone, Long tenantId);

    /**
     * 根据邮箱加载用户信息
     *
     * @param email    邮箱
     * @param tenantId 租户ID
     * @return 登录用户信息
     */
    LoginUser loadUserByEmail(String email, Long tenantId);

    /**
     * 获取用户密码
     *
     * @param userId 用户ID
     * @return 加密后的密码
     */
    String getUserPassword(Long userId);

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean matchPassword(String rawPassword, String encodedPassword);

    /**
     * 验证验证码
     *
     * @param codeKey 验证码key
     * @param code    验证码
     * @return 是否正确
     */
    boolean validateCode(String codeKey, String code);

    /**
     * 验证手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 是否正确
     */
    boolean validatePhoneCode(String phone, String code);
}

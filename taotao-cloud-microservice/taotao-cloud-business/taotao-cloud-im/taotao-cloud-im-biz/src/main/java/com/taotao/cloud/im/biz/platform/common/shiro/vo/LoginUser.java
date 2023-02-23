package com.taotao.cloud.im.biz.platform.common.shiro.vo;

import com.platform.modules.chat.domain.ChatUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录用户身份权限
 */
@Data
@NoArgsConstructor
public class LoginUser {

    /**
     * 用户唯一标识
     */
    private String tokenId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 角色key
     */
    private String roleKey;

    /**
     * 登陆时间
     */
    private String loginTime;

    /**
     * 登录IP地址
     */
    private String ipAddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    public LoginUser(ChatUser chatUser, String roleKey, List<String> permissions) {
        this.userId = chatUser.getUserId();
        this.phone = chatUser.getPhone();
        this.roleKey = roleKey;
        this.permissions = permissions;
    }

}

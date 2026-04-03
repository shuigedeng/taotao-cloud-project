package com.taotao.cloud.tenant.biz.application.service.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOnlineUser;

import java.util.List;

/**
 * 在线用户管理服务接口
 */
public interface ISysOnlineUserService extends IService<SysOnlineUser> {

    /**
     * 添加在线用户
     *
     * @param tokenValue Token值
     * @param loginId    登录ID
     */
    void addOnlineUser(String tokenValue, Object loginId);

    /**
     * 移除在线用户
     *
     * @param tokenValue Token值
     */
    void removeOnlineUser(String tokenValue);

    /**
     * 更新最后活动时间
     *
     * @param tokenValue Token值
     */
    void updateLastActivityTime(String tokenValue);

    /**
     * 分页获取在线用户列表
     *
     * @param page     分页对象
     * @param username 用户名(可选,用于搜索)
     * @return 在线用户分页数据
     */
    IPage<SysOnlineUser> getOnlineUsersPage(Page<SysOnlineUser> page, String username);

    /**
     * 获取在线用户列表
     *
     * @param username 用户名(可选,用于搜索)
     * @return 在线用户列表
     */
    List<SysOnlineUser> getOnlineUsers(String username);

    /**
     * 根据Token获取在线用户信息
     *
     * @param tokenValue Token值
     * @return 在线用户信息
     */
    SysOnlineUser getOnlineUser(String tokenValue);

    /**
     * 强制用户下线
     *
     * @param tokenValue Token值
     */
    void kickoutUser(String tokenValue);

    /**
     * 批量强制用户下线
     *
     * @param tokenValues Token值列表
     */
    void batchKickoutUser(List<String> tokenValues);

    /**
     * 封禁用户(禁止登录指定时长)
     *
     * @param userId     用户ID
     * @param banSeconds 封禁时长(秒)
     * @param reason     封禁原因
     */
    void banUser(Long userId, long banSeconds, String reason);

    /**
     * 解封用户
     *
     * @param userId 用户ID
     */
    void unbanUser(Long userId);

    /**
     * 通知用户被踢下线
     *
     * @param tokenValue Token值
     * @param loginId    登录ID
     */
    void notifyUserKickout(String tokenValue, Object loginId);

    /**
     * 通知用户被顶下线
     *
     * @param tokenValue Token值
     * @param loginId    登录ID
     */
    void notifyUserReplaced(String tokenValue, Object loginId);

    /**
     * 通知用户被封禁
     *
     * @param loginId       登录ID
     * @param disableTime   封禁时长(秒)
     */
    void notifyUserBanned(Object loginId, long disableTime);

    /**
     * 获取用户的所有在线Token
     *
     * @param userId 用户ID
     * @return Token列表
     */
    List<String> getUserTokens(Long userId);

    /**
     * 踢出用户的所有会话(用于同一账号互踢)
     *
     * @param userId        用户ID
     * @param excludeToken  排除的Token(当前登录的Token)
     */
    void kickoutAllSessions(Long userId, String excludeToken);
}

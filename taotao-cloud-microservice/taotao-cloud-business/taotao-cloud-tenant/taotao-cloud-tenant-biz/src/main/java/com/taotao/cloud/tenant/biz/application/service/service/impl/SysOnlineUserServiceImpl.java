package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOnlineUser;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysOrg;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysUser;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysOnlineUserMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysOnlineUserService;
import com.taotao.cloud.tenant.biz.application.service.service.ISysOrgService;
import com.taotao.cloud.tenant.biz.application.service.service.ISysUserService;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.websocket.domain.WebSocketMessage;
import com.mdframe.forge.starter.websocket.enums.MessageType;
import com.mdframe.forge.starter.websocket.service.IMessagePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 在线用户管理服务实现
 * 基于数据库持久化
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOnlineUserServiceImpl extends ServiceImpl<SysOnlineUserMapper,SysOnlineUser> implements ISysOnlineUserService {

    private final SysOnlineUserMapper sysOnlineUserMapper;
    private final IMessagePushService messagePushService;
    private final ISysUserService sysUserService;
    private final ISysOrgService sysOrgService;

    @Override
    public void addOnlineUser(String tokenValue, Object loginId) {
        try {
            // 获取用户信息
            SaSession session = StpUtil.getSessionByLoginId(loginId, false);
            if (session == null) {
                log.warn("无法获取用户Session, loginId: {}", loginId);
                return;
            }
            
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(String.valueOf(loginId)));
            if (sysUser == null) {
                log.warn("Session中没有loginUser信息, loginId: {}", loginId);
                return;
            }

            // 获取请求信息
            String ipAddress = getClientIp();
            String userAgentStr = getUserAgent();
            UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
            
            String deptName = null;
            List<Long> selectUserOrgIds = sysUserService.selectUserOrgIds(sysUser.getId());
            if (CollUtil.isNotEmpty(selectUserOrgIds)) {
                SysOrg sysOrg = sysOrgService.selectOrgById(selectUserOrgIds.get(0));
                deptName = sysOrg.getOrgName();
            }
            
            // 构建在线用户信息
            SysOnlineUser sysOnlineUser = SysOnlineUser.builder()
                    .tokenValue(tokenValue)
                    .userId(sysUser.getId())
                    .username(sysUser.getUsername())
                    .realName(sysUser.getRealName())
                    .deptName(deptName)
                    .ipAddress(ipAddress)
                    .loginLocation(getLoginLocation(ipAddress))
                    .browser(userAgent.getBrowser().getName())
                    .os(userAgent.getOs().getName())
                    .loginTime(LocalDateTime.now())
                    .lastActivityTime(LocalDateTime.now())
                    .expireTime(LocalDateTime.now().plusSeconds(StpUtil.getTokenTimeout(tokenValue)))
                    .status(1) // 在线状态
                    .tenantId(sysUser.getTenantId())
                    .build();

            // 保存到数据库
            sysOnlineUserMapper.insert(sysOnlineUser);

            log.info("添加在线用户成功: userId={}, username={}, token={}",
                    sysUser.getId(), sysUser.getUsername(), tokenValue);
        } catch (Exception e) {
            log.error("添加在线用户失败: tokenValue={}, loginId={}", tokenValue, loginId, e);
        }
    }

    @Override
    public void removeOnlineUser(String tokenValue) {
        try {
            // 更新为离线状态
            LambdaUpdateWrapper<SysOnlineUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysOnlineUser::getTokenValue, tokenValue)
                    .set(SysOnlineUser::getStatus, 0) // 离线
                    .set(SysOnlineUser::getLogoutTime, LocalDateTime.now())
                    .set(SysOnlineUser::getLogoutType, 1); // 主动登出

            sysOnlineUserMapper.delete(updateWrapper);
            
            log.info("移除在线用户成功: token={}", tokenValue);
        } catch (Exception e) {
            log.error("移除在线用户失败: tokenValue={}", tokenValue, e);
        }
    }

    @Override
    public void updateLastActivityTime(String tokenValue) {
        try {
            LambdaUpdateWrapper<SysOnlineUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysOnlineUser::getTokenValue, tokenValue)
                    .set(SysOnlineUser::getLastActivityTime, LocalDateTime.now());

            sysOnlineUserMapper.update(null, updateWrapper);
        } catch (Exception e) {
            log.error("更新最后活动时间失败: tokenValue={}", tokenValue, e);
        }
    }

    @Override
    public IPage<SysOnlineUser> getOnlineUsersPage(Page<SysOnlineUser> page, String username) {
        try {
            LambdaQueryChainWrapper<SysOnlineUser> queryWrapper = new LambdaQueryChainWrapper<SysOnlineUser>(this.sysOnlineUserMapper);
            queryWrapper.eq(SysOnlineUser::getStatus, 1); // 只查询在线用户
            
            // 如果指定了用户名,进行模糊查询
            if (StrUtil.isNotBlank(username)) {
                queryWrapper.and(wrapper -> wrapper
                        .like(SysOnlineUser::getUsername, username)
                        .or()
                        .like(SysOnlineUser::getRealName, username)
                );
            }
            
            // 按登录时间降序排序
            queryWrapper.orderByDesc(SysOnlineUser::getLoginTime);
            
            IPage<SysOnlineUser> result = queryWrapper.page(page);
            
            // 检查是否被封禁
            for (SysOnlineUser user : result.getRecords()) {
                user.setBanned(StpUtil.isDisable(user.getUserId()));
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取在线用户分页列表失败", e);
            return new Page<>();
        }
    }

    @Override
    public List<SysOnlineUser> getOnlineUsers(String username) {
        try {
            LambdaQueryWrapper<SysOnlineUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysOnlineUser::getStatus, 1); // 只查询在线用户
            
            // 如果指定了用户名,进行模糊查询
            if (StrUtil.isNotBlank(username)) {
                queryWrapper.and(wrapper -> wrapper
                        .like(SysOnlineUser::getUsername, username)
                        .or()
                        .like(SysOnlineUser::getRealName, username)
                );
            }
            
            // 按登录时间降序排序
            queryWrapper.orderByDesc(SysOnlineUser::getLoginTime);
            
            List<SysOnlineUser> sysOnlineUsers = sysOnlineUserMapper.selectList(queryWrapper);
            
            // 检查是否被封禁
            for (SysOnlineUser user : sysOnlineUsers) {
                user.setBanned(StpUtil.isDisable(user.getUserId()));
            }
            
            return sysOnlineUsers;
        } catch (Exception e) {
            log.error("获取在线用户列表失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public SysOnlineUser getOnlineUser(String tokenValue) {
        LambdaQueryWrapper<SysOnlineUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysOnlineUser::getTokenValue, tokenValue)
                .eq(SysOnlineUser::getStatus, 1);
        return sysOnlineUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void kickoutUser(String tokenValue) {
        try {
            // 先通知前端
            SysOnlineUser sysOnlineUser = getOnlineUser(tokenValue);
            if (sysOnlineUser != null) {
                notifyUserKickout(tokenValue, sysOnlineUser.getUserId());
                
                // 更新数据库状态
                LambdaUpdateWrapper<SysOnlineUser> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(SysOnlineUser::getTokenValue, tokenValue)
                        .set(SysOnlineUser::getStatus, 0)
                        .set(SysOnlineUser::getLogoutTime, LocalDateTime.now())
                        .set(SysOnlineUser::getLogoutType, 2); // 被踢下线
                
                sysOnlineUserMapper.update(null, updateWrapper);
            }
            
            // 踢下线
            StpUtil.kickoutByTokenValue(tokenValue);
            log.info("强制用户下线成功: token={}", tokenValue);
        } catch (Exception e) {
            log.error("强制用户下线失败: tokenValue={}", tokenValue, e);
            throw new RuntimeException("强制下线失败", e);
        }
    }

    @Override
    public void batchKickoutUser(List<String> tokenValues) {
        for (String tokenValue : tokenValues) {
            try {
                kickoutUser(tokenValue);
            } catch (Exception e) {
                log.error("批量强制下线失败: tokenValue={}", tokenValue, e);
            }
        }
    }

    @Override
    public void banUser(Long userId, long banSeconds, String reason) {
        try {
            // 封禁用户
            StpUtil.disable(userId, banSeconds);
            
            // 踢出所有在线会话
            kickoutAllSessions(userId, null);
            
            // 通知用户被封禁
            notifyUserBanned(userId, banSeconds);
            
            log.info("封禁用户成功: userId={}, banSeconds={}, reason={}", userId, banSeconds, reason);
        } catch (Exception e) {
            log.error("封禁用户失败: userId={}", userId, e);
            throw new RuntimeException("封禁用户失败", e);
        }
    }

    @Override
    public void unbanUser(Long userId) {
        try {
            StpUtil.untieDisable(userId);
            log.info("解封用户成功: userId={}", userId);
        } catch (Exception e) {
            log.error("解封用户失败: userId={}", userId, e);
            throw new RuntimeException("解封用户失败", e);
        }
    }

    @Override
    public void notifyUserKickout(String tokenValue, Object loginId) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", loginId);
            data.put("tokenValue", tokenValue);

            WebSocketMessage message = WebSocketMessage.builder()
                    .type(MessageType.AUTH_KICKOUT.getCode())
                    .title("强制下线通知")
                    .message("您已被管理员强制下线")
                    .data(data)
                    .level("warning")
                    .requireConfirm(true)
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            // 使用通用Topic广播,由前端根据userId过滤
            messagePushService.pushToTopic("auth", message);
            log.info("通知用户被踢下线: loginId={}, token={}", loginId, tokenValue);
        } catch (Exception e) {
            log.error("通知用户被踢下线失败: loginId={}", loginId, e);
        }
    }

    @Override
    public void notifyUserReplaced(String tokenValue, Object loginId) {
        try {
            // 先更新数据库状态为被顶下线
            LambdaUpdateWrapper<SysOnlineUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysOnlineUser::getTokenValue, tokenValue)
                    .set(SysOnlineUser::getStatus, 0)
                    .set(SysOnlineUser::getLogoutTime, LocalDateTime.now())
                    .set(SysOnlineUser::getLogoutType, 3); // 被顶下线
            
            sysOnlineUserMapper.update(null, updateWrapper);
            
            // 推送通知
            Map<String, Object> data = new HashMap<>();
            data.put("userId", loginId);
            data.put("tokenValue", tokenValue);
            
            WebSocketMessage message = WebSocketMessage.builder()
                    .type(MessageType.AUTH_REPLACED.getCode())
                    .title("账号被顶下线")
                    .message("您的账号在其他地方登录,当前会话已失效")
                    .data(data)
                    .level("warning")
                    .requireConfirm(true)
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            // 使用通用Topic广播,由前端根据userId过滤
            messagePushService.pushToTopic("auth", message);
            log.info("通知用户被顶下线: loginId={}, token={}", loginId, tokenValue);
        } catch (Exception e) {
            log.error("通知用户被顶下线失败: loginId={}", loginId, e);
        }
    }

    @Override
    public void notifyUserBanned(Object loginId, long disableTime) {
        try {
            long minutes = disableTime / 60;
            String timeText = minutes > 60
                    ? (minutes / 60) + "小时"
                    : minutes + "分钟";
            
            Map<String, Object> data = new HashMap<>();
            data.put("disableTime", disableTime);
            data.put("disableMinutes", minutes);
            data.put("userId", loginId);
            
            WebSocketMessage message = WebSocketMessage.builder()
                    .type(MessageType.AUTH_BANNED.getCode())
                    .title("账号封禁通知")
                    .message("您的账号已被封禁,封禁时长: " + timeText)
                    .data(data)
                    .level("error")
                    .requireConfirm(true)
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            // 使用通用Topic广播,由前端根据userId过滤
            messagePushService.pushToTopic("auth", message);
            log.info("通知用户被封禁: loginId={}, disableTime={}", loginId, disableTime);
        } catch (Exception e) {
            log.error("通知用户被封禁失败: loginId={}", loginId, e);
        }
    }

    @Override
    public List<String> getUserTokens(Long userId) {
        LambdaQueryWrapper<SysOnlineUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysOnlineUser::getUserId, userId)
                .eq(SysOnlineUser::getStatus, 1)
                .select(SysOnlineUser::getTokenValue);
        
        List<SysOnlineUser> list = sysOnlineUserMapper.selectList(queryWrapper);
        return list.stream()
                .map(SysOnlineUser::getTokenValue)
                .toList();
    }

    @Override
    public void kickoutAllSessions(Long userId, String excludeToken) {
        List<String> tokens = getUserTokens(userId);
        for (String token : tokens) {
            if (!token.equals(excludeToken)) {
                try {
                    kickoutUser(token);
                } catch (Exception e) {
                    log.error("踢出会话失败: userId={}, token={}", userId, token, e);
                }
            }
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }
                if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        } catch (Exception e) {
            log.error("获取客户端IP失败", e);
        }
        return "unknown";
    }

    /**
     * 获取User-Agent
     */
    private String getUserAgent() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("User-Agent");
            }
        } catch (Exception e) {
            log.error("获取User-Agent失败", e);
        }
        return "unknown";
    }

    /**
     * 根据IP获取登录地点(简单实现,可接入第三方IP库)
     */
    private String getLoginLocation(String ip) {
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return "本地";
        }
        // TODO: 接入IP地址库获取真实地理位置
        return "未知";
    }
}

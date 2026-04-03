package com.taotao.cloud.tenant.biz.interfaces.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdframe.forge.plugin.system.entity.SysOnlineUser;
import com.taotao.cloud.tenant.biz.application.service.service.ISysOnlineUserService;

import com.mdframe.forge.starter.core.annotation.crypto.ApiDecrypt;
import com.mdframe.forge.starter.core.annotation.crypto.ApiEncrypt;
import com.mdframe.forge.starter.core.annotation.tenant.IgnoreTenant;
import com.mdframe.forge.starter.websocket.domain.WebSocketMessage;
import com.mdframe.forge.starter.websocket.enums.MessageType;
import com.mdframe.forge.starter.websocket.service.IMessagePushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户管理控制器
 */
@RestController
@RequestMapping("/auth/online")
@RequiredArgsConstructor
@ApiDecrypt
@ApiEncrypt
public class SysOnlineUserController {

    private final ISysOnlineUserService onlineUserService;
    
    
    private final IMessagePushService messagePushService;
    
    @IgnoreTenant
    @GetMapping("/test")
    @SaIgnore
    public void banUser() {
        WebSocketMessage message = WebSocketMessage.builder()
                .type(MessageType.AUTH_BANNED.getCode())
                .title("账号封禁通知")
                .message("您的账号已被封禁,封禁时长: " + 1)
                .level("error")
                .requireConfirm(true)
                .timestamp(System.currentTimeMillis())
                .build();
        messagePushService.pushToTopic("auth",message);
    }

    /**
     * 分页获取在线用户列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param username 用户名(可选,用于搜索)
     * @return 在线用户分页数据
     */
    @GetMapping("/page")
    public Result<IPage<SysOnlineUser>> getOnlineUsersPage(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String username) {
        Page<SysOnlineUser> page = new Page<>(pageNum, pageSize);
        IPage<SysOnlineUser> result = onlineUserService.getOnlineUsersPage(page, username);
        return Result.success(result);
    }

    /**
     * 获取在线用户列表（不分页）
     *
     * @param username 用户名(可选,用于搜索)
     * @return 在线用户列表
     */
    @GetMapping("/list")
    public Result<List<SysOnlineUser>> getOnlineUsers(@RequestParam(required = false) String username) {
        List<SysOnlineUser> sysOnlineUsers = onlineUserService.getOnlineUsers(username);
        return Result.success(sysOnlineUsers);
    }

    /**
     * 强制用户下线
     *
     * @param tokenValue Token值
     * @return 操作结果
     */
    @PostMapping("/kickout")
    public Result<Void> kickoutUser(@RequestParam String tokenValue) {
        onlineUserService.kickoutUser(tokenValue);
        return Result.success();
    }

    /**
     * 批量强制用户下线
     *
     * @param tokenValues Token值列表
     * @return 操作结果
     */
    @PostMapping("/batchKickout")
    public Result<Void> batchKickoutUser(@RequestBody List<String> tokenValues) {
        onlineUserService.batchKickoutUser(tokenValues);
        return Result.success();
    }

    /**
     * 封禁用户
     *
     * @param userId     用户ID
     * @param banSeconds 封禁时长(秒)
     * @param reason     封禁原因
     * @return 操作结果
     */
    @PostMapping("/ban")
    public Result<Void> banUser(@RequestParam Long userId,
                                   @RequestParam long banSeconds,
                                   @RequestParam(required = false) String reason) {
        onlineUserService.banUser(userId, banSeconds, reason);
        return Result.success();
    }

    /**
     * 解封用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/unban")
    public Result<Void> unbanUser(@RequestParam Long userId) {
        onlineUserService.unbanUser(userId);
        return Result.success();
    }

    /**
     * 获取用户的所有在线Token
     *
     * @param userId 用户ID
     * @return Token列表
     */
    @GetMapping("/userTokens")
    public Result<List<String>> getUserTokens(@RequestParam Long userId) {
        List<String> tokens = onlineUserService.getUserTokens(userId);
        return Result.success(tokens);
    }
}

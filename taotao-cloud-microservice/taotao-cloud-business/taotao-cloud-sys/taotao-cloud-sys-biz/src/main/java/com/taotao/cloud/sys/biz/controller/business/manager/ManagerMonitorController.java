package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.sys.biz.model.vo.monitor.OnlineUserInfo;
import com.taotao.cloud.sys.biz.model.vo.monitor.RedisCacheInfoDTO;
import com.taotao.cloud.sys.biz.model.vo.monitor.ServerInfo;
import com.taotao.cloud.sys.biz.model.vo.server.Server;
import com.taotao.cloud.sys.biz.service.business.MonitorApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ManagerMonitorController
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Tag(name = "监控API", description = "监控相关信息")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class ManagerMonitorController {

    @NonNull
    private MonitorApplicationService monitorApplicationService;

    @Operation(summary = "Redis信息")
    @PreAuthorize("@permission.has('monitor:cache:list')")
    @GetMapping("/cacheInfo")
    public Result<RedisCacheInfoDTO> getRedisCacheInfo() {
        RedisCacheInfoDTO redisCacheInfo = monitorApplicationService.getRedisCacheInfo();
        return Result.success(redisCacheInfo);
    }


    @Operation(summary = "服务器信息")
    @PreAuthorize("@permission.has('monitor:server:list')")
    @GetMapping("/serverInfo")
    public Result<ServerInfo> getServerInfo() {
        ServerInfo serverInfo = monitorApplicationService.getServerInfo();
        return Result.success(serverInfo);
    }

    /**
     * 获取在线用户列表
     */
    @Operation(summary = "在线用户列表")
    @PreAuthorize("@permission.has('monitor:online:list')")
    @GetMapping("/onlineUser/list")
    public Result<List<OnlineUserInfo>> list( String ipaddr, String userName ) {
        List<OnlineUserInfo> onlineUserList = monitorApplicationService.getOnlineUserList(userName, ipaddr);
        return Result.success(onlineUserList);
    }

    /**
     * 强退用户
     */
//	@Operation(summary = "强退用户")
//	@PreAuthorize("@permission.has('monitor:online:forceLogout')")
//	//@AccessLog(title = "在线用户", businessType = BusinessTypeEnum.FORCE_LOGOUT)
//	@DeleteMapping("/onlineUser/{tokenId}")
//	public Result<Void> forceLogout(@PathVariable String tokenId) {
//		CacheCenter.loginUserCache.delete(tokenId);
//		return ResponseDTO.ok();
//	}

    //***********************
    @Operation(summary = "查询服务器监控信息")
    @GetMapping
//	@SaCheckPermission("upms:sysserver:get")
    public Result<Server> getServerInfo111() throws Exception {
        Server server = new Server();
        server.copyTo();
        return Result.success(server);
    }

}

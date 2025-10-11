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
	 *
	 * @param ipaddr
	 * @param userName
	 * @return
	 */
	@Operation(summary = "在线用户列表")
	@PreAuthorize("@permission.has('monitor:online:list')")
	@GetMapping("/onlineUser/list")
	public Result<List<OnlineUserInfo>> list(String ipaddr, String userName) {
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

	//*************
//	@At("/redis/info")
//	@Ok("json")
//	@GET
//	@ApiOperation(description = "Redis信息")
//	@ApiFormParams
//	@ApiResponses
//	@SaCheckRole("sysadmin")
//	public Result<?> redisInfo() {
//		NutMap nutMap = NutMap.NEW();
//		String info = redisService.info();
//		String[] infos = Strings.splitIgnoreBlank(info, "\r\n");
//		for (String str : infos) {
//			if (Strings.sNull(str).contains(":")) {
//				String[] v = Strings.splitIgnoreBlank(str, ":");
//				nutMap.put(v[0], v[1]);
//			}
//		}
//		nutMap.addv("dbSize", redisService.dbSize());
//		return Result.data(nutMap);
//	}

//	@At("/server/info")
//	@Ok("json")
//	@GET
//	@ApiOperation(description = "服务器信息")
//	@ApiFormParams
//	@ApiResponses
//	@SaCheckRole("sysadmin")
//	public Result<?> serverInfo() {
//		SystemInfo si = new SystemInfo();
//		HardwareAbstractionLayer hal = si.getHardware();
//		return Result.data(
//			NutMap.NEW().addv("cpu", OshiServer.getCpu(hal.getProcessor()))
//				.addv("jvm", OshiServer.getJvmInfo())
//				.addv("mem", OshiServer.getMemInfo(hal.getMemory()))
//				.addv("sys", OshiServer.getSysInfo())
//				.addv("files", OshiServer.getSysFiles(si.getOperatingSystem()))
//		);
//	}
//
//	@At("/nacos/services")
//	@Ok("json")
//	@POST
//	@ApiOperation(description = "Nacos服务列表")
//	@ApiFormParams(
//		{
//			@ApiFormParam(name = "serviceNameParam", description = "服务名称"),
//			@ApiFormParam(name = "groupNameParam", description = "分组名称"),
//			@ApiFormParam(name = "pageNo", description = "页码", example = "1", type = "integer", required = true, check = true),
//			@ApiFormParam(name = "pageSize", description = "页大小", example = "10", type = "integer", required = true, check = true)
//		}
//	)
//	@ApiResponses
//	@SaCheckRole("sysadmin")
//	public Result<?> list(@Param("serviceNameParam") String serviceNameParam, @Param("groupNameParam") String groupNameParam, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize) {
//		return Result.error();
//	}
//
//	@At("/nacos/service")
//	@Ok("json")
//	@POST
//	@ApiOperation(description = "Nacos服务详情")
//	@ApiFormParams(
//		{
//			@ApiFormParam(name = "serviceName", description = "服务名称", required = true, check = true),
//			@ApiFormParam(name = "groupName", description = "分组名称", required = true, check = true)
//		}
//	)
//	@ApiResponses
//	@SaCheckRole("sysadmin")
//	public Result<?> service(@Param("serviceName") String serviceName, @Param("groupName") String groupName) {
//		return Result.error();
//	}
}

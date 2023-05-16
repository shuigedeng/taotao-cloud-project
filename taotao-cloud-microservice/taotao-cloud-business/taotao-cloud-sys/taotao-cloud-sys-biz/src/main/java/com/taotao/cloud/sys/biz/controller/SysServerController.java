package com.taotao.cloud.sys.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.huanxing.cloud.common.core.entity.Result;
import com.huanxing.cloud.upms.common.vo.server.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CPU相关信息
 *
 * @author lijx
 * @since 2022/5/21 15:35
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/sysserver")
@Api(value = "sysserver", tags = "服务器监控")
public class SysServerController {

	@ApiOperation(value = "查询服务器监控信息")
	@GetMapping
	@SaCheckPermission("upms:sysserver:get")
	public Result<Server> getServerInfo() throws Exception {
		Server server = new Server();
		server.copyTo();
		return Result.success(server);
	}

}

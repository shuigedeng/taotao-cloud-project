package com.taotao.cloud.member.biz.controller.buyer.connect;


import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.biz.connect.service.ConnectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,app/小程序 联合登录
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-app/小程序联合登录API", description = "买家端-app/小程序联合登录API")
@RequestMapping("/member/buyer/passport/connect/bind")
public class ConnectBindController {

	private final ConnectService connectService;

	@Operation(summary = "unionId绑定", description = "unionId绑定", method = CommonConstant.POST)
	@RequestLogger(description = "unionId绑定")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public void unionIdBind(@RequestParam String unionId, @RequestParam String type) {
		connectService.bind(unionId, type);
	}

	@Operation(summary = "unionID解绑", description = "unionID解绑", method = CommonConstant.POST)
	@RequestLogger(description = "unionID解绑")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/unbind")
	public void unionIdunBind(@RequestParam String type) {
		connectService.unbind(type);
	}

	@Operation(summary = "绑定列表", description = "绑定列表", method = CommonConstant.GET)
	@RequestLogger(description = "绑定列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/list")
	public Result<List<String>> bindList() {
		return Result.success(connectService.bindList());
	}


}

package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.vo.alipay.EmailVo;
import com.taotao.cloud.sys.biz.entity.EmailConfig;
import com.taotao.cloud.sys.biz.service.IEmailConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@Tag(name = "平台管理端-邮件管理API", description = "平台管理端-邮件管理API")
@RequestMapping("/sys/tools/email")
public class EmailController {

	private final IEmailConfigService emailService;

	public EmailController(IEmailConfigService emailService) {
		this.emailService = emailService;
	}

	@Operation(summary = "查询邮件配置信息", description = "查询邮件配置信息", method = CommonConstant.GET)
	@RequestLogger(description = "查询邮件配置信息")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<EmailConfig> get() {
		return Result.success(emailService.find());
	}

	@Operation(summary = "配置邮件", description = "配置邮件", method = CommonConstant.PUT)
	@RequestLogger(description = "配置邮件")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> emailConfig(@Validated @RequestBody EmailConfig emailConfig) {
		emailService.update(emailConfig, emailService.find());
		return Result.success(true);
	}

	@Operation(summary = "发送邮件", description = "发送邮件", method = CommonConstant.POST)
	@RequestLogger(description = "发送邮件")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> send(@Validated @RequestBody EmailVo emailVo) throws Exception {
		emailService.send(emailVo, emailService.find());
		return Result.success(true);
	}
}

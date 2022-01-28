package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.vo.alipay.EmailVo;
import com.taotao.cloud.sys.biz.entity.EmailConfig;
import com.taotao.cloud.sys.biz.service.EmailConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/email")
public class EmailController {

	private final EmailConfigService emailService;

	public EmailController(EmailConfigService emailService) {
		this.emailService = emailService;
	}

	@GetMapping
	public ResponseEntity<Object> get() {
		return new ResponseEntity<>(emailService.find(), HttpStatus.OK);
	}

	@RequestLogger("配置邮件")
	@PutMapping
	@ApiOperation("配置邮件")
	public ResponseEntity<Object> emailConfig(@Validated @RequestBody EmailConfig emailConfig) {
		emailService.update(emailConfig, emailService.find());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestLogger("发送邮件")
	@PostMapping
	@ApiOperation("发送邮件")
	public ResponseEntity<Object> send(@Validated @RequestBody EmailVo emailVo) throws Exception {
		emailService.send(emailVo, emailService.find());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

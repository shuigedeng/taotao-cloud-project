package com.taotao.cloud.message.biz.austin.web.controller;


import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.biz.austin.api.domain.SendRequest;
import com.taotao.cloud.message.biz.austin.api.domain.SendResponse;
import com.taotao.cloud.message.biz.austin.api.service.SendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 三歪
 */
@RestController
@Tag(name = "pc端-发送消息API", description = "pc端-发送消息API")
public class SendController {

	@Autowired
	private SendService sendService;


	/**
	 * 发送消息接口
	 * 入参完整示例：curl -XPOST "127.0.0.1:8080/send"  -H 'Content-Type: application/json'  -d '{"code":"send","messageParam":{"receiver":"13788888888","variables":{"title":"yyyyyy","contentValue":"6666164180"}},"messageTemplateId":1}'
	 *
	 * @return
	 */
	@Operation(summary = "下发接口", description = "多渠道多类型下发消息，目前支持邮件和短信，类型支持：验证码、通知类、营销类。")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/send")
	public SendResponse send(@RequestBody SendRequest sendRequest) {
		return sendService.send(sendRequest);
	}
}

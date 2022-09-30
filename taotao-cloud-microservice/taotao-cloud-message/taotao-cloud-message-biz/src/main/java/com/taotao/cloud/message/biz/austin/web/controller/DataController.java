package com.taotao.cloud.message.biz.austin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.web.service.DataService;
import com.taotao.cloud.message.biz.austin.web.vo.DataParam;
import com.taotao.cloud.message.biz.austin.web.vo.amis.EchartsVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.SmsTimeLineVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.UserTimeLineVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取数据接口（全链路追踪)
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/trace")
@Tag(name = "pc端-获取数据接口（全链路追踪)API", description = "pc端-获取数据接口（全链路追踪)API")
public class DataController {
	@Autowired
	private DataService dataService;

	@Operation(summary = "获取【当天】用户接收消息的全链路数据", description = "获取【当天】用户接收消息的全链路数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/user")
	public BasicResultVO getUserData(@RequestBody DataParam dataParam) {
		UserTimeLineVo traceUserInfo = dataService.getTraceUserInfo(dataParam.getReceiver());

		return BasicResultVO.success(traceUserInfo);
	}

	@Operation(summary = "获取消息模板全链路数据", description = "获取消息模板全链路数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/messageTemplate")
	public BasicResultVO getMessageTemplateData(@RequestBody DataParam dataParam) {
		EchartsVo echartsVo = EchartsVo.builder().build();
		if (StrUtil.isNotBlank(dataParam.getBusinessId())) {
			echartsVo = dataService.getTraceMessageTemplateInfo(dataParam.getBusinessId());
		}
		return new BasicResultVO<>(RespStatusEnum.SUCCESS, echartsVo);
	}

	@Operation(summary = "获取短信下发数据", description = "获取短信下发数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/sms")
	public BasicResultVO getSmsData(@RequestBody DataParam dataParam) {
		if (dataParam == null || dataParam.getDateTime() == null || dataParam.getReceiver() == null) {
			return new BasicResultVO<>(RespStatusEnum.SUCCESS, new SmsTimeLineVo());
		}

		SmsTimeLineVo smsTimeLineVo = dataService.getTraceSmsInfo(dataParam);

		return new BasicResultVO<>(RespStatusEnum.SUCCESS, smsTimeLineVo);
	}

}

package com.taotao.cloud.message.biz.austin.web.controller;


import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.cron.handler.RefreshDingDingAccessTokenHandler;
import com.taotao.cloud.message.biz.austin.cron.handler.RefreshGeTuiAccessTokenHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author 3y
 */
@RestController
@Tag(name = "pc端-手动刷新tokenAPI", description = "pc端-手动刷新tokenAPI")
public class RefreshTokenController {


	@Autowired
	private RefreshDingDingAccessTokenHandler refreshDingDingAccessTokenHandler;
	@Autowired
	private RefreshGeTuiAccessTokenHandler refreshGeTuiAccessTokenHandler;

	/**
	 * 按照不同的渠道刷新对应的Token，channelType取值来源com.taotao.cloud.message.biz.austin.common.enums.ChannelType
	 *
	 * @param channelType
	 * @return
	 */
	@Operation(summary = "手动刷新token", description = "钉钉/个推 token刷新")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping({"/refresh"})
	public BasicResultVO refresh(Integer channelType) {
		if (ChannelType.PUSH.getCode().equals(channelType)) {
			refreshGeTuiAccessTokenHandler.execute();
		}
		if (ChannelType.DING_DING_WORK_NOTICE.getCode().equals(channelType)) {
			refreshDingDingAccessTokenHandler.execute();

		}
		return BasicResultVO.success("刷新成功");
	}

}

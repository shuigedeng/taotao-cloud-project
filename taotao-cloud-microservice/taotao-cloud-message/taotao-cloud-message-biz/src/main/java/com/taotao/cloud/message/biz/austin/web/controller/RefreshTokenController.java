package com.taotao.cloud.message.biz.austin.web.controller;


import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.cron.handler.RefreshDingDingAccessTokenHandler;
import com.taotao.cloud.message.biz.austin.cron.handler.RefreshGeTuiAccessTokenHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author 3y
 */
@Api(tags = {"手动刷新token的接口"})
@RestController
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
	@ApiOperation(value = "手动刷新token", notes = "钉钉/个推 token刷新")
	@GetMapping("/refresh")
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
